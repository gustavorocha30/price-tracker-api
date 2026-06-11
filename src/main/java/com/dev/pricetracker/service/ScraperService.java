package com.dev.pricetracker.service;

import com.dev.pricetracker.entity.Product;
import com.dev.pricetracker.repository.ProductRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScraperService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    // Nosso disfarce de navegador
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    @Scheduled(fixedRate = 20000)
    public void rastrearProdutosCadastrados() {

        List<Product> produtosMonitorados = productRepository.findAll();

        if (produtosMonitorados.isEmpty()) {
            System.out.println("💤 Robô dormindo... Nenhum produto cadastrado no painel.");
            return;
        }

        System.out.println("🤖 [AGENDADOR] Iniciando varredura em " + produtosMonitorados.size() + " produto(s)...");

        for (Product produto : produtosMonitorados) {
            try {
                // =========================================================
                // 1. REGRA DE PERMISSÃO: Aceitar apenas ML ou Toscrape
                // =========================================================
                if (!produto.getUrl().contains("mercadolivre.com.br") && !produto.getUrl().contains("toscrape.com")) {
                    System.out.println("⚠️ Pulando link não suportado: " + produto.getUrl());
                    continue;
                }

                // =========================================================
                // 2. CONEXÃO E DOWNLOAD DA PÁGINA
                // =========================================================
                Document documentoHTML = Jsoup.connect(produto.getUrl())
                        .userAgent(USER_AGENT)
                        .header("Accept-Language", "pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                        .get();

                // =========================================================
                // 3. EXTRAÇÃO INTELIGENTE (Plano A e Plano B)
                // =========================================================
                Element elementoTitulo = documentoHTML.selectFirst("h1.ui-pdp-title"); // Tenta o Mercado Livre
                if (elementoTitulo == null) {
                    elementoTitulo = documentoHTML.selectFirst("h1"); // Falhou? Tenta a Livraria
                }

                Element elementoPreco = documentoHTML.selectFirst(".andes-money-amount__fraction"); // Tenta o Mercado Livre
                if (elementoPreco == null) {
                    elementoPreco = documentoHTML.selectFirst(".price_color"); // Falhou? Tenta a Livraria
                }

                // Proteção contra bloqueios
                if (elementoTitulo == null || elementoPreco == null) {
                    System.out.println("🛡️ BLOQUEIO ou Layout Desconhecido no link: " + produto.getUrl());
                    continue;
                }

                // Extrai o texto limpo
                String tituloExtraido = elementoTitulo.text();
                // A expressão regular [^\\d.] limpa letras tanto do R$ quanto do £
                String precoLimpo = elementoPreco.text().replaceAll("[^\\d.]", "");
                Double precoFinal = Double.parseDouble(precoLimpo);

                // =========================================================
                // 4. ATUALIZA O BANCO DE DADOS
                // =========================================================
                produto.setTitle(tituloExtraido);
                produto.setCurrentPrice(precoFinal);
                produto.setLastChecked(LocalDateTime.now());

                productRepository.save(produto);
                System.out.println("✅ Atualizado no banco: " + tituloExtraido + " | Valor: " + precoFinal);

                // =========================================================
                // 5. MOTOR DE ALERTA DE PREÇO
                // =========================================================
                if (produto.getTargetPrice() != null && precoFinal <= produto.getTargetPrice()) {
                    System.out.println("🚨 PROMOÇÃO DETECTADA! Enviando e-mail...");

                    // TODO: COLOQUE SEU E-MAIL AQUI
                    String meuEmail = "SEU_EMAIL_AQUI@gmail.com";

                    emailService.enviarAlertaDePreco(meuEmail, tituloExtraido, precoFinal, produto.getUrl());
                }

            } catch (Exception e) {
                System.err.println("❌ Erro ao ler produto ID " + produto.getId() + ": " + e.getMessage());
            }
        }
    }
}