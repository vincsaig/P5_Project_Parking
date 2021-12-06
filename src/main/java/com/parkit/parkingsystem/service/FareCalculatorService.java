package com.parkit.parkingsystem.service;

import java.util.List;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(List<Ticket> tickets){
        Ticket ticket = tickets.get(0);
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        
        double duration = (((outHour - inHour)/1000)/60)/60;

        //Parking durations under or equal to 30 minutes are free
        if (duration > 0.5){
            switch (ticket.getParkingSpot().getParkingType()){
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
            }
            //If the list happens to have multiple tickets, the user already parked here, so add a discount
            if(tickets.size() > 1){
                ticket.setPrice(ticket.getPrice() * Fare.DISCOUNT);
            }
        }
        else{
            ticket.setPrice(0);
        }
    }
}