package com.mwt.explorers;

import com.mwt.consumers.ConsumePolicy;
import com.mwt.misc.ChosenAction;
import com.mwt.policies.Policy;

import java.util.Random;

/**
 * The epsilon greedy exploration algorithm. This is a good choice if you have no idea
 * which actions should be preferred.  Epsilon greedy is also computationally cheap.
 */
public class EpsilonGreedyExplorer<T> implements Explorer<T>, ConsumePolicy<T> {
  private Policy<T> defaultPolicy;
  private final float epsilon;
  private boolean explore = true;
  private int numActions;

  /**
   * The constructor
   *
   * @param defaultPolicy   A default function which outputs an action given a context.
   * @param epsilon         The probability of a random exploration.
   * @param numActions      The number of actions to randomize over.
   *
   */
  public EpsilonGreedyExplorer(Policy<T> defaultPolicy, float epsilon, int numActions) {
    if (numActions < 1) {
      throw new IllegalArgumentException("Number of actions must be at least 1.");
    }
    if (epsilon < 0 || epsilon > 1) {
      throw new IllegalArgumentException("Epsilon must be between 0 and 1.");
    }
    
    this.defaultPolicy = defaultPolicy;
    this.epsilon = epsilon;
    this.numActions = numActions;
  }

  protected int getNumActions(T context) {
    return numActions;
  }

  public void updatePolicy(Policy<T> newPolicy) {
    this.defaultPolicy = newPolicy;
  }

  public ChosenAction chooseAction(long saltedSeed, T context) {
    Random random = new Random(saltedSeed);

    int chosenAction = defaultPolicy.chooseAction(context);
    if (chosenAction <= 0 || chosenAction > getNumActions(context)) {
      throw new RuntimeException("Action chosen by default policy is not within valid range.");
    }

    float epsilon = explore ? this.epsilon : 0f;
    float actionProbability = 0f;
    float baseProbability = epsilon / (float) getNumActions(context);
    if (random.nextFloat() < (1.0f - epsilon)) {
      actionProbability = 1.f - epsilon + baseProbability;
    } else {
      // Get uniform random action ID
      int actionId = random.nextInt(getNumActions(context)) + 1; // Add 1 because actions are 1-indexed
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
