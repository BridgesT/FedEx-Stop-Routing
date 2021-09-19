package com.example.fedexrouting;

import java.util.ArrayList;

/**
 * Thomas Bridges, Charles Porter, Joel Bernhardt
 * Technocrats
 * FedExRouting
 *
 * Contributions
 * Thomas Bridges - 100%
 *
 * StopManager keeps track of all of the stops. This is separate from a route because a route
 * can change. After the stops are added to the stop manager they are always there.
 *
 */

public class StopManager {

    // Holds our stops
    private static ArrayList destinationStops = new ArrayList<Customer>();

    // Adds a customer
    public static void addStop(Customer customer) {
        destinationStops.add(customer);
    }

    // Get a customer
    public static Customer getStop(int index){
        return (Customer) destinationStops.get(index);
    }

    // Get the number of customers
    public static int numberOfStops(){
        return destinationStops.size();
    }

}
