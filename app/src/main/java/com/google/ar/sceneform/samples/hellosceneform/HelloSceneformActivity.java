/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.sceneform.samples.hellosceneform;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Light;
import com.google.ar.sceneform.rendering.Light.Type;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity {

  private static final int RC_PERMISSIONS = 0x123;
  private boolean installRequested;

  private GestureDetector gestureDetector;
//  private Snackbar loadingMessageSnackbar = null;

  private GraphSettings mGraphSettings = new GraphSettings();
  private Material mMaterial;
  private ArSceneView arSceneView;
  private ModelRenderable andyRenderable;
//  private ModelRenderable sunRenderable;
//  private ModelRenderable mercuryRenderable;
//  private ModelRenderable venusRenderable;
//  private ModelRenderable earthRenderable;
//  private ModelRenderable lunaRenderable;
//  private ModelRenderable marsRenderable;
//  private ModelRenderable jupiterRenderable;
//  private ModelRenderable saturnRenderable;
//  private ModelRenderable uranusRenderable;
//  private ModelRenderable neptuneRenderable;
//  private ViewRenderable solarControlsRenderable;

  private final GraphSettings solarSettings = new GraphSettings();

  private Float mXShiftPosition;
  // True once scene is loaded
  private boolean hasFinishedLoading = false;

  // True once the scene has been placed.
  private boolean hasPlacedSolarSystem = false;

  // Astronomical units to meters ratio. Used for positioning the planets of the solar system.
  private static final float AU_TO_METERS = 0.5f;

  @Override
  @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
  // CompletableFuture requires api level 24
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (!DemoUtils.checkIsSupportedDeviceOrFinish(this)) {
      // Not a supported device.
      return;
    }


    setContentView(R.layout.activity_ux);
    arSceneView = findViewById(R.id.ar_scene_view);
    MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.RED))
        .thenAccept(
            (Material material) -> {
              mMaterial = material;

            });
    hasFinishedLoading = true;
//    // Build all the planet models.
//    CompletableFuture<ModelRenderable> sunStage =
//        ModelRenderable.builder().setSource(this, Uri.parse("Sol.sfb")).build();
//    CompletableFuture<ModelRenderable> mercuryStage =
//        ModelRenderable.builder().setSource(this, Uri.parse("Mercury.sfb")).build();
//    CompletableFuture<ModelRenderable> venusStage =
//        ModelRenderable.builder().setSource(this, Uri.parse("Venus.sfb")).build();
//    CompletableFuture<ModelRenderable> earthStage =
//        ModelRenderable.builder().setSource(this, Uri.parse("Earth.sfb")).build();
//    CompletableFuture<ModelRenderable> lunaStage =
//        ModelRenderable.builder().setSource(this, Uri.parse("Luna.sfb")).build();
//    CompletableFuture<ModelRenderable> marsStage =
//        ModelRenderable.builder().setSource(this, Uri.parse("Mars.sfb")).build();
//    CompletableFuture<ModelRenderable> jupiterStage =
//        ModelRenderable.builder().setSource(this, Uri.parse("Jupiter.sfb")).build();
//    CompletableFuture<ModelRenderable> saturnStage =
//        ModelRenderable.builder().setSource(this, Uri.parse("Saturn.sfb")).build();
//    CompletableFuture<ModelRenderable> uranusStage =
//        ModelRenderable.builder().setSource(this, Uri.parse("Uranus.sfb")).build();
//    CompletableFuture<ModelRenderable> neptuneStage =
//        ModelRenderable.builder().setSource(this, Uri.parse("Neptune.sfb")).build();

//    // Build a renderable from a 2D View.
//    CompletableFuture<ViewRenderable> solarControlsStage =
//        ViewRenderable.builder().setView(this, R.layout.solar_controls).build();

//    CompletableFuture.allOf(
//        sunStage,
//        mercuryStage,
//        venusStage,
//        earthStage,
//        lunaStage,
//        marsStage,
//        jupiterStage,
//        saturnStage,
//        uranusStage,
//        neptuneStage,
//        solarControlsStage)
//        .handle(
//            (notUsed, throwable) -> {
//              // When you build a Renderable, Sceneform loads its resources in the background while
//              // returning a CompletableFuture. Call handle(), thenAccept(), or check isDone()
//              // before calling get().
//
//              if (throwable != null) {
//                DemoUtils.displayError(this, "Unable to load renderable", throwable);
//                return null;
//              }
//
//              try {
//                sunRenderable = sunStage.get();
//                mercuryRenderable = mercuryStage.get();
//                venusRenderable = venusStage.get();
//                earthRenderable = earthStage.get();
//                lunaRenderable = lunaStage.get();
//                marsRenderable = marsStage.get();
//                jupiterRenderable = jupiterStage.get();
//                saturnRenderable = saturnStage.get();
//                uranusRenderable = uranusStage.get();
//                neptuneRenderable = neptuneStage.get();
//                solarControlsRenderable = solarControlsStage.get();
//
//                // Everything finished loading successfully.
//                hasFinishedLoading = true;
//
//              } catch (InterruptedException | ExecutionException ex) {
//                DemoUtils.displayError(this, "Unable to load renderable", ex);
//              }
//
//              return null;
//            });

    // Set up a tap gesture detector.
    gestureDetector =
        new GestureDetector(
            this,
            new GestureDetector.SimpleOnGestureListener() {
              @Override
              public boolean onSingleTapUp(MotionEvent e) {
                onSingleTap(e);
                return true;
              }

              @Override
              public boolean onDown(MotionEvent e) {
                return true;
              }
            });

    // Set a touch listener on the Scene to listen for taps.
    arSceneView
        .getScene()
        .setOnTouchListener(
            (HitTestResult hitTestResult, MotionEvent event) -> {
              // If the solar system hasn't been placed yet, detect a tap and then check to see if
              // the tap occurred on an ARCore plane to place the solar system.
              if (!hasPlacedSolarSystem) {
                return gestureDetector.onTouchEvent(event);
              }

              // Otherwise return false so that the touch event can propagate to the scene.
              return false;
            });

    // Set an update listener on the Scene that will hide the loading message once a Plane is
    // detected.
    arSceneView
        .getScene()
        .addOnUpdateListener(
            frameTime -> {
//              if (loadingMessageSnackbar == null) {
//                return;
//              }

              Frame frame = arSceneView.getArFrame();
              if (frame == null) {
                return;
              }

              if (frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
                return;
              }

              for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
                if (plane.getTrackingState() == TrackingState.TRACKING) {
                  hideLoadingMessage();
                }
              }
            });

    // Lastly request CAMERA permission which is required by ARCore.
//    DemoUtils.requestCameraPermission(this, RC_PERMISSIONS);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (arSceneView == null) {
      return;
    }

    if (arSceneView.getSession() == null) {
      // If the session wasn't created yet, don't resume rendering.
      // This can happen if ARCore needs to be updated or permissions are not granted yet.
      try {
        Session session = DemoUtils.createArSession(this, installRequested);
        if (session == null) {
          installRequested = DemoUtils.hasCameraPermission(this);
          return;
        } else {
          arSceneView.setupSession(session);
        }
      } catch (UnavailableException e) {
        DemoUtils.handleSessionException(this, e);
      }
    }

    try {
      arSceneView.resume();
    } catch (CameraNotAvailableException ex) {
      DemoUtils.displayError(this, "Unable to get camera", ex);
      finish();
      return;
    }

    if (arSceneView.getSession() != null) {
      showLoadingMessage();
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (arSceneView != null) {
      arSceneView.pause();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (arSceneView != null) {
      arSceneView.destroy();
    }
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
    if (!DemoUtils.hasCameraPermission(this)) {
      if (!DemoUtils.shouldShowRequestPermissionRationale(this)) {
        // Permission denied with checking "Do not ask again".
        DemoUtils.launchPermissionSettings(this);
      } else {
        Toast.makeText(
            this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
            .show();
      }
      finish();
    }
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    if (hasFocus) {
      // Standard Android full-screen functionality.
      getWindow()
          .getDecorView()
          .setSystemUiVisibility(
              View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                  | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                  | View.SYSTEM_UI_FLAG_FULLSCREEN
                  | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
  }

  private void onSingleTap(MotionEvent tap) {
    if (!hasFinishedLoading) {
      // We can't do anything yet.
      return;
    }

    Frame frame = arSceneView.getArFrame();
    if (frame != null) {
      if (!hasPlacedSolarSystem && tryPlaceSolarSystem(tap, frame)) {
        hasPlacedSolarSystem = true;
      }
    }
  }

  private List<Float> getSpeedList() {
    List<Float> mFloatsList = new ArrayList<>();
    mFloatsList.add(1f);
    mFloatsList.add(10f);
    mFloatsList.add(15f);
    mFloatsList.add(29f);
    mFloatsList.add(40f);
    mFloatsList.add(35f);
    mFloatsList.add(1f);

    return mFloatsList;
  }

  private boolean tryPlaceSolarSystem(MotionEvent tap, Frame frame) {
    if (tap != null && frame.getCamera().getTrackingState() == TrackingState.TRACKING) {
      for (HitResult hit : frame.hitTest(tap)) {
        Trackable trackable = hit.getTrackable();
        if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
          // Create the Anchor.
          Anchor anchor = hit.createAnchor();
          AnchorNode anchorNode = new AnchorNode(anchor);
          anchorNode.setParent(arSceneView.getScene());
          Node solarSystem = createSolarSystem();
          Float shiftInX=-(getXShiftPosition()/2);
          solarSystem.setLocalPosition(new Vector3(0f, 0f, 0f));
//          solarSystem.setWorldPosition(new Vector3(0f, 0f, 0f));
          solarSystem.setLocalScale(new Vector3(0.1f, 0.1f, 0.1f));
//          solarSystem.setLocalRotation(Quaternion.axisAngle(new Vector3(0, 0, 1f), 180));
          anchorNode.addChild(solarSystem);
          return true;
        }
      }
    }

    return false;
  }


  private Float getXShiftPosition(int index) {
    return (index * mGraphSettings.getGraphWidth()) + mGraphSettings.getIndexGap();
  }

  private ModelRenderable getRenderable(Float height) {
    ModelRenderable andyRenderable = ShapeFactory
        .makeCylinder(0.01f, height, new Vector3(0.0f, 0.00f, 0.0f), mMaterial);
    return andyRenderable;
  }

  private Float getBarHeight(Float value) {
    return value * mGraphSettings.getGraphHeight();
  }

  private Node createSolarSystem() {
    Node sun = new Node();

    for (int index = 0; index < getSpeedList().size(); index++) {
      setXShiftPosition(getXShiftPosition(index));
      createPlanet("Mercury" + index, sun, getBarHeight(getSpeedList().get(index)),
          getXShiftPosition());
    }

    return sun;
  }

  private Node createPlanet(
      String name,
      Node parent,
      Float mHeight,
      Float mPreviousXPosition
  ) {
    // Orbit is a rotating node with no renderable positioned at the sun.
    // The planet is positioned relative to the orbit so that it appears to rotate around the sun.
    // This is done instead of making the sun rotate so each planet can orbit at its own speed.
//    RotatingNode orbit = new RotatingNode(solarSettings, true);
//    orbit.setDegreesPerSecond(orbitDegreesPerSecond);
//    orbit.setParent(parent);

    // public Planet(Context context, String planetName, float planetScale, Material mMaterial, Float mHeight, Float mPreviousXPosition, GraphSettings solarSettings) {
    // 
    // Create the planet and position it relative to the sun.
    Planet planet = new Planet(this, name, mMaterial, mHeight, mPreviousXPosition,
        solarSettings);
    planet.setParent(parent);
    planet.setLocalPosition(new Vector3(0.0f, 0.5f, 0.0f));

    return planet;
  }

  public Float getXShiftPosition() {
    return mXShiftPosition;
  }

  public void setXShiftPosition(Float XShiftPosition) {
    mXShiftPosition = XShiftPosition;
  }

  private void showLoadingMessage() {
//    if (loadingMessageSnackbar != null && loadingMessageSnackbar.isShownOrQueued()) {
//      return;
//    }
//
//    loadingMessageSnackbar =
//        Snackbar.make(
//            SolarActivity.this.findViewById(android.R.id.content),
//            R.string.plane_finding,
//            Snackbar.LENGTH_INDEFINITE);
//    loadingMessageSnackbar.getView().setBackgroundColor(0xbf323232);
//    loadingMessageSnackbar.show();
  }

  private void hideLoadingMessage() {
//    if (loadingMessageSnackbar == null) {
//      return;
//    }
//
//    loadingMessageSnackbar.dismiss();
//    loadingMessageSnackbar = null;
  }
}

///*
///*
// * Copyright 2018 Google LLC. All Rights Reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.google.ar.sceneform.samples.hellosceneform;
//
//    import android.animation.ObjectAnimator;
//    import android.animation.ValueAnimator;
//    import android.app.Activity;
//    import android.app.ActivityManager;
//    import android.content.Context;
//    import android.net.Uri;
//    import android.os.Build;
//    import android.os.Build.VERSION_CODES;
//    import android.os.Bundle;
//    import android.support.v7.app.AppCompatActivity;
//    import android.util.Log;
//    import android.view.Gravity;
//    import android.view.MotionEvent;
//    import android.widget.Toast;
//    import com.google.ar.core.Anchor;
//    import com.google.ar.core.HitResult;
//    import com.google.ar.core.Plane;
//    import com.google.ar.sceneform.AnchorNode;
//    import com.google.ar.sceneform.Node;
//    import com.google.ar.sceneform.math.Vector3;
//    import com.google.ar.sceneform.rendering.Color;
//    import com.google.ar.sceneform.rendering.Light;
//    import com.google.ar.sceneform.rendering.Light.Type;
//    import com.google.ar.sceneform.rendering.Material;
//    import com.google.ar.sceneform.rendering.MaterialFactory;
//    import com.google.ar.sceneform.rendering.ModelRenderable;
//    import com.google.ar.sceneform.rendering.Renderable;
//    import com.google.ar.sceneform.rendering.ShapeFactory;
//    import com.google.ar.sceneform.ux.ArFragment;
//    import com.google.ar.sceneform.ux.TransformableNode;
//    import java.util.ArrayList;
//    import java.util.List;
//    import java.util.concurrent.CompletableFuture;
//    import java.util.concurrent.atomic.AtomicBoolean;
//
///**
// * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
// */
//public class HelloSceneformActivity extends AppCompatActivity {
//
//  private static final String TAG = HelloSceneformActivity.class.getSimpleName();
//  private static final double MIN_OPENGL_VERSION = 3.0;
//  final int durationInMilliseconds = 1000;
//  final float minimumIntensity = 1000.0f;
//  final float maximumIntensity = 3000.0f;
//  private ArFragment arFragment;
//  private ModelRenderable andyRenderable;
//  private ModelRenderable sandyRenderable;
//
//  private AtomicBoolean mAtomicBoolean=new AtomicBoolean(false);
//  private GraphSettings mGraphSettings = new GraphSettings();
//  /// Chamfer radius to use for the bars.
//  public Float chamferRadius = 0.0f;
//
//  /// Gap between series, expressed as a ratio of gap to bar width (Z-axis).
//  public Float seriesGap = 0.5f;
//
//  /// Gap between indices, expressed as a ratio of gap to bar length (X-axis).
//  public Float indexGap = 0.5f;
//
//  /// Space to allow for the series labels, expressed as a ratio of label space to graph width (Z-axis).
//  public Float spaceForSeriesLabels = 0.2f;
//
//  /// Space to allow for the index labels, expressed as a ratio of label space to graph length (X-axis).
//  public Float spaceForIndexLabels = 0.2f;
//
//  /// Opacity of each bar in the graph.
//  public Float barOpacity = 1.0f;
//
//
//  private List<Float> getSpeedList() {
//    List<Float> mFloatsList = new ArrayList<>();
//    mFloatsList.add(1f);
//    mFloatsList.add(10f);
//    mFloatsList.add(15f);
//    mFloatsList.add(29f);
//    mFloatsList.add(40f);
//    mFloatsList.add(35f);
//    return mFloatsList;
//  }
//
//  Vector3 size = new Vector3(1.0f, 0.0f, 0.0f);
//  private Material mMaterial;
//
//  @Override
//  @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
//  // CompletableFuture requires api level 24
//  // FutureReturnValueIgnored is not valid
//  protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//
//    if (!checkIsSupportedDeviceOrFinish(this)) {
//      return;
//    }
//
//    setContentView(R.layout.activity_ux);
//    arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
//
////    CompletableFuture<ModelRenderable> sunStage =
////        ModelRenderable.builder().setSource(this, Uri.parse("Sol.sfb")).build();
//
//    MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.RED))
//        .thenAccept(
//            (Material material) -> {
//              mMaterial = material;
////              sandyRenderable = ShapeFactory
////                  .makeCylinder(0.02f, 0.2f, new Vector3(0.0f, 0.00f, 0.0f), mMaterial);
//
//            });
////    Node base=new Node();
////    for(int i=0;i<1;i++){
////      base.setParent(arFragment);
////    }
//
//    // When you build a Renderable, Sceneform loads its resources in the background while returning
//    // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
////    ModelRenderable.builder()
////        .setSource(this, R.raw.andy)
////        .build()
////        .thenAccept(renderable -> andyRenderable = renderable)
////        .exceptionally(
////            throwable -> {
////              Toast toast =
////                  Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
////              toast.setGravity(Gravity.CENTER, 0, 0);
////              toast.show();
////              return null;
////            });
//
//    arFragment.setOnTapArPlaneListener(
//        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
////          if (andyRenderable == null) {
////            return;
////          }
//
////          // Create the Anchor.
////          Anchor anchor = hitResult.createAnchor();
////          AnchorNode anchorNode = new AnchorNode(anchor);
////          anchorNode.setParent(arFragment.getArSceneView().getScene());
//
//          Anchor anchor = hitResult.createAnchor();
//          AnchorNode anchorNode = new AnchorNode(anchor);
//          anchorNode.setParent(arFragment.getArSceneView().getScene());
//
//          if(!mAtomicBoolean.get()){
//            mAtomicBoolean.set(true);
//            Node solarSystem = init();
//            anchorNode.addChild(solarSystem);
//          }
//
//        });
//
////    Light spotLightYellow =
////        Light.builder(getApplicationContext(), Light.Type.FOCUSED_SPOTLIGHT)
////            .setColor(new Color(android.graphics.Color.YELLOW))
////            .setShadowCastingEnabled(true)
////            .build();
//
////    ValueAnimator intensityAnimator =
////        ObjectAnimator.ofFloat(
////            spot.getLight(), "intensity", minimumIntensity, maximumIntensity);
////    intensityAnimator.setDuration(durationInMilliseconds);
////    intensityAnimator.setRepeatCount(ValueAnimator.INFINITE);
////    intensityAnimator.setRepeatMode(ValueAnimator.REVERSE);
////    intensityAnimator.start();
//  }
//
//  private Node init() {
//    Float previousXPosition = 0.0f;
//
//    Node mParentNode = new Node();
//
//    for (int index = 0; index < getSpeedList().size(); index++) {
//
//      createNode(mParentNode, "d", getXShiftPosition(index),
//          getBarHeight(getSpeedList().get(index)));
//    }
//
//    return mParentNode;
//  }
//
//
//  private Float getBarHeight(Float value) {
//    return value * mGraphSettings.getGraphHeight();
//  }
//
//  private Node initGraph() {
//
////    float spaceForSeriesLabels = 0f, spaceForIndexLabels = 0f;
//    Vector3 sizeAvailableForBars = new Vector3(
//        size.x * (float) (1.0 - mGraphSettings.getSpaceForSeriesLabels()),
//        size.y, size.z * (float) (1.0 - mGraphSettings.getSpaceForIndexLabels()));
//
//    Float maxValue = 10f, minValue = 0f;
//    Float biggestValueRange = maxValue - minValue;
//
//    Float barLength = seriesSize(1, sizeAvailableForBars.z);
//    Float barWidth = indexSize(1, sizeAvailableForBars.x);
//    Float maxBarHeight = sizeAvailableForBars.y / (Float) biggestValueRange;
//
//    Float xShift = size.x * (spaceForSeriesLabels - 0.5f);
//    Float zShift = size.z * (spaceForIndexLabels - 0.5f);
//    Float previousZPosition = 0.0f;
//
//    Float yPosition = 0.5f * (Float) 1f * (Float) maxBarHeight;
//    Node mNode1 = new Node();
////    mNode1.setParent(base);
//    mNode1.setLocalPosition(new Vector3(0.0f, yPosition, 0.0f));
//    mNode1.setRenderable(sandyRenderable);
//
//    return mNode1;
////    List<Float> mFloats = new ArrayList<>();
//
//    //  for (Float value : mFloats) {
////      Float barHeight = (Float) value * maxBarHeight;
//
////      et barBox = SCNBox(width: CGFloat(barWidth),
////          height: CGFloat(startingBarHeight),
////          length: CGFloat(barLength),
////          chamferRadius: CGFloat(barChamferRadius))
////
////      let barNode = ARBarChartBar(geometry: barBox,
////          index: index,
////          series: series,
////          value: value,
////          finalHeight: barHeight,
////          finalOpacity: barOpacity)
////      barNode.opacity = CGFloat(startingBarOpacity)
////
////      let yPosition = 0.5 * Float(value) * Float(maxBarHeight)
////      let startingYPosition = (animationType == .grow || animationType == .progressiveGrow) ? 0.0 : yPosition
////      let xPosition = self.xPosition(forIndex: index, previousXPosition, barWidth)
////      barNode.position = SCNVector3(x: xPosition + xShift, y: startingYPosition, z: zPosition + zShift)
//
////      let barMaterial = delegate.barChart(self, materialForBarAtIndex: index, forSeries: series)
////      barNode.geometry?.firstMaterial = barMaterial
////
////      self.addChildNode(barNode)
////
////      if series == 0 {
////        self.addLabel(forIndex: index, atXPosition: xPosition + xShift, withMaxHeight: barWidth)
////      }
////      previousXPosition = xPosition
//
////    }
//
//  }
//
//
//  /**
//   * Calculates the actual size available for one series on the graph.
//   * - parameter numberOfSeries: The number of series on the graph.
//   * - parameter availableZSize: The available size on the Z axis of the graph.
//   * - returns: The actual size available for one series on the graph.
//   */
//  private Float seriesSize(Integer numberOfSeries, Float availableZSize) {
//    Float totalGapCoefficient = 0.0f;
//
//    totalGapCoefficient = 1 + seriesGap;
//    return availableZSize / (Float) (numberOfSeries + totalGapCoefficient);
//  }
//
//  /**
//   * Calculates the actual size available for one index on the graph.
//   * - parameter numberOfIndices: The number of indices on the graph.
//   * - parameter availableXSize: The available size on the X axis of the graph.
//   * - returns: The actual size available for one index on the graph.
//   */
//
//  private Float indexSize(Integer numberOfIndices, Float availableXSize) {
//    Float totalGapCoefficient = 0.0f;
//
//    totalGapCoefficient = 1 + indexGap;
//    return availableXSize / (Float) (numberOfIndices + totalGapCoefficient);
//  }
//
////  private ModelRenderable getRenderable() {
////    sandyRenderable = ShapeFactory
////        .makeCylinder(0.02f, 0.2f, new Vector3(0.0f, 0.00f, 0.0f), mMaterial);
////    return sandyRenderable;
////  }
////
////
////  private Node getNode(Node base, int i) {
////    Node mNode1 = new Node();
////    mNode1.setParent(base);
////
////    mNode1.setLocalPosition(new Vector3(getX(i), 0.5f, 0.0f));
////    mNode1.setRenderable(getRenderable());
////
////    return mNode1;
////  }
//
//
//  private Node createNode(Node parentNode, String name,
//      Float mXShiftDirection, Float height) {
//    Node mNode = new Node();
//    mNode.setParent(parentNode);
//    mNode.setRenderable(getRenderable(height));
//    mNode.setName(name);
//    mNode.setLocalPosition(new Vector3(mXShiftDirection, 0f, 0f));
//    return mNode;
//  }
//
//  private Float getXShiftPosition(int index) {
//    return (index * mGraphSettings.getGraphWidth()) + mGraphSettings.getIndexGap();
//  }
//
//  private ModelRenderable getRenderable(Float height) {
//    sandyRenderable = ShapeFactory
//        .makeCylinder(0.01f, height, new Vector3(0.0f, 0.00f, 0.0f), mMaterial);
//    return sandyRenderable;
//  }
//
//
//  /**
//   * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
//   * on this device.
//   *
//   * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
//   *
//   * <p>Finishes the activity if Sceneform can not run
//   */
//  public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
//    if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
//      Log.e(TAG, "Sceneform requires Android N or later");
//      Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
//      activity.finish();
//      return false;
//    }
//    String openGlVersionString =
//        ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
//            .getDeviceConfigurationInfo()
//            .getGlEsVersion();
//    if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
//      Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
//      Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
//          .show();
//      activity.finish();
//      return false;
//    }
//    return true;
//  }
//}

