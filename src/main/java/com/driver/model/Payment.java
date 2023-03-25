package com.driver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    boolean paymentCompleted;

    PaymentMode paymentMode;

    @OneToOne
    @JoinColumn
    Reservation reservation;
}
