package com.mwt.explorers;

import com.mwt.contexts.VariableActionContext;
import com.mwt.scorers.Scorer;

/**
 * In some cases, different actions have a different scores, and you would prefer to
 * choose actions with large scores. Softmax allows you to do that.
 */
public class VariableActionSoftmaxExplorer<T extends VariableActionContext> extends SoftmaxExplorer<T> {
  /**
   * Constructor
   *
   * @param defaultScorer  A function which outputs a score for each action.
   * @param lambda          lambda = 0 implies uniform distribution.  Large lambda is equivalent to a max.
   */
  public VariableActionSoftmaxExplorer(Scorer<T> defaultScorer, float lambda) {
    super(defaultScorer, lambda, Integer.MAX_VALUE);
  }

  @Override
  protected int getNumActions(T context) {
    return context.getNumberOfActions();
  }
}
