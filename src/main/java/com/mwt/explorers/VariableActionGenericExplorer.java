package com.mwt.explorers;

import com.mwt.contexts.VariableActionContext;
import com.mwt.scorers.Scorer;

/**
 * GenericExplorer provides complete flexibility.  You can create any
 * distribution over actions desired, and it will draw from that.
 */
public class VariableActionGenericExplorer<T extends VariableActionContext> extends GenericExplorer<T> {
  /**
   * Constructor
   *
   * @param defaultScorer  A function which outputs the probability of each action.
   */
  public VariableActionGenericExplorer(Scorer<T> defaultScorer) {
    super(defaultScorer, Integer.MAX_VALUE);
  }

  @Override
  protected int getNumActions(T context) {
    return context.getNumberOfActions();
  }
}
