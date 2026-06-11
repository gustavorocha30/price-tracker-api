package com.dev.pricetracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Método que formata e dispara o e-mail
    public void enviarAlertaDePreco(String emailDestino, String nomeProduto, Double precoAtual, String link) {
        SimpleMailMessage mensagem = new SimpleMailMessage();

        mensagem.setTo(emailDestino);
        mensagem.setSubject("🚨 ALERTA DE PREÇO CAIU: " + nomeProduto);
        mensagem.setText("Olá!\n\nO seu robô encontrou um preço excelente para o produto que você está monitorando.\n\n"
                + "Produto: " + nomeProduto + "\n"
                + "Preço Atual: £" + precoAtual + "\n\n"
                + "Link para compra: " + link + "\n\n"
                + "Corra antes que acabe!\nAss: Seu Robô Java.");

        try {
            mailSender.send(mensagem);
            System.out.println("📧 E-mail de alerta enviado com sucesso para: " + emailDestino);
        } catch (Exception e) {
            System.err.println("❌ Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}