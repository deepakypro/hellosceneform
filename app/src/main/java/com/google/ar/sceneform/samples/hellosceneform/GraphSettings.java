package com.google.ar.sceneform.samples.hellosceneform;

public class GraphSettings {

  /// Chamfer radius to use for the bars.
  public Float chamferRadius = 0.0f;

  /// Gap between series, expressed as a ratio of gap to bar width (Z-axis).
  public Float seriesGap = 0.5f;

  /// Gap between indices, expressed as a ratio of gap to bar length (X-axis).
  public Float indexGap = 0.1f;

  /// Gap between indices, expressed as a ratio of gap to bar length (X-axis).
  public Float graphHeight = 0.1f;

  /// Gap between indices, expressed as a ratio of gap to bar length (X-axis).
  public Float graphWidth = 0.3f;


  /// Space to allow for the series labels, expressed as a ratio of label space to graph width (Z-axis).
  public Float spaceForSeriesLabels = 0.2f;

  /// Space to allow for the index labels, expressed as a ratio of label space to graph length (X-axis).
  public Float spaceForIndexLabels = 0.2f;

  /// Opacity of each bar in the graph.
  public Float barOpacity = 1.0f;


  public Float getGraphWidth() {
    return graphWidth;
  }

  public void setGraphWidth(Float graphWidth) {
    this.graphWidth = graphWidth;
  }

  public Float getGraphHeight() {
    return graphHeight;
  }

  public void setGraphHeight(Float graphHeight) {
    this.graphHeight = graphHeight;
  }

  public void setChamferRadius(Float chamferRadius) {
    this.chamferRadius = chamferRadius;
  }

  public void setSeriesGap(Float seriesGap) {
    this.seriesGap = seriesGap;
  }

  public void setIndexGap(Float indexGap) {
    this.indexGap = indexGap;
  }

  public void setSpaceForSeriesLabels(Float spaceForSeriesLabels) {
    this.spaceForSeriesLabels = spaceForSeriesLabels;
  }

  public void setSpaceForIndexLabels(Float spaceForIndexLabels) {
    this.spaceForIndexLabels = spaceForIndexLabels;
  }

  public void setBarOpacity(Float barOpacity) {
    this.barOpacity = barOpacity;
  }

  public Float getChamferRadius() {
    return chamferRadius;
  }

  public Float getSeriesGap() {
    return seriesGap;
  }

  public Float getIndexGap() {
    return indexGap;
  }

  public Float getSpaceForSeriesLabels() {
    return spaceForSeriesLabels;
  }

  public Float getSpaceForIndexLabels() {
    return spaceForIndexLabels;
  }

  public Float getBarOpacity() {
    return barOpacity;
  }
}
