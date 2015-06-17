package com.mwt.explore;

import java.util.ArrayList;

/**
 * Exposes a method for specifying a score (weight) for each action given a generic context.
 */
public interface Scorer<T> {
  /**
   * Determines the score of each action for a given context.
   * This implementation should be thread-safe if multithreading is needed.
   *
   * @param context   A user-defined context for the decision
   * @returns         A vector of scores indexed by action (1-based)
   */
  ArrayList<Float> scoreActions(T context);
}
