package com.example.fedexrouting;

import com.google.android.gms.maps.model.LatLng;

/**
 * Thomas Bridges, Charles Porter, Joel Bernhardt
 * Technocrats
 * FedExRouting
 *
 * Contributions
 * Thomas Bridges - 100%
 *
 * A Customer is made from a line from the file where the data is stored. The line is tokenized and
 * then the info is put into the corresponding variables.
 *
 * A Customer is used to make a marker since a customer can hold a latitude and longitude value.
 */

public class Customer {

    private String name, address, city, state, zipcode, note, delivered;
    private double latitude, longitude;
    private int priority;

    //Used to make a blank customer. This is really only used when we need a temporary customer,
    //such as when we want to swap customers in the list
    public Customer(){

        name = null;
        address = null;
        city = null;
        state = null;
        zipcode = null;
        note = null;
        delivered = null;
        latitude = 0.0;
        longitude = 0.0;
        priority = 0;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {

        if(note.equals("null")){
            this.note = "";
        }
        else
            this.note = note;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng getLatLng(){

        LatLng result = new LatLng(latitude, longitude);

        return  result;
    }
    @Override
    public String toString(){

        return name;

    }

    /*
    distanceTo calculates the distance from this customer to a different customer. The distance is
    found with the distance formula.
     */
    public double distanceTo(Customer customer){
        double xDistance = Math.abs(getLatitude() - customer.getLatitude());
        double yDistance = Math.abs(getLongitude() - customer.getLongitude());
        double distance =  Math.sqrt( (xDistance*xDistance) + (yDistance*yDistance) );

        return distance;
    }
}
