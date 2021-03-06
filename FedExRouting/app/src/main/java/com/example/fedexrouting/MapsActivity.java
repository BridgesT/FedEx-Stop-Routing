package com.example.fedexrouting;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


/**
 * Thomas Bridges, Charles Porter, Joel Bernhardt
 * Technocrats
 * FedExRouting
 *
 * The following code was all done by Thomas Bridges, except a small section which will be annotated
 * with a comment. Overall the percentage is as follows
 * Thomas Bridges - 95%
 * Joel Bernhardt - 5%
 *
 * Research was split evenly between all members when trying to get the initial setup working.
 *
 * MapsActivity is the main view that the driver will see when using the app, this is responsible
 * for holding the map and the buttons that are displayed to the driver.
 */



/*
The Google map activity. This is the main display when the app is running
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //The actual Google map that the driver sees
    private GoogleMap mMap;
    //list of all the stops
    private ArrayList<Customer> stops = new ArrayList<>();
    private static final String TAG = MapsActivity.class.getSimpleName();

    //keeps track of the best route.
    Route best;
    //a counter to keep track of how many times the sort button can be used
    int sortLimit = 0;
    //used to get the real time location of the driver
    private FusedLocationProviderClient fusedLocationClient;
    //a Latitude Longitude object that is used to make the markers
    LatLng currentLocation;

    //A location object used to help find the driver. This object has a lat/long value used to make the above LatLng obj
    Location cuLoc;
    boolean sorted = false;

    /*
    onCreate This is the code that runs when the map is initiated and being created. This was
    generated by android studio for a Google Maps Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //this service is made to initiate the location services for getting the Location of yhe driver
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        //sets the styling of the map. this comes from a Json file in the resources folder
        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));
        if (!success) {
            Log.e(TAG, "Style parsing failed.");
        }


        //allows us to get and manipulate the UI settings
        mMap.getUiSettings();
        //Checks for all the permissions needed to use the app such as location and media
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
            return;
        }

        //using some of the UI settings of google maps to turn on the My Location button
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //Change the map from hybrid, satellite, none, or topography
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        /**
         * This is the code that was made by Joel
         * This code accesses the file where the stops data is saved. The file is read line by line
         * and fr each line a new customer is made and then added to the list of stops
         */
        Log.d("MapsActivity", "Start file read");
        BufferedReader br;
        try {
            String SD_CARD_PATH = Environment.getExternalStorageDirectory().toString();

            // File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(SD_CARD_PATH + "/" + "file.txt");
            //Log.d("MapsActivity","I am here1");
            br = new BufferedReader(new FileReader(file));
            String line;
            line = br.readLine();

            while (line != null) {

                makeCustomer(line);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * End Joel's Contribution
         */

        //moves camera to the first stop
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stops.get(0).getLatLng(), 11));

        makeInitialMarkers();
        //addLines();
    }


    /*
    Makes the markers of the best route. Sets the first one to green and always puts in on the top
    layer. Markers are made with latitude and longitude values which are gotten from the file
    that is loaded
     */
    public void makeMarkers() {

        if(stops.size() > 15) {

            for (int i = 0; i < 15; i++) {

                LatLng location = new LatLng(best.getCustomer(i).getLatitude(), best.getCustomer(i).getLongitude());
                if (i == 0) {

                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .zIndex(1)
                            .title(best.getCustomer(i).getName())
                            .snippet(best.getCustomer(i).getNote())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                } else
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(best.getCustomer(i).getName())
                            .snippet(best.getCustomer(i).getNote()));
            }
        }
        else{
            for (int i = 0; i < stops.size(); i++) {

                LatLng location = new LatLng(best.getCustomer(i).getLatitude(), best.getCustomer(i).getLongitude());
                if (i == 0) {

                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .zIndex(1)
                            .title(best.getCustomer(i).getName())
                            .snippet(best.getCustomer(i).getNote())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                } else
                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(best.getCustomer(i).getName())
                            .snippet(best.getCustomer(i).getNote()));
            }


        }
    }

    /*
    Makes the initial markers from the stops being loaded in. Sets the first one to green. Markers
    are made with latitude and longitude values which are gotten from the file that is loaded
     */
    public void makeInitialMarkers() {

        for (int i = 0; i < stops.size(); i++) {

            LatLng location = new LatLng(stops.get(i).getLatitude(), stops.get(i).getLongitude());
            if (i == 0) {

                mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(stops.get(i).getName())
                        .snippet(stops.get(i).getNote())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else
                mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(stops.get(i).getName())
                        .snippet(stops.get(i).getNote()));
        }
    }



    /*
    makeCustomer makes a customer from each line that is read in from the file. The line read in is
    already in the proper formatting to make sure that each data field is in the right position
    so that when the line is tokenized, the data goes to the proper field
     */
    public void makeCustomer(String line) {

        //make a customer
        Customer cust = new Customer();

        String[] tokens = line.split("%");
        cust.setName(tokens[0]);
        cust.setAddress(tokens[1]);
        cust.setCity(tokens[2]);
        cust.setState(tokens[3]);
        cust.setZipcode(tokens[4]);
        cust.setNote(tokens[5]);
        cust.setPriority(Integer.parseInt(tokens[6]));
        cust.setDelivered(tokens[7]);
        cust.setLatitude(Double.parseDouble(tokens[8]));
        cust.setLongitude(Double.parseDouble(tokens[9]));

        Log.d("MapsActivity", cust.toString());

        //add that customer to the list of stops
        stops.add(cust);
    }

    /*
    This is the method for when the "Next Stop" button is pressed. The first stop gets removed
    since that is the stop that was just delivered. The map is cleared of all the lines and markers
    and the new set of markers and new lines are made. The camera also moves to the new first stop
     */
    public void onButtonNextStop(View v) {

        if(sorted == false) {}
        else{

            if (stops.size() > 0) {

                //remove the first stop
                stops.remove(0);
                mMap.clear();
                makeMarkers();
                addLines();
                if (!(stops.size() == 0))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stops.get(0).getLatLng(), 12));
            } else {
            }
        }
    }

    /*
    The method for when the sort button is pressed. Only allows the sort to be pressed once and
    does the sorting algorithm
     */
    public void onButtonSortRoute(View v){

        Toast.makeText(MapsActivity.this, "Sorting, please wait..", Toast.LENGTH_LONG).show();
        

        if (sortLimit < 1) {
            simulatedAnnealing();
            makeMarkers();
            addLines();
            sortLimit++;
        }
        sorted = true;
    }

    /*
 The method for when the show all button is pressed. Shows all the markers and paths
  */
    public void onButtonShowAll(View v){

        makeInitialMarkers();
        addAllLines();
    }


    /*
    Finds the current location of the user. This is used to make the stop closest to the driver the
    first stop
     */
    @SuppressLint("MissingPermission")
    public void findUserLatLng() {

        //Waits until the user location is found. If this loop was not there then the program would
        //move on before the location of the user was found.
        Task<Location> result = fusedLocationClient.getLastLocation();
        while(!(result.isComplete()));

        //culoc is the current Location object of the user. This has a latitude and longitude that
        //is used to make the currentLocation latLng object which is then used to find the stop
        //that is closest to the driver
        cuLoc = result.getResult();
        result.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        System.out.println(location.getLatitude());
                        Log.d("MapsActivity", "complete");

                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                        }

                    }
                });

        double lat = cuLoc.getLatitude();
        double lon = cuLoc.getLongitude();
        currentLocation = new LatLng(lat,lon);
    }

    /*
    finds the closest stop to the driver. Returns the index of in the list of stops. This index
    is then used in a different method to make sure that this index is set as the first stop.
     */
    public int findClosestStop(LatLng driver){

        //a customer object to make comparisons with
        Customer d = new Customer();
        d.setLatitude(driver.latitude);
        d.setLongitude(driver.longitude);

        int index = 0;
        double shortestDistance = 999999;
        double distanceComp;

        for(int i = 0; i < stops.size(); i++){

            distanceComp = d.distanceTo(stops.get(i));

            if(distanceComp < shortestDistance){
                shortestDistance = distanceComp;
                index =  i;

            }
        }
        return index;
    }

    /**
    simulatedAnnealing is the algorithm used to sort the stops. It works in the following steps:
     1. Clear the map of any lines or markers. They will be added back later.
     2. Find the driver's location.
     3. Swap the closest stop to the driver to the first position. The algorithm ignores this stop
     since we always want it to be the first one.
     4. Add all the stops to the route manager. Route Manager just makes a route from a list of
     customers
     5. Make the initial route. This is just the stops as they are initially. This is currently
     the "best" route so far so assign that as the best route.
     6. The system will now go through the cooling process. Read below for an explanation on the
     variables for cooling.
     7. For each step of the cooling process, swap random edges between 2 sets of stops.
     An example
     w   x
      \/    ---->  w -- x
      /\    ---->  y -- z
     y   z
     As you can see the WZ line and YX lines got swapped to WX and YZ. That is how the swap works.
     8. Get the distance of the route before and after the swap.
     9. Using a probability of acceptance function, determine if the route should be kept as the
     "best route". This acceptance probability is based on a lot of things such as how early into
     the cooling process it is, the variables used for the cooling process, the chance we will allow
     a "worse" route to be selected. Sometimes a worse route will lead to a better
     possible route in the end. This acceptance probability allows for that to happen.
     10. Apply the cooling amount and loop back to the start.
     11. After the system has cooled all the way, the route that is left is the best route found.

     Variables used are the temperature and cooling rate. These variables are used to determine how
     long the algorithm will run, which changes the acceptance probability. The higher the temp the
     longer the system has to cool which means the system can make changes longer. The lower the
     cooling rate, there is less taken off per percentage of the temperature. The way these numbers
     affect the acceptance probability is because the higher the temp, the higher chance that a
     worse off route will be chosen as the route to take. If you want to make it so there is a lower
     chance of a worse route to take, you would lower the temperature and make the cooling rate

     Credit to http://www.theprojectspot.com/tutorial-post/simulated-annealing-algorithm-for-beginners/6
     for the tutorial on how to use simulated annealing
     */
    public void simulatedAnnealing() {
        Log.d("MapsActivity", "Starting SA");
        long start = System.currentTimeMillis();

        mMap.clear();

        findUserLatLng();
        int closestIndex = findClosestStop(currentLocation);

        /*
        swaps the closest stop to the driver into index 0
        This is done because the algorithm ignores the very first stop in the the list of stops, since
        this is the closest stop to the driver so we want that stop to be first
         */
        //the customer closest to the driver
        Customer swapCust = stops.get(closestIndex);
        stops.set(closestIndex, stops.get(0));
        stops.set(0, swapCust);


        for(int i = 0; i < stops.size(); i++){

            StopManager.addStop(stops.get(i));
        }
        Log.d("MapsActivity", " "+StopManager.numberOfStops());


        // Set initial temp
        double temp = 1000000;
        // Cooling rate
        double coolingRate = 0.00001;

        // Initialize intial solution
        Route currentSolution = new Route();
        currentSolution.generateIndividual();


        double initialSolutionDistance =currentSolution.getDistance();
        System.out.println("Initial solution distance: " + initialSolutionDistance);

        // Set as current best
        best = new Route(currentSolution.getRoute());

        // Loop until system has cooled
        while (temp > 1) {
            // Create new neighbour tour
            Route newSolution = new Route(currentSolution.getRoute());

            Random rand = new Random();

            // Get a random positions in the tour, except the first one. The first one should be the
            //closest to your position
            int routePos1 = (int) (rand.nextInt(newSolution.routeSize() - 1)+1);
            int routePos2 = (int) (rand.nextInt(newSolution.routeSize() - 1)+1);

            // Get the cities at selected positions in the tour
            Customer customerSwap1 = newSolution.getCustomer(routePos1);
            Customer customerSwap2 = newSolution.getCustomer(routePos2);

            // Swap them
            newSolution.setCustomer(routePos2, customerSwap1);
            newSolution.setCustomer(routePos1, customerSwap2);

            // Get energy of solutions
            double currentEnergy = currentSolution.getDistance();
            double neighbourEnergy = newSolution.getDistance();

            // Decide if we should accept the neighbour
            // THE .99999 IS THE CHANCE THAT A WORSE ROUTE WILL BE SELECTED. Essentially this is
            //saying that 1/10000 routes will take the worse one. This was found using trial and
            //error to see which number gave the best results
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > .99999) {
                currentSolution = new Route(newSolution.getRoute());
            }

            // Keep track of the best solution found
            if (currentSolution.getDistance() < best.getDistance()) {
                best = new Route(currentSolution.getRoute());
            }
            // Cool system
            temp *= 1-coolingRate;
        }
        long finish = System.currentTimeMillis();

        double timeElapsed = (finish - start) /60000.0;
        System.out.println("Time elasped: "+timeElapsed+" minutes");

        stops = best.getRoute();

        System.out.println("Initial distance: "+initialSolutionDistance);
        Log.d("MapsActivity","Final solution distance: " + best.getDistance());
        Log.d("MapsActivity", "Route: " + best);

    }

    /*
    The probability that a route will be selected
     */
    public static double acceptanceProbability(double energy, double newEnergy, double temperature) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }

    /*
    Adds the lines to the map between each marker
     */
    public void addLines(){
        PolylineOptions polylineOptions = new PolylineOptions();

        if(stops.size() > 15) {

            for (int i = 0; i <= 15; i++) {

                polylineOptions.add(stops.get(i).getLatLng());
            }
        }
        else{
            for (int i = 0; i < stops.size(); i++) {

                polylineOptions.add(stops.get(i).getLatLng());
            }

        }

        Polyline polyline = mMap.addPolyline(polylineOptions);
    }

    /*
   Adds the lines to the map between each marker
    */
    public void addAllLines(){
        PolylineOptions polylineOptions = new PolylineOptions();

        for(int i = 0; i < stops.size(); i++){

            polylineOptions.add(stops.get(i).getLatLng());
        }

        Polyline polyline = mMap.addPolyline(polylineOptions);
    }
}
