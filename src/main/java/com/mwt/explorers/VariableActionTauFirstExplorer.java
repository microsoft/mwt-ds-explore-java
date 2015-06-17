package com.mwt.explorers;

import com.mwt.consumers.ConsumePolicy;
import com.mwt.contexts.VariableActionContext;
import com.mwt.misc.ChosenAction;
import com.mwt.policies.Policy;

import java.util.Random;

/**
 * The tau-first explorer collects exactly tau uniform random exploration events, and then
 * uses the default policy thereafter.
 */
public class VariableActionTauFirstExplorer<T extends VariableActionContext> implements Explorer<T>, ConsumePolicy<T> {
  private Policy<T> defaultPolicy;
  private int tau;
  private boolean explore = true;

  /**
   * The constructor
   *
   * @param defaultPolicy   A default policy after randomization finishes.
   * @param tau             The number of events to be uniform over.
   */
  public VariableActionTauFirstExplorer(Policy<T> defaultPolicy, int tau) {
    if (tau < 0) {
      throw new IllegalArgumentException("Tau must be non-negative.");
    }
    
    this.defaultPolicy = defaultPolicy;
    this.tau = tau;
  }

  public void updatePolicy(Policy<T> newPolicy) {
    this.defaultPolicy = newPolicy;
  }

  public ChosenAction chooseAction(long saltedSeed, T context) {
    Random random = new Random(saltedSeed);
    int numActions = context.getNumberOfActions();

    int chosenAction = 0;
    float actionProbability = 0.0f;
    boolean logAction;

    if ((tau > 0) && explore) {
      tau--;
      chosenAction = random.nextInt(numActions) + 1; // Add 1 because actions are 1-indexed
      actionProbability = 1.f / numActions;
      logAction = true;
    } else {
      // Invoke the default policy function to get the action
      chosenAction = defaultPolicy.chooseAction(context);

      if (chosenAction == 0 || chosenAction > numActions) {
        throw new RuntimeException("Action chosen by default policy is not within valid range.");
      }

      actionProbability = 1.f;
      logAction = false;
    }

    return new ChosenAction(chosenAction, actionProbability, logAction);
  }

  public void enableExplore(boolean explore) {
    this.explore = explore;
  }
}
