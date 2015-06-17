package com.mwt.explore;

/**
 * Represents a context interface with variable number of actions which is
 * enforced if exploration algorithm is initialized in variable number of actions mode.
 */
public interface VariableActionContext {
  int getNumberOfActions();
}
