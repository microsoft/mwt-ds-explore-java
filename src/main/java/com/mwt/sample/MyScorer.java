package com.mwt.sample;

import com.mwt.scorers.Scorer;

import java.util.ArrayList;

/**
 * Example of a custom scorer which implements the IScorer<MyContext>,
 * declaring that this scorer only interacts with MyContext objects.
 */
class MyScorer implements Scorer<MyContext> {
  private int numActions;

  public MyScorer(int numActions) {
    this.numActions = numActions;
  }

  public ArrayList<Float> scoreActions(MyContext context) {
    ArrayList<Float> actions = new ArrayList<Float>();
    for (int i = 0; i < numActions; i++) {
      actions.add(1.0f / numActions);
    }
    return actions;
  }
}
