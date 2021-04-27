package org.vnuk.osmap.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.vnuk.osmap.R;
import org.vnuk.osmap.api.LocationEntity;
import org.vnuk.osmap.util.Helper;
import org.vnuk.osmap.viewmodel.LocationViewModel;

import java.util.ArrayList;
import java.util.List;

import static org.vnuk.osmap.view.PasswordFragment.PASSWORD_VALUE;

public class MapActivity extends AppCompatActivity {
    private static final String TAG = MapActivity.class.getSimpleName();
    private static final String DEFAULT_TYPE = "Monument";
    private static final double DEF_ZOOM = 15d;
    private static final GeoPoint DEF_START = new GeoPoint(44.808382, 20.463508);

    private MapView map = null;
    private LocationViewModel locationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "OnCreate started.");
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_map);

        getDataFromIntent();
        setupMap();
        setupViewModel();
        Log.i(TAG, "OnCreate finished.");
    }

    @Override
    public void onResume() {
        Log.v(TAG, "OnResume.");
        super.onResume();
        // With current setup, there is no need for this line,
        // but it`s better to add it in start.
        map.onResume();
    }

    @Override
    public void onPause() {
        Log.v(TAG, "OnPause.");
        super.onPause();
        // With current setup, there is no need for this line,
        // but it`s better to add it in start.
        map.onPause();
    }

    private void getDataFromIntent() {
        Log.v(TAG, "Getting data from intent.");
        Intent intent = getIntent();
        String title = intent.getStringExtra(PASSWORD_VALUE);
        getSupportActionBar().setTitle(title);
    }

    private void setupMap() {
        Log.v(TAG, "Setting up map, center and zoom level.");
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(DEF_ZOOM);
        mapController.setCenter(DEF_START);
        Log.i(TAG, "Finished map setting up.");
    }

    private void setupViewModel() {
        Log.v(TAG, "Setting up ViewModel.");
        ProgressBar progressBar = findViewById(R.id.pb_map);
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);

        locationViewModel.getMldLocations().observe(this, locations -> {
            if (locations != null) {
                Log.i(TAG, "New locations (" + locations.size() + ") fetched.");
                addLocations(locations);
            } else {
                Log.e(TAG, "There was some kind of error, while fetching locations.");
                new Helper().alerter(this, R.string.fetching_locations_error_title, R.string.fetching_locations_error_msg);
            }
            progressBar.setVisibility(View.INVISIBLE);
        });

        locationViewModel.fetchLocations();
    }

    private void addLocations(List<LocationEntity> locations) {
        Log.v(TAG, "Adding locations on map started.");
        List<OverlayItem> items = new ArrayList<>();
        for (LocationEntity location : locations)
            items.add(new OverlayItem(DEFAULT_TYPE,
                    location.getName(),
                    new GeoPoint(location.getLatitude(), location.getLongitude())));

        Drawable markerIcon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_place);
        markerIcon.setTint(Color.BLUE);

        ItemizedIconOverlay<OverlayItem> mOverlay = new ItemizedIconOverlay<>(items, markerIcon,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(MapActivity.this, item.getSnippet(), Toast.LENGTH_LONG).show();
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return true;
                    }
                }, this);

        map.getOverlays().add(mOverlay);
        map.invalidate();
        Log.i(TAG, "Finished adding locations on map.");
    }
}