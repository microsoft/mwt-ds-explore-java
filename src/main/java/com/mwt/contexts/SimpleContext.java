package com.mwt.contexts;

import com.mwt.misc.Feature;

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
    StringBuilder out = new StringBuilder();
    for (Feature feature: features) {
      out.append(feature.getId()).append(": ").append(feature.getValue()).append(" ");
    }
    return out.toString();
  }
}
