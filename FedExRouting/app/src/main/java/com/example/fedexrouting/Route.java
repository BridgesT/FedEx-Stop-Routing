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
 * Route keeps an array of all of the stops. A "route" is the order of the items in the array.
 * Array[0] = stop 1, Array[1] = stop 2, etc
 */

public class Route{

    // Holds our tour of cities
    public ArrayList route = new ArrayList<Customer>();
    // Cache
    private double distance = 0.0;

    // Constructs a blank tour
    public Route(){
        for (int i = 0; i < StopManager.numberOfStops(); i++) {
            route.add(null);
        }
    }

    // Constructs a tour from another tour
    public Route(ArrayList route){
        this.route = (ArrayList) route.clone();
    }

    // Returns tour information
    public ArrayList getRoute(){
        return route;
    }

    // Creates a random individual
    public void generateIndividual() {
        // Loop through all our destination cities and add them to our tour
        for (int stopIndex = 0; stopIndex < StopManager.numberOfStops(); stopIndex++) {
            setCustomer(stopIndex, StopManager.getStop(stopIndex));
        }
        // Randomly reorder the tour
        //Collections.shuffle(route);
    }

    // Gets a city from the tour
    public Customer getCustomer(int tourPosition) {
        return (Customer) route.get(tourPosition);
    }

    // Sets a city in a certain position within a tour
    public void setCustomer(int tourPosition, Customer customer) {
        route.set(tourPosition, customer);
        // If the tours been altered we need to reset the fitness and distance
        distance = 0;
    }

    // Gets the total distance of the tour
    public double getDistance(){

        double distance = 0;

        for(int i = 0; i < route.size()-1; i++){

            distance += getCustomer(i).distanceTo(getCustomer(i+1));
            //Log.d("MapsActivity", ""+distance);

        }
        return distance ;
    }

    // Get number of cities on our tour
    public int routeSize() {
        return route.size();
    }

    @Override
    public String toString() {
        String geneString = "|";
        for (int i = 0; i < routeSize(); i++) {
            geneString += getCustomer(i)+"|";
        }
        return geneString;
    }
}