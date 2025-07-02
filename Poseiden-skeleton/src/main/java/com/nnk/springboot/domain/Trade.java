package com.nnk.springboot.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "trade")
public class Trade {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer tradeId;
    @NotBlank(message = "Account is mandatory")
    private String account;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @Positive(message = "Buy quantity must be positive")
    private Double buyQuantity;

    private Double sellQuantity;
    private Double buyPrice;
    private Double sellPrice;
    private String benchmark;
    private Timestamp tradeDate;
    private String security;
    private String status;
    private String trader;
    private String book;
    private String creationName;
    private Timestamp creationDate;
    private String revisionName;
    private Timestamp revisionDate;
    private String dealName;
    private String dealType;
    private String sourceListId;
    private String side;
}
