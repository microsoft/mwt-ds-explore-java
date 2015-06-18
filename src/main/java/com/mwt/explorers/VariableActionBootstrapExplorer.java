package com.mwt.explorers;

import com.mwt.contexts.VariableActionContext;
import com.mwt.policies.Policy;

import java.util.List;

/**
 * The Bootstrap explorer randomizes over the actions chosen by a set of default policies. 
 * This performs well statistically but can be computationally expensive.
 * Should be used with the MwtExplorer.
 */
public class VariableActionBootstrapExplorer<T extends VariableActionContext> extends BootstrapExplorer<T> {
  /**
   * @param policies  A set of default policies to be uniform random over.
   * The policy pointers must be valid throughout the lifetime of this explorer.
   */
  public VariableActionBootstrapExplorer(List<Policy<T>> policies) {
    super(policies, Integer.MAX_VALUE);
  }

  @Override
  protected int getNumActions(T context) {
    return context.getNumberOfActions();
  }
}
