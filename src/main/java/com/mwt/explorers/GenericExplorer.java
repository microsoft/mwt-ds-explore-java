package com.mwt.explorers;

import com.mwt.consumers.ConsumeScorer;
import com.mwt.misc.ChosenAction;
import com.mwt.scorers.Scorer;

import java.util.ArrayList;
import java.util.Random;

/**
 * In some cases, different actions have a different scores, and you would prefer to
 * choose actions with large scores. Softmax allows you to do that.
 */
public class GenericExplorer<T> implements Explorer<T>, ConsumeScorer<T> {
  private Scorer<T> defaultScorer;
  private boolean explore = true;
  private final int numActions;

  /**
   * Constructor
   *
   * @param defaultScorer  A function which outputs the probability of each action.
   * @param numActions     The number of actions to randomize over.
   */
  public GenericExplorer(Scorer<T> defaultScorer, int numActions) {
    if (numActions < 1) {
      throw new IllegalArgumentException("Number of actions must be at least 1.");
    }

    this.defaultScorer = defaultScorer;
    this.numActions = numActions;
  }

  public void updateScorer(Scorer<T> newScorer) {
    defaultScorer = newScorer;
  }

  public ChosenAction chooseAction(long saltedSeed, T context) {
    Random random = new Random(saltedSeed);

    // Invoke the default scorer function
    ArrayList<Float> weights = defaultScorer.scoreActions(context);
    int numWeights = weights.size();
    if (numWeights != numActions) {
      throw new RuntimeException("The number of weights returned by the scorer must equal number of actions");
    }

    // Create a discrete_distribution based on the returned weights. This class handles the
    // case where the sum of the weights is < or > 1, by normalizing agains the sum.
    float total = 0.f;
    for (Float weight : weights) {
      if (weight < 0) {
        throw new RuntimeException("Scores must be non-negative.");
      }
      total += weight;
    }

    if (total == 0) {
      throw new RuntimeException("At least one score must be positive.");
    }

    float draw = random.nextFloat();

    float sum = 0.f;
    float actionProbability = 0.f;
    int actionIndex = numWeights - 1;
    for (int i = 0; i < numWeights; i++) {
      float prob = weights.get(i) / total;
      sum += prob;
      if (sum > draw) {
        actionIndex = i;
        actionProbability = prob;
        break;
      }
    }

    // action id is one-based
    return new ChosenAction(actionIndex + 1, actionProbability, true);
  }

  public void enableExplore(boolean explore) {
    this.explore = explore;
  }
}
