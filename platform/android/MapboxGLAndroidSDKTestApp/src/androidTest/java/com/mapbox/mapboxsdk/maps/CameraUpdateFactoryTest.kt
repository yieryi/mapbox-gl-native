package com.mapbox.mapboxsdk.maps

import android.support.test.runner.AndroidJUnit4
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.testapp.activity.EspressoTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class CameraUpdateFactoryTest : EspressoTest() {

  private val countDownLatch = CountDownLatch(1)

  @Test
  fun testLatLngBoundsUntiltedUnrotated() {
    rule.activity.runOnUiThread {
      mapboxMap.cameraPosition = CameraPosition.Builder()
        .target(LatLng(60.0, 24.0))
        .bearing(0.0)
        .tilt(0.0)
        .build()

      val bounds: LatLngBounds = LatLngBounds.Builder()
        .include(LatLng(62.0, 26.0))
        .include(LatLng(58.0, 22.0))
        .build()

      mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0))

      val cameraPosition = mapboxMap.cameraPosition
      assertEquals("latitude should match:", 60.0, cameraPosition.target.latitude, 0.1)
      assertEquals("longitude should match:", 24.0, cameraPosition.target.longitude, 0.1)
      assertEquals("bearing should match:", 0.0, cameraPosition.bearing, 0.1)
      assertEquals("zoom should match", 5.7, cameraPosition.zoom, 0.1)
      assertEquals("tilt should match:", 0.0, cameraPosition.tilt, 0.1)
      countDownLatch.countDown()
    }
    if (!countDownLatch.await(30, TimeUnit.SECONDS)) {
      throw TimeoutException()
    }
  }

  @Test
  fun testLatLngBoundsTilted() {
    rule.activity.runOnUiThread {
      mapboxMap.cameraPosition = CameraPosition.Builder()
        .target(LatLng(60.0, 24.0))
        .bearing(0.0)
        .tilt(45.0)
        .build()

      val bounds: LatLngBounds = LatLngBounds.Builder()
        .include(LatLng(62.0, 26.0))
        .include(LatLng(58.0, 22.0))
        .build()

      mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0))

      val cameraPosition = mapboxMap.cameraPosition
      assertEquals("latitude should match:", 60.0, cameraPosition.target.latitude, 0.1)
      assertEquals("longitude should match:", 24.0, cameraPosition.target.longitude, 0.1)
      assertEquals("bearing should match:", 0.0, cameraPosition.bearing, 0.1)
      assertEquals("zoom should match", 6.2, cameraPosition.zoom, 0.1)
      assertEquals("tilt should match:", 45.0, cameraPosition.tilt, 0.1)
      countDownLatch.countDown()
    }
    if (!countDownLatch.await(30, TimeUnit.SECONDS)) {
      throw TimeoutException()
    }
  }

  @Test
  fun testLatLngBoundsRotated() {
    rule.activity.runOnUiThread {
      mapboxMap.cameraPosition = CameraPosition.Builder()
        .target(LatLng(60.0, 24.0))
        .bearing(30.0)
        .tilt(0.0)
        .build()

      val bounds: LatLngBounds = LatLngBounds.Builder()
        .include(LatLng(62.0, 26.0))
        .include(LatLng(58.0, 22.0))
        .build()

      mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0))

      val cameraPosition = mapboxMap.cameraPosition
      assertEquals("latitude should match:", 60.0, cameraPosition.target.latitude, 0.1)
      assertEquals("longitude should match:", 24.0, cameraPosition.target.longitude, 0.1)
      assertEquals("bearing should match:", 30.0, cameraPosition.bearing, 0.1)
      assertEquals("zoom should match", 5.3, cameraPosition.zoom, 0.1)
      assertEquals("tilt should match:", 0.0, cameraPosition.tilt, 0.1)
      countDownLatch.countDown()
    }
    if (!countDownLatch.await(30, TimeUnit.SECONDS)) {
      throw TimeoutException()
    }
  }

  @Test
  fun testLatLngBoundsTiltedRotated() {
    rule.activity.runOnUiThread {
      mapboxMap.cameraPosition = CameraPosition.Builder()
        .target(LatLng(60.0, 24.0))
        .bearing(30.0)
        .tilt(45.0)
        .build()

      val bounds: LatLngBounds = LatLngBounds.Builder()
        .include(LatLng(62.0, 26.0))
        .include(LatLng(58.0, 22.0))
        .build()

      mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0))

      val cameraPosition = mapboxMap.cameraPosition
      assertEquals("latitude should match:", 60.0, cameraPosition.target.latitude, 0.1)
      assertEquals("longitude should match:", 24.0, cameraPosition.target.longitude, 0.1)
      assertEquals("bearing should match:", 30.0, cameraPosition.bearing, 0.1)
      assertEquals("zoom should match", 5.3, cameraPosition.zoom, 0.1)
      assertEquals("tilt should match:", 45.0, cameraPosition.tilt, 0.1)
      countDownLatch.countDown()
    }
    if (!countDownLatch.await(30, TimeUnit.SECONDS)) {
      throw TimeoutException()
    }
  }

  @Test
  fun testLatLngBoundsWithProvidedTiltAndRotation() {
    rule.activity.runOnUiThread {
      mapboxMap.cameraPosition = CameraPosition.Builder()
        .target(LatLng(60.0, 24.0))
        .bearing(0.0)
        .tilt(0.0)
        .build()

      val bounds: LatLngBounds = LatLngBounds.Builder()
        .include(LatLng(62.0, 26.0))
        .include(LatLng(58.0, 22.0))
        .build()

      mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30.0, 40.0, 0))

      val cameraPosition = mapboxMap.cameraPosition
      assertEquals("latitude should match:", 60.0, cameraPosition.target.latitude, 0.1)
      assertEquals("longitude should match:", 24.0, cameraPosition.target.longitude, 0.1)
      assertEquals("bearing should match:", 30.0, cameraPosition.bearing, 0.1)
      assertEquals("zoom should match", 5.3, cameraPosition.zoom, 0.1)
      assertEquals("tilt should match:", 40.0, cameraPosition.tilt, 0.1)
      countDownLatch.countDown()
    }
    if (!countDownLatch.await(30, TimeUnit.SECONDS)) {
      throw TimeoutException()
    }
  }


}