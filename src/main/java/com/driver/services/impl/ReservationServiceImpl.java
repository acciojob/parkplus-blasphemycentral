package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception
    {
        SpotType spotType;
        if (numberOfWheels == 2) spotType = SpotType.TWO_WHEELER;
        else if(numberOfWheels == 4) spotType = SpotType.FOUR_WHEELER;
        else spotType = SpotType.OTHERS;

        List<Spot> spots = spotRepository3.findBySpotType(spotType);
        Spot spo = null;
        int min = Integer.MAX_VALUE;
        for(Spot spot : spots) if(!spot.getOccupied() && spot.getPricePerHour()<min)
        {
            spo = spot;
            min = spot.getPricePerHour();
        }

        if(min == Integer.MAX_VALUE) throw new Exception("Cannot make reservation");

        User user;
        try
        {
            user = userRepository3.findById(userId).get();
        }
        catch (Exception e)
        {
            throw new Exception("Cannot make reservation");
        }

        ParkingLot parkingLot;
        try
        {
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        }
        catch (Exception e)
        {
            throw new Exception("Cannot make reservation");
        }
        Reservation reservation = Reservation.builder().numberOfHours(timeInHours).build();
        reservation.setSpot(spo);
        reservation.setUser(user);

        user.getReservationList().add(reservation);
        spo.getReservationList().add(reservation);

        userRepository3.save(user);
        spotRepository3.save(spo);

        return reservation;
    }
}
