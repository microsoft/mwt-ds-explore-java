package com.mwt.sample;

/**
 * Represents a tuple (context, action, probability, key)
 * @param <T> The context type
 */
class Interaction<T> {
  public T context;
  public int action;
  public float probability;
  public String uniqueKey;

  public Interaction(T context, int action, float probability, String uniqueKey) {
    this.context = context;
    this.action = action;
    this.probability = probability;
    this.uniqueKey = uniqueKey;
  }
}
