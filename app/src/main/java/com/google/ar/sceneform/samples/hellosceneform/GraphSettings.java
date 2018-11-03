package com.google.ar.sceneform.samples.hellosceneform;

import android.support.annotation.NonNull;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.ModelRenderable;
import java.util.ArrayList;
import java.util.List;

public class GraphSettings {

  private List<Float> mSpeedList = new ArrayList<>();


  private Float mGraphScaleFactor = 0.1f;
  private Float mMaximumSpeed = 0.0f;
  private Float mGraphTotalLength = 2.0f;
  private Material mNormalMaterial, mMaxSpeedMaterial;
  private ModelRenderable platformRenderable,arrowRenderable;
  private Float mCubeWidth = 0.01f;

  private Float mCubeLength = 0.5f;

  private Float mSeriesGap = 0.01f;

  private Float mCubeHeight = 0.1f;

  public Float getCubeHeight() {
    return mCubeHeight;
  }

  public void setCubeHeight(Float cubeHeight) {
    mCubeHeight = cubeHeight;
  }

  public Float getCubeLength() {
    return mCubeLength;
  }

  public void setCubeLength(Float cubeLength) {
    mCubeLength = cubeLength;
  }

  public Float getSeriesGap() {
    return mSeriesGap;
  }

  public void setSeriesGap(Float seriesGap) {
    mSeriesGap = seriesGap;
  }

  public Float getCubeWidth() {
    return mCubeWidth;
  }

  public void setCubeWidth(Float cubeWidth) {
    mCubeWidth = cubeWidth;
  }

  public ModelRenderable getPlatformRenderable() {
    return platformRenderable;
  }

  public void setPlatformRenderable(ModelRenderable platformRenderable) {
    this.platformRenderable = platformRenderable;
  }

  public Material getNormalMaterial() {
    return mNormalMaterial;
  }

  public void setNormalMaterial(Material normalMaterial) {
    mNormalMaterial = normalMaterial;
  }

  public Material getMaxSpeedMaterial() {
    return mMaxSpeedMaterial;
  }

  public void setMaxSpeedMaterial(Material maxSpeedMaterial) {
    mMaxSpeedMaterial = maxSpeedMaterial;
  }

  public Float getGraphTotalLength() {
    return mGraphTotalLength;
  }

  public void setGraphTotalLength(Float graphTotalLength) {
    mGraphTotalLength = graphTotalLength;
  }

  public Float getMaximumSpeed() {
    return mMaximumSpeed;
  }

  public void setMaximumSpeed(Float maximumSpeed) {
    mMaximumSpeed = maximumSpeed;
  }

  public List<Float> getSpeedList() {
    return mSpeedList;
  }

  public void setSpeedList(@NonNull List<Float> speedList) {
    mSpeedList = speedList;
  }

  public Float getGraphScaleFactor() {
    return mGraphScaleFactor;
  }

  public void setGraphScaleFactor(Float graphScaleFactor) {
    mGraphScaleFactor = graphScaleFactor;
  }

  public ModelRenderable getArrowRenderable() {
    return arrowRenderable;
  }

  public void setArrowRenderable(ModelRenderable arrowRenderable) {
    this.arrowRenderable = arrowRenderable;
  }
}
