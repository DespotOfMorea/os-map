package org.vnuk.osmap.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("/locations")
    Call<List<LocationEntity>> getLocations();
}
