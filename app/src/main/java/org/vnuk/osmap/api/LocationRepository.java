package org.vnuk.osmap.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LocationRepository {
    private static final String TAG = LocationRepository.class.getSimpleName();

    private APIService apiService;
    private MutableLiveData<List<LocationEntity>> mldLocations;

    public LocationRepository() {
        apiService = APIClient.getClient().create(APIService.class);
        mldLocations = new MutableLiveData<>();
        Log.v(TAG, "Finished creating.");
    }

    public void fetchLocations() {
        Log.v(TAG, "Fetching locations.");
        Call<List<LocationEntity>> call = apiService.getLocations();

        call.enqueue(new Callback<List<LocationEntity>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<List<LocationEntity>> call, Response<List<LocationEntity>> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "GET Locations: call was successful.");
                    mldLocations.setValue(response.body());
                } else {
                    Log.e(TAG, "GET Locations: There was some kind of error.");
                    // When working with (more detailed) API error parsing would be performed here.
                    // In this case, simplest way would be setting value as null and informing user about it.
                    mldLocations.setValue(null);
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<List<LocationEntity>> call, Throwable t) {
                Log.e(TAG, "GET Locations: call failed.");
                mldLocations.setValue(null);
                call.cancel();
            }
        });
    }

    public MutableLiveData<List<LocationEntity>> getMldLocations() {
        return mldLocations;
    }
}
