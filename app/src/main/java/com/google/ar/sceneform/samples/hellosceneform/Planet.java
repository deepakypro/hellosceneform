/*
 * Copyright 2018 Google LLC
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

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import java.util.Vector;


/**
 * Node that represents a planet.
 *
 * <p>The planet creates two child nodes when it is activated:
 *
 * <ul>
 * <li>The visual of the planet, rotates along it's own axis and renders the planet.
 * <li>An info card, renders an Android View that displays the name of the planerendt. This can be
 * toggled on and off.
 * </ul>
 *
 * The planet is rendered by a child instead of this node so that the spinning of the planet doesn't
 * make the info card spin as well.
 */
public class Planet extends Node implements Node.OnTapListener {

  private final String planetName;

  private final GraphSettings mGraphSettings = new GraphSettings();
  //  private final ModelRenderable planetRenderable;
  private final GraphSettings solarSettings;

  private Node infoCard;
  //  private Node speedCard;
  private Float mXPreviousPosition;
  private Float mBarHeight;
  private Node planetVisual;
  private final Context context;
  private Material mMaterial;
  //  private ArFragment arFragment;
  private boolean isMaxSpeed = false;
  private Float mBarWidth;
  private ModelRenderable mArrowRenderable;

  public Planet(Context context, String planetName, Material mMaterial,
      Float mBarHeight, Float mXPreviousPosition, GraphSettings solarSettings,
      boolean isMaxSpeed, Float mBarWidth, ModelRenderable mArrowRenderable) {
    this.context = context;
    this.planetName = planetName;
    this.mMaterial = mMaterial;
    this.mBarHeight = mBarHeight;
    this.mXPreviousPosition = mXPreviousPosition;
    this.solarSettings = solarSettings;
    this.isMaxSpeed = isMaxSpeed;
    this.mBarWidth = mBarWidth;
    this.mArrowRenderable = mArrowRenderable;
    setOnTapListener(this);
  }

  @Override
  @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
  public void onActivate() {

    if (getScene() == null) {
      throw new IllegalStateException("Scene is null!");
    }

    if (isMaxSpeed) {
      if (infoCard == null) {
        infoCard = new Node();
        infoCard.setParent(this);
        infoCard.setEnabled(true);
        infoCard.setLocalScale(new Vector3(0.1f, 0.1f, 0.1f));
        infoCard.setRenderable(mArrowRenderable);
        infoCard.setLocalPosition(new Vector3(mXPreviousPosition, (mBarHeight), 0.0f));

        ViewRenderable.builder()
            .setView(context, R.layout.planet_card_view)
            .build()
            .thenAccept(
                (renderable) -> {
                  infoCard.setRenderable(renderable);
                  TextView textView = (TextView) renderable.getView();
                  textView.setText(planetName);
                })
            .exceptionally(
                (throwable) -> {
                  throw new AssertionError("Could not load plane card view.", throwable);
                });
      }

    }

    if (planetVisual == null) {
      planetVisual = new Node();
      planetVisual.setParent(this);
      planetVisual.setRenderable(getPlanetRenderable(mMaterial, mBarHeight));
      planetVisual.setLocalPosition(new Vector3(mXPreviousPosition, 0.0f, 0.0f));
//      planetVisual.setLocalScale(new Vector3(planetScale, planetScale, planetScale));
    }
  }

  private ModelRenderable getPlanetRenderable(Material mMaterial, Float height) {

    Float mYAboveGround = (height / 2);
//    Log.d("Distance 1", mGraphSettings.getCubeWidth() + " ");
    return ShapeFactory
        .makeCube(
            new Vector3(mBarWidth, mBarHeight, mGraphSettings.getCubeLength()),
            new Vector3(0.0f, mYAboveGround, 0.0f), mMaterial);

  }

  @Override
  public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {
    if (infoCard == null) {
      return;
    }

    infoCard.setEnabled(!infoCard.isEnabled());
  }

  @Override
  public void onUpdate(FrameTime frameTime) {
    if (infoCard == null) {
      return;
    }

    // Typically, getScene() will never return null because onUpdate() is only called when the node
    // is in the scene.
    // However, if onUpdate is called explicitly or if the node is removed from the scene on a
    // different thread during onUpdate, then getScene may be null.
    if (getScene() == null) {
      return;
    }
    Vector3 cameraPosition = getScene().getCamera().getWorldPosition();
    Vector3 cardPosition = infoCard.getWorldPosition();
    Vector3 direction = Vector3.subtract(cameraPosition, cardPosition);
    Quaternion lookRotation = Quaternion.lookRotation(direction, Vector3.up());
    infoCard.setWorldRotation(lookRotation);

  }
}
