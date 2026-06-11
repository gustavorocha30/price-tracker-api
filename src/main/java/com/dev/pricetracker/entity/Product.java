package com.dev.pricetracker.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String url;

    private Double currentPrice;

    // 🌟 NOVO CAMPO: O preço que você deseja pagar para receber o alerta
    private Double targetPrice;

    private LocalDateTime lastChecked;
}