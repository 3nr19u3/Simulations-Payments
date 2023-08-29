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

    @Column(name="origin_currency", nullable = false)
    private String origin_currency;

    @Column(name="destiny_currency", nullable = false)
    private String destiny_currency;

    @Column(nullable = false, unique = true)
    private Long balanceId;

}
