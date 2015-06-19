package com.mwt.sample;

import com.mwt.recorders.Recorder;

import java.util.ArrayList;
import java.util.List;

/**
 * Example of a custom recorder which implements the IRecorder<MyContext>,
 * declaring that this recorder only interacts with MyContext objects.
 */
class MyRecorder implements Recorder<MyContext> {
  private List<Interaction<MyContext>> interactions = new ArrayList<Interaction<MyContext>>();
  public void record(MyContext context, int action, float probability, String uniqueKey) {
    interactions.add(new Interaction<MyContext>(context, action, probability, uniqueKey));
  }

  public List<Interaction<MyContext>> getAllInteractions() {
    return interactions;
  }
}
