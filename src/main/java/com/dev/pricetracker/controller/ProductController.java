package com.dev.pricetracker.controller;

import com.dev.pricetracker.entity.Product;
import com.dev.pricetracker.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // Libera o acesso para o nosso futuro Frontend (HTML)
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // Rota GET: O Frontend vai usar para listar os produtos na tela
    @GetMapping
    public List<Product> listarProdutos() {
        return productRepository.findAll();
    }

    // Rota POST: O Frontend vai usar para cadastrar um novo link para o robô vigiar
    @PostMapping
    public Product cadastrarProduto(@RequestBody Product product) {
        // Quando o usuário cadastra, ainda não temos o título nem o preço atual (o robô vai buscar depois)
        // Então salvamos apenas a URL e o Preço Alvo iniciais.
        return productRepository.save(product);
    }

    // Rota DELETE: Para você poder apagar um monitoramento pela tela
    @DeleteMapping("/{id}")
    public void deletarProduto(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}