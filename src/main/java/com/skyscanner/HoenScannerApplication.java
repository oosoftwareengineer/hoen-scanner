package com.skyscanner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyscanner.models.SearchResult;
import com.skyscanner.resources.SearchResource;
import com.fasterxml.jackson.core.type.TypeReference;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;


public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public String getName() {
        return "hoen-scanner";
    }

    @Override
    public void initialize(final Bootstrap<HoenScannerConfiguration> bootstrap) {

    }

    @Override
    public void run(final HoenScannerConfiguration configuration, final Environment environment) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Load hotels
        InputStream hotelStream = getClass().getClassLoader().getResourceAsStream("hotels.json");
        List<SearchResult> hotels = mapper.readValue(hotelStream, new TypeReference<List<SearchResult>>() {});
        hotels.forEach(h -> h.setKind("hotel"));

        // Load rental cars
        InputStream carStream = getClass().getClassLoader().getResourceAsStream("rental_cars.json");
        List<SearchResult> cars = mapper.readValue(carStream, new TypeReference<List<SearchResult>>() {});
        cars.forEach(c -> c.setKind("rental_car"));

        // Combine into one list
        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.addAll(hotels);
        searchResults.addAll(cars);

        // Register the resource
        environment.jersey().register(new SearchResource(searchResults));
    }

}
