package com.mwt.recorders;

import com.mwt.explorers.MwtExplorer;

/**
 * Exposes a method to record exploration data based on generic contexts. Exploration data
 * is specified as a set of tuples <context, action, probability, key> as described below. An 
 * application passes Recorder object to the {@link MwtExplorer} constructor. See
 * {@link StringRecorder} for a sample IRecorder object.
 *
 * @param <T> The type of the context
 */
public interface Recorder<T> {
  /**
   * Records the exploration data associated with a given decision.
   * This implementation should be thread-safe if multithreading is needed.
   *
   * @param context      A user-defined context for the decision
   * @param action       The action chosen by an exploration algorithm given context
   * @param probability  The probability the exploration algorithm chose said action 
   * @param uniqueKey   A user-defined unique identifer for the decision
   */
  void record(T context, int action, float probability, String uniqueKey);
}
