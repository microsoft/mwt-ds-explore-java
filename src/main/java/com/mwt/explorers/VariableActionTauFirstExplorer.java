package com.mwt.explorers;

import com.mwt.contexts.VariableActionContext;
import com.mwt.policies.Policy;

/**
 * The tau-first explorer collects exactly tau uniform random exploration events, and then
 * uses the default policy thereafter.
 */
public class VariableActionTauFirstExplorer<T extends VariableActionContext> extends TauFirstExplorer<T> {
  /**
   * The constructor
   *
   * @param defaultPolicy   A default policy after randomization finishes.
   * @param tau             The number of events to be uniform over.
   */
  public VariableActionTauFirstExplorer(Policy<T> defaultPolicy, int tau) {
    super(defaultPolicy, tau, Integer.MAX_VALUE);
  }

  @Override
  protected int getNumActions(T context) {
    return context.getNumberOfActions();
  }
}
