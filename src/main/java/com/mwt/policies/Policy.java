package com.mwt.policies;

/**
 * Exposes a method to choose an action given a generic context. IPolicy objects are
 * passed to (and invoked by) exploration algorithms to specify the default policy behavior.
 */
public interface Policy<T> {
  /**
   * Determines the action to take for a given context.
   * This implementation should be thread-safe if multithreading is needed.
   *
   * @param context   A user-defined context for the decision
   * @returns	        The action to take (1-based index)
   */
  int chooseAction(T context);
}
