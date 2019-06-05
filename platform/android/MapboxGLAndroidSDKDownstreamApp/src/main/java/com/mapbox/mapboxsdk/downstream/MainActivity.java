package com.mapbox.mapboxsdk.downstream;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.google.gson.JsonObject;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

  private final static String TAG = "Main";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_donwload);

    Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

    OfflineManager offlineManager = OfflineManager.getInstance(this);

    LatLngBounds latLngBounds = new LatLngBounds.Builder()
      .include(new LatLng(50.849772, 5.688173))
      .include(new LatLng(50.843630, 5.710903))
      .build();

    float pixelRatio = getResources().getDisplayMetrics().density;

    OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
      Style.MAPBOX_STREETS, latLngBounds, 16, 20, pixelRatio
    );

    offlineManager.createOfflineRegion(definition, convertRegionName("test"), new OfflineManager.CreateOfflineRegionCallback() {
      @Override
      public void onCreate(OfflineRegion offlineRegion) {
        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
        offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
          @Override
          public void onStatusChanged(OfflineRegionStatus status) {
            if (status.isComplete()) {
              View button = findViewById(R.id.button);
              button.setVisibility(View.VISIBLE);
              button.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), MapActivity.class);
                startActivity(intent);
              });
            }
          }

          @Override
          public void onError(OfflineRegionError error) {
            Logger.e(TAG, error.getMessage());
          }

          @Override
          public void mapboxTileCountLimitExceeded(long limit) {

          }
        });
      }

      @Override
      public void onError(String error) {
        Logger.e(TAG, error);
      }
    });
  }

  public static final String JSON_CHARSET = "UTF-8";
  public static final String JSON_FIELD_REGION_NAME = "FIELD_REGION_NAME";

  public static byte[] convertRegionName(String regionName) {
    try {
      JsonObject jsonObject = new JsonObject();
      jsonObject.addProperty(JSON_FIELD_REGION_NAME, regionName);
      return jsonObject.toString().getBytes(JSON_CHARSET);
    } catch (Exception exception) {
      Timber.e(exception, "Failed to encode metadata: ");
    }
    return null;
  }
}