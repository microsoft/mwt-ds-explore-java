package com.mwt.misc;

/**
 * A ChosenAction consists of an action to take,
 * the probability it was chosen,
 * and a flag indicating whether to record this decision
 */
public class DecisionTuple {
  private int action;
  private float probability;
  private boolean record;

  public DecisionTuple(int action, float probability, boolean record) {
    this.action = action;
    this.probability = probability;
    this.record = record;
  }

  public int getAction() {
    return action;
  }

  public float getProbability() {
    return probability;
  }

  public boolean shouldRecord() {
    return record;
  }
}
