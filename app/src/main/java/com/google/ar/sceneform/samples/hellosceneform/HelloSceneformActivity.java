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
import com.google.ar.sceneform.ux.TransformationSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity {


  private Float mShiftGraph = 0.0f;
  private Float mXPositionShift = 0.0f;
  private GraphSettings mGraphSettings = new GraphSettings();
  private ArFragment arFragment;
  private final GraphSettings graphSettings = new GraphSettings();
  private AtomicBoolean hasFinishedLoading = new AtomicBoolean(false);
  private AtomicBoolean isMaximumSpeedAlreadyPlotted = new AtomicBoolean(false);
  private AtomicBoolean mIsGraphLoaded = new AtomicBoolean(false);
  private int mListSize = 0;
  private int mDivideFactor = 20;

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
    arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment1);

    loadMaterials();

    mGraphSettings.setSpeedList(getSpeedList());
    mGraphSettings.setMaximumSpeed(Collections.max(mGraphSettings.getSpeedList()));
    setListSize(mGraphSettings.getSpeedList().size());

    if (getListSize() < 10) {
      mGraphSettings.setGraphTotalLength(1.0f);
//      mDivideFactor = 10;
    }

    if (mGraphSettings.getMaximumSpeed() > 50) {
      mGraphSettings.setCubeHeight(0.05f);
    }
    arFragment.setOnTapArPlaneListener(
        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {

          if (mIsGraphLoaded.get() || !hasFinishedLoading.get()) {
            return;
          }

          Trackable trackable = hitResult.getTrackable();
          if (trackable instanceof Plane && ((Plane) trackable)
              .isPoseInPolygon(hitResult.getHitPose())) {

            mIsGraphLoaded.set(true);
            // Create the Anchor.
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());

            TransformableNode mParentNode = new TransformableNode(
                arFragment.getTransformationSystem());
//            mParentNode.setLocalScale(new Vector3(0.05f, 0.05f, 0.05f));
            anchorNode.addChild(mParentNode);

            Node graphNode = createGraph();
            graphNode.setLocalScale(new Vector3(mGraphSettings.getGraphScaleFactor(),
                mGraphSettings.getGraphScaleFactor(), mGraphSettings.getGraphScaleFactor()));
            Float mXShiftPosition = -(getXPositionShift() / mDivideFactor);
            Log.d("asdlkjfas", mXShiftPosition + " ");
            graphNode.setLocalPosition(new Vector3(mXShiftPosition, 0.03f, 0f));

            Node mLoadPlatformNode = loadPlatform();
            mLoadPlatformNode.addChild(graphNode);

            mParentNode.addChild(mLoadPlatformNode);

          }


        });

  }

  private void loadMaterials() {
    // Build all the planet models.
    CompletableFuture<ModelRenderable> platformStage = ModelRenderable.builder()
        .setSource(this, R.raw.above).build();

    CompletableFuture<ModelRenderable> arrow = ModelRenderable.builder()
        .setSource(this, R.raw.arrow).build();
    CompletableFuture<Material> redMaterial = MaterialFactory
        .makeOpaqueWithColor(this, new Color(android.graphics.Color.RED));
    CompletableFuture<Material> blueMaterial = MaterialFactory
        .makeOpaqueWithColor(this, new Color(android.graphics.Color.BLUE));
    CompletableFuture.allOf(
        platformStage,
        redMaterial,
        blueMaterial)
        .handle(
            (notUsed, throwable) -> {

              if (throwable != null) {
                DemoUtils.displayError(this, "Unable to load renderable", throwable);
                return null;
              }

              try {
                mGraphSettings.setMaxSpeedMaterial(blueMaterial.get());
                mGraphSettings.setNormalMaterial(redMaterial.get());
                mGraphSettings.setPlatformRenderable(platformStage.get());
                mGraphSettings.setArrowRenderable(arrow.get());

                // Everything finished loading successfully.
                hasFinishedLoading.set(true);

              } catch (InterruptedException | ExecutionException ex) {
                DemoUtils.displayError(this, "Unable to load renderable", ex);
              }

              return null;
            });

  }


  private Node loadPlatform() {
    Node platformNode = new Node();
    platformNode.setRenderable(mGraphSettings.getPlatformRenderable());
    return platformNode;
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

  private List<Float> getSpeedList() {
    List<Float> mFloatsList = new ArrayList<>();
    mFloatsList.add(1f);
    mFloatsList.add(100f);
    mFloatsList.add(14f);
    mFloatsList.add(50f);

    mFloatsList.add(61f);
    mFloatsList.add(55f);
    mFloatsList.add(83f);
    mFloatsList.add(1f);
    mFloatsList.add(100f);
    mFloatsList.add(14f);
    mFloatsList.add(50f);

    mFloatsList.add(61f);
    mFloatsList.add(55f);
    mFloatsList.add(83f);
    mFloatsList.add(1f);
    mFloatsList.add(100f);
    mFloatsList.add(14f);
    mFloatsList.add(50f);

    mFloatsList.add(61f);
    mFloatsList.add(55f);
    mFloatsList.add(83f);
    mFloatsList.add(1f);
    mFloatsList.add(100f);
    mFloatsList.add(14f);
    mFloatsList.add(50f);

    mFloatsList.add(61f);
    mFloatsList.add(55f);
    mFloatsList.add(83f);
    mFloatsList.add(1f);
    mFloatsList.add(100f);
    mFloatsList.add(14f);
    mFloatsList.add(50f);

    mFloatsList.add(61f);
    mFloatsList.add(55f);
    mFloatsList.add(83f);
    mFloatsList.add(1f);
    mFloatsList.add(100f);
    mFloatsList.add(14f);
    mFloatsList.add(50f);

    mFloatsList.add(61f);
    mFloatsList.add(55f);
    mFloatsList.add(83f);
    mFloatsList.add(1f);
    mFloatsList.add(100f);
    mFloatsList.add(14f);
    mFloatsList.add(50f);

    mFloatsList.add(61f);
    mFloatsList.add(55f);
    mFloatsList.add(83f);
    mFloatsList.add(1f);
    mFloatsList.add(100f);
    mFloatsList.add(14f);
    mFloatsList.add(50f);

    mFloatsList.add(61f);
    mFloatsList.add(55f);
    mFloatsList.add(83f);

    return mFloatsList;
  }


  private Float getBarHeight(Float value) {
    return value * mGraphSettings.getCubeHeight();
  }

  private Node createGraph() {

    Node parentNode = new Node();
    Float barWidth = getBarWidth(getListSize());
    Float xShiftPosition = 0f;
    Log.d("asdlkjfas", barWidth + "");
//    caluclateShiftInGraph(barWidth, mGraphSettings.getSpeedList().size());
    for (Float value : mGraphSettings.getSpeedList()) {

      if (isMaxRun(value)) {
        createNode("Maximum speed is : " + value, parentNode, getBarHeight(value),
            mGraphSettings.getMaxSpeedMaterial(), xShiftPosition, true, barWidth);
      } else {
        createNode("Maximum speed is : " + value, parentNode, getBarHeight(value),
            mGraphSettings.getNormalMaterial(),
            xShiftPosition, false, barWidth);
      }

      xShiftPosition = xShiftPosition + (barWidth / 2) + barWidth;
      setXPositionShift(xShiftPosition);
    }
    return parentNode;
  }

  private Float getBarWidth(int size) {
    return (mGraphSettings.getGraphTotalLength() / size);
  }

  private Boolean isMaxRun(Float mSpeed) {

    if (isMaximumSpeedAlreadyPlotted.get()) {
      return false;
    }
    if (mSpeed == mGraphSettings.getMaximumSpeed()) {
      isMaximumSpeedAlreadyPlotted.set(true);
      return true;
    }
    return false;
  }


  private Node createNode(
      String name,
      Node parent,
      Float mHeight,
      Material material,
      Float mPreviousXPosition,
      Boolean isMaxRun,
      Float barWidth
  ) {

    Planet planet = new Planet(this, name, material, mHeight, mPreviousXPosition,
        graphSettings, isMaxRun, barWidth,mGraphSettings.getArrowRenderable());
    planet.setParent(parent);
    return planet;
  }


  public int getListSize() {
    return mListSize;
  }

  public void setListSize(int listSize) {
    mListSize = listSize;
  }

  public Float getXPositionShift() {
    return mXPositionShift;
  }

  public void setXPositionShift(Float XPositionShift) {
    mXPositionShift = XPositionShift;
  }
}

