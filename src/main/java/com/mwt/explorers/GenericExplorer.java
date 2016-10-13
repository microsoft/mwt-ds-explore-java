package com.mwt.explorers;

import com.mwt.consumers.ConsumeScorer;
import com.mwt.misc.DecisionTuple;
import com.mwt.scorers.Scorer;
import com.mwt.utilities.PRG;

import java.util.List;

/**
 * GenericExplorer provides complete flexibility.  You can create any
 * distribution over actions desired, and it will draw from that.
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

  protected int getNumActions(T context) {
    return numActions;
  }

  public void updateScorer(Scorer<T> newScorer) {
    defaultScorer = newScorer;
  }

  public DecisionTuple chooseAction(long saltedSeed, T context) {
    final PRG random = new PRG(saltedSeed);

    // Invoke the default scorer function
    final List<Float> weights = defaultScorer.scoreActions(context);
    final int numWeights = weights.size();
    if (numWeights != getNumActions(context)) {
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

    final float draw = random.uniformUnitInterval();

    float sum = 0.f;
    float actionProbability = 0.f;
    int actionIndex = numWeights - 1;
    int i = 0;
    for (float weight: weights) {
      final float prob = weight / total;
      sum += weight / total;
      if (sum > draw) {
        actionIndex = i;
        actionProbability = prob;
        break;
      }
      ++i;
    }

    // action id is one-based
    return new DecisionTuple(actionIndex + 1, actionProbability, true);
  }

  public void enableExplore(boolean explore) {
    this.explore = explore;
  }
}
