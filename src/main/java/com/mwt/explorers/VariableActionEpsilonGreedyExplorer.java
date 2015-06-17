package com.mwt.explorers;

import com.mwt.consumers.ConsumePolicy;
import com.mwt.contexts.VariableActionContext;
import com.mwt.misc.ChosenAction;
import com.mwt.policies.Policy;

import java.util.Random;

/**
 * The epsilon greedy exploration algorithm. This is a good choice if you have no idea
 * which actions should be preferred.  Epsilon greedy is also computationally cheap.
 */
public class VariableActionEpsilonGreedyExplorer<T extends VariableActionContext> implements Explorer<T>, ConsumePolicy<T> {
  private Policy<T> defaultPolicy;
  private final float epsilon;
  private boolean explore = true;

  /**
   * The constructor .
   *
   * @param defaultPolicy   A default function which outputs an action given a context.
   * @param epsilon         The probability of a random exploration.
   *
   */
  public VariableActionEpsilonGreedyExplorer(Policy<T> defaultPolicy, float epsilon) {
    if (epsilon < 0 || epsilon > 1) {
      throw new IllegalArgumentException("Epsilon must be between 0 and 1.");
    }
    
    this.defaultPolicy = defaultPolicy;
    this.epsilon = epsilon;
  }

  public void updatePolicy(Policy<T> newPolicy) {
    this.defaultPolicy = newPolicy;
  }

  public ChosenAction chooseAction(long saltedSeed, T context) {
    int numActions = context.getNumberOfActions();
    Random random = new Random(saltedSeed);

    int chosenAction = defaultPolicy.chooseAction(context);
    if (chosenAction <= 0 || chosenAction > numActions) {
      throw new RuntimeException("Action chosen by default policy is not within valid range.");
    }

    float epsilon = explore ? this.epsilon : 0f;
    float actionProbability = 0f;
    float baseProbability = epsilon / (float) numActions;
    if (random.nextFloat() < (1.0f - epsilon)) {
      actionProbability = 1.f - epsilon + baseProbability;
    } else {
      // Get uniform random action ID
      int actionId = random.nextInt(numActions) + 1; // Add 1 because actions are 1-indexed
      if (actionId == chosenAction) {
        // IF it matches the one chosen by the default policy
        // then increase the probability
        actionProbability = 1.f - epsilon + baseProbability;
      }
      else {
        // Otherwise it's just the uniform probability
        actionProbability = baseProbability;
      }
      chosenAction = actionId;
    }
    return new ChosenAction(chosenAction, actionProbability, true);
  }

  public void enableExplore(boolean explore) {
    this.explore = explore;
  }
}
