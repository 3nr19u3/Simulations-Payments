package com.balance.balanceservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name", unique=true)
    private String name;
    @Column(name="value", nullable = false)
    private double value;
    @Column(name="isLocal", nullable = false)
    private boolean isLocal;

}
