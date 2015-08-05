package com.mwt.explorers;

import com.mwt.consumers.ConsumePolicy;
import com.mwt.misc.DecisionTuple;
import com.mwt.policies.Policy;
import com.mwt.utilities.PRG;

import java.util.Random;

/**
 * The tau-first explorer collects exactly tau uniform random exploration events, and then
 * uses the default policy thereafter.
 */
public class TauFirstExplorer<T> implements Explorer<T>, ConsumePolicy<T> {
  private Policy<T> defaultPolicy;
  private int tau;
  private boolean explore = true;
  private int numActions;

  /**
   * The constructor
   *
   * @param defaultPolicy   A default policy after randomization finishes.
   * @param tau             The number of events to be uniform over.
   * @param numActions      The number of actions to randomize over.
   */
  public TauFirstExplorer(Policy<T> defaultPolicy, int tau, int numActions) {
    if (numActions < 1) {
      throw new IllegalArgumentException("Number of actions must be at least 1.");
    }
    if (tau < 0) {
      throw new IllegalArgumentException("Tau must be non-negative.");
    }
    
    this.defaultPolicy = defaultPolicy;
    this.tau = tau;
    this.numActions = numActions;
  }

  protected int getNumActions(T context) {
    return numActions;
  }

  public void updatePolicy(Policy<T> newPolicy) {
    this.defaultPolicy = newPolicy;
  }

  public DecisionTuple chooseAction(long saltedSeed, T context) {
    int numActionsForContext = getNumActions(context);
    PRG random = new PRG(saltedSeed);

    int chosenAction = 0;
    float actionProbability = 0.0f;
    boolean logAction;

    if ((tau > 0) && explore) {
      tau--;
      chosenAction = random.uniformInt(1, numActionsForContext); // Add 1 because actions are 1-indexed
      actionProbability = 1.f / numActionsForContext;
      logAction = true;
    } else {
      // Invoke the default policy function to get the action
      chosenAction = defaultPolicy.chooseAction(context);

      if (chosenAction == 0 || chosenAction > numActionsForContext) {
        throw new RuntimeException("Action chosen by default policy is not within valid range.");
      }

      actionProbability = 1.f;
      logAction = false;
    }

    return new DecisionTuple(chosenAction, actionProbability, logAction);
  }

  public void enableExplore(boolean explore) {
    this.explore = explore;
  }
}
