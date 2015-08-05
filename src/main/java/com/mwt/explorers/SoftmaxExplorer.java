package com.mwt.explorers;

import com.mwt.consumers.ConsumeScorer;
import com.mwt.misc.DecisionTuple;
import com.mwt.scorers.Scorer;
import com.mwt.utilities.PRG;

import java.util.ArrayList;

/**
 * In some cases, different actions have a different scores, and you would prefer to
 * choose actions with large scores. Softmax allows you to do that.
 */
public class SoftmaxExplorer<T> implements Explorer<T>, ConsumeScorer<T> {
  private Scorer<T> defaultScorer;
  private boolean explore = true;
  private final float lambda;
  private final int numActions;

  /**
   * Constructor
   *
   * @param defaultScorer  A function which outputs a score for each action.
   * @param lambda          lambda = 0 implies uniform distribution.  Large lambda is equivalent to a max.
   * @param numActions     The number of actions to randomize over.
   */
  public SoftmaxExplorer(Scorer<T> defaultScorer, float lambda, int numActions) {
    if (numActions < 1) {
      throw new IllegalArgumentException("Number of actions must be at least 1.");
    }

    this.defaultScorer = defaultScorer;
    this.lambda = lambda;
    this.numActions = numActions;
  }

  protected int getNumActions(T context) {
    return numActions;
  }

  public void updateScorer(Scorer<T> newScorer) {
    defaultScorer = newScorer;
  }

  public DecisionTuple chooseAction(long saltedSeed, T context) {
    PRG random = new PRG(saltedSeed);

    // Invoke the default scorer function
    ArrayList<Float> scores = defaultScorer.scoreActions(context);
    int numScores = scores.size();
    if (numScores != getNumActions(context)) {
      throw new RuntimeException("The number of scores returned by the scorer must equal number of actions");
    }

    int actionIndex = 0;
    float actionProbability = 1.0f;
    Float maxScore = Float.MIN_VALUE;
    for (int i = 0; i < numScores; i++) {
      if (maxScore < scores.get(i)) {
        maxScore = scores.get(i);
        actionIndex = i;
      }
    }

    if (explore) {
      float[] newScores = new float[numScores];
      // Create a normalized exponential distribution based on the returned scores
      for (int i = 0; i < numScores; i++)
      {
        newScores[i] = (float)Math.exp(lambda * (scores.get(i) - maxScore));
      }

      // Create a discrete_distribution based on the returned weights. This class handles the
      // case where the sum of the weights is < or > 1, by normalizing agains the sum.
      float total = 0.f;
      for (int i = 0; i < numScores; i++) {
        total += newScores[i];
      }

      float draw = random.uniformUnitInterval();

      float sum = 0.f;
      actionProbability = 0.f;
      actionIndex = numScores - 1;
      for (int i = 0; i < numScores; i++) {
        newScores[i] = newScores[i] / total;
        sum += newScores[i];
        if (sum > draw) {
          actionIndex = i;
          actionProbability = newScores[i];
          break;
        }
      }
    }

    // action id is one-based
    return new DecisionTuple(actionIndex + 1, actionProbability, true);
  }

  public void enableExplore(boolean explore) {
    this.explore = explore;
  }
}
