package com.mwt.explore;

import java.util.ArrayList;

/**
 * A sample context class that stores a vector of Features.
 */
public class SimpleContext {
  private ArrayList<Feature> features;

  public SimpleContext(ArrayList<Feature> features) {
    this.features = features;
  }

  public ArrayList<Feature> getFeatures() {
    return features;
  }

  @Override
  public String toString() {
    String out = "";
    for (Feature feature: features) {
      out = out + feature.getId() + ": " + feature.getValue() + " ";
    }
    return out;
  }
}
