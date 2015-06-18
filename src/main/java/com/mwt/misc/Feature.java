package com.mwt.misc;

/**
 * Represents a feature in a sparse array.
 */
final public class Feature {
  private float value;
  private int id;

  public Feature(float value, int id) {
    this.value = value;
    this.id = id;
  }

  public float getValue() {
    return value;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;

    Feature feature = (Feature) o;

    return id == feature.id;
  }

  @Override
  public int hashCode() {
    return id;
  }
}
