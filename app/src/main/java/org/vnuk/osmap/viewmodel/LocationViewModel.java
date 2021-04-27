package org.vnuk.osmap.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.vnuk.osmap.api.LocationEntity;
import org.vnuk.osmap.api.LocationRepository;

import java.util.List;

public class LocationViewModel extends AndroidViewModel {

    private LocationRepository locationRepository;
    private MutableLiveData<List<LocationEntity>> mldLocations;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationRepository = new LocationRepository();
        mldLocations = locationRepository.getMldLocations();
    }

    public void fetchLocations() {
        locationRepository.fetchLocations();
    }

    public MutableLiveData<List<LocationEntity>> getMldLocations() {
        return mldLocations;
    }
}
