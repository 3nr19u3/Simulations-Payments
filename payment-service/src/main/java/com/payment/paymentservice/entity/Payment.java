package com.payment.paymentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="payment", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="amount", nullable = false)
    private double amount;

    @Column(name = "origin_currency_id", nullable = false)
    private int origin_currency;

    @Column(name = "destiny_currency_id", nullable = false)
    private int destiny_currency;

    @Column(nullable = false)
    private int balanceId;

}
