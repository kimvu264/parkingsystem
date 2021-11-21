package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inTime = ticket.getInTime().getTime();
        long outTime = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct

        float duration = ((float) (outTime - inTime)) / (3600 * 1000);

        double price = 0d;
        if (duration > 0.5) {
            switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                    price = roundPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    price = roundPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default: throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
        ticket.setPrice(price);
    }

    private double roundPrice(final double price) {
        return Math.round(price * 100.0) / 100.0;
    }
}
