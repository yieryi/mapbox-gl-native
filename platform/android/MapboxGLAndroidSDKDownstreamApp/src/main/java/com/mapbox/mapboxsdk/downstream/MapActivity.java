package com.mapbox.mapboxsdk.downstream;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Activity showcasing adding symbols using the annotation plugin
 */
public class MapActivity extends AppCompatActivity {

  private static final String ID_ICON = "airport";

  private MapView mapView;
  private SymbolManager symbolManager;
  private Symbol symbol;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mapView = findViewById(R.id.mapView);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
      findViewById(R.id.fabStyles).setOnClickListener(v -> mapboxMap.setStyle(Style.DARK));

      mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.846317, 5.698414), 17));

      style.addImage(ID_ICON, BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.mapbox_marker_icon_default)), false);

      // create symbol manager
      symbolManager = new SymbolManager(mapView, mapboxMap, style);
      symbolManager.setIconAllowOverlap(true);
      symbolManager.setTextAllowOverlap(true);

      // create a symbol
      SymbolOptions symbolOptions = new SymbolOptions()
        .withLatLng(new LatLng(50.846202, 5.69738))
        .withIconImage(ID_ICON)
        .withTextFont(new String[]{"Open Sans Regular","Arial Unicode MS Regular"})
        .withIconSize(1.3f)
        .withTextField("Hello World")
        .withZIndex(10);
      symbol = symbolManager.create(symbolOptions);
      Timber.e(symbol.toString());


    }));
  }

  @Override
  protected void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    if (symbolManager != null) {
      symbolManager.onDestroy();
    }

    mapView.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }
}