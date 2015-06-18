package com.mwt.explorers;

import com.mwt.contexts.VariableActionContext;
import com.mwt.policies.Policy;

/**
 * The epsilon greedy exploration algorithm. This is a good choice if you have no idea
 * which actions should be preferred.  Epsilon greedy is also computationally cheap.
 */
public class VariableActionEpsilonGreedyExplorer<T extends VariableActionContext> extends EpsilonGreedyExplorer<T> {
  /**
   * The constructor
   *
   * @param defaultPolicy   A default function which outputs an action given a context.
   * @param epsilon         The probability of a random exploration.
   *
   */
  public VariableActionEpsilonGreedyExplorer(Policy<T> defaultPolicy, float epsilon) {
    super(defaultPolicy, epsilon, Integer.MAX_VALUE);
  }

  @Override
  protected int getNumActions(T context) {
    return context.getNumberOfActions();
  }
}
