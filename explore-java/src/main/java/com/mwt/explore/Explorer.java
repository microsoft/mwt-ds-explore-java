package com.mwt.explore;

/**
 * Exposes a method to choose an action given a generic context, and obtain the relevant
 * exploration bits. Invokes {@link Policy#chooseAction)} internally. Do not implement this
 * interface yourself: instead, use the various exploration algorithms below, which 
 * implement it for you. 
 */
public interface Explorer<T> {
  /**
   * Determines the action to take and the probability with which it was chosen, for a
   * given context.
   *
   * @param saltedSeed  A PRG seed based on a unique id information provided by the user
   * @param context      A user-defined context for the decision
   * @returns            The action to take, the probability it was chosen, and a flag indicating 
   *                     whether to record this decision
   */
  ChosenAction chooseAction(long saltedSeed, T context);
  void enableExplore(boolean explore);
}
