package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address)
    {
        ParkingLot parkingLot = ParkingLot.builder().name(name).address(address).build();
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour)
    {
        SpotType spotType;
        if (numberOfWheels == 2) spotType = SpotType.TWO_WHEELER;
        else if(numberOfWheels == 4) spotType = SpotType.FOUR_WHEELER;
        else spotType = SpotType.OTHERS;

        Spot spot = Spot.builder().spotType(spotType).pricePerHour(pricePerHour).build();
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

        spot.setParkingLot(parkingLot);
        parkingLot.getSpotList().add(spot);

        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteSpot(int spotId)
    {
        Spot spot = spotRepository1.findById(spotId).get();
        ParkingLot parkingLot = parkingLotRepository1.findById(spot.getParkingLot().getId()).get();

        spotRepository1.deleteById(spotId);
        parkingLot.getSpotList().remove(spot);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour)
    {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = spotRepository1.findById(spotId).get();

        spotRepository1.deleteById(spotId);
        parkingLot.getSpotList().remove(spot);

        spot.setPricePerHour(pricePerHour);
        parkingLot.getSpotList().add(spot);

        parkingLotRepository1.save(parkingLot);

        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId)
    {
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
