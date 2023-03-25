package com.driver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Spot
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    SpotType spotType;

    int pricePerHour;

    boolean occupied;

    @ManyToOne
    @JoinColumn
    ParkingLot parkingLot;


    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL)
    List<Reservation> reservationList = new ArrayList<>();
}
