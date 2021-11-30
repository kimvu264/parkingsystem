package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    private final TicketDAO ticketDAO;

    public FareCalculatorService(final TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    public void calculateFare(Ticket ticket) {
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        long inTime = ticket.getInTime().getTime();
        long outTime = ticket.getOutTime().getTime();

        float duration = ((float) (outTime - inTime)) / (3600 * 1000);

        Ticket userRecurring = ticketDAO.getTicket(ticket.getVehicleRegNumber());
        ticket.setDiscount(userRecurring != null && userRecurring.getOutTime() != null);

        double price = 0d;
        if (duration > 0.5)
        {
            switch(ticket.getParkingSpot().getParkingType())
            {
                case CAR:
                {
                    price = duration * Fare.CAR_RATE_PER_HOUR;
                    break;
                }
                case BIKE:
                {
                    price = duration * Fare.BIKE_RATE_PER_HOUR;
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
        if(ticket.isDiscount())
        {
            price *= 0.95;
        }
        ticket.setPrice(roundPrice(price));
        }

    private double roundPrice(final double price) {
        return Math.round(price * 100.0) / 100.0;
    }
}
