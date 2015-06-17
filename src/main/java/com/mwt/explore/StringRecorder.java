package com.mwt.explore;

/**
 * A sample recorder class that converts the exploration tuple into string format.
 */
public class StringRecorder<T> implements Recorder<T> {
  private StringBuilder recording = new StringBuilder();

  public void record(T context, int action, float probability, String uniqueKey) {
    recording.append(action);
    recording.append(" ");
    recording.append(uniqueKey);
    recording.append(" ");
    recording.append(String.format("%.5f", probability));
    recording.append(" | ");
    recording.append(context.toString());
    recording.append("\n");
  }

  public String getRecording() {
    return getRecording(true);
  }

  public String getRecording(boolean flush) {
    if (!flush) {
      return recording.toString();
    }

    String out = recording.toString();
    recording = new StringBuilder();
    return out;
  }
}
