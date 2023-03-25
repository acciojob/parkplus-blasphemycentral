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
        try
        {

            SpotType spotType;
            if (numberOfWheels <= 2) spotType = SpotType.TWO_WHEELER;
            else if (numberOfWheels <= 4) spotType = SpotType.FOUR_WHEELER;
            else spotType = SpotType.OTHERS;

            ParkingLot parkingLot;
            try
            {
                parkingLot = parkingLotRepository3.findById(parkingLotId).get();
            } catch (Exception e)
            {
                throw new Exception("Cannot make reservation");
            }

            List<Spot> spotList = parkingLot.getSpotList();
            if (spotList.size() == 0) throw new Exception("Cannot make reservation");
            Spot selectedSpot = null;
            int minPrice = Integer.MAX_VALUE;
            for (Spot spot : spotList)
            {
                if (!spot.getOccupied())
                {
                    if (spot.getSpotType() == SpotType.OTHERS)
                    {
                        int parkingPrice = spot.getPricePerHour() * timeInHours;
                        if (parkingPrice < minPrice)
                        {
                            minPrice = parkingPrice;
                            selectedSpot = spot;
                        }
                    }
                    if (spot.getSpotType() == SpotType.FOUR_WHEELER && numberOfWheels <= 4)
                    {
                        int parkingPrice = spot.getPricePerHour() * timeInHours;
                        if (parkingPrice < minPrice)
                        {
                            minPrice = parkingPrice;
                            selectedSpot = spot;
                        }
                    }
                    if (spot.getSpotType() == SpotType.TWO_WHEELER && numberOfWheels <= 2)
                    {
                        int parkingPrice = spot.getPricePerHour() * timeInHours;
                        if (parkingPrice < minPrice)
                        {
                            minPrice = parkingPrice;
                            selectedSpot = spot;
                        }
                    }
                }
            }


            User user;
            try
            {
                user = userRepository3.findById(userId).get();
            } catch (Exception e)
            {
                throw new Exception("Cannot make reservation");
            }

            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(selectedSpot);
            reservation.setUser(user);

            user.getReservationList().add(reservation);
            selectedSpot.getReservationList().add(reservation);
            selectedSpot.setOccupied(true);

            userRepository3.save(user);
            spotRepository3.save(selectedSpot);

            return reservation;
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
