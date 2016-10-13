package com.mwt.explorers;

import com.mwt.consumers.ConsumeScorer;
import com.mwt.misc.DecisionTuple;
import com.mwt.scorers.Scorer;
import com.mwt.utilities.PRG;

import java.util.List;

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
    final PRG random = new PRG(saltedSeed);

    // Invoke the default scorer function
    final List<Float> scores = defaultScorer.scoreActions(context);
    final int numScores = scores.size();
    if (numScores != getNumActions(context)) {
      throw new RuntimeException("The number of scores returned by the scorer must equal number of actions");
    }

    int actionIndex = 0;
    float actionProbability = 1f;
    float maxScore = Float.MIN_VALUE;
    int i = 0;
    for (float score: scores) {
      if (maxScore < score) {
        maxScore = score;
        actionIndex = i;
      }
      ++i;
    }

    if (explore) {
      float[] newScores = new float[numScores];
      // Create a normalized exponential distribution based on the returned scores
      i = 0;
      for (float score: scores) {
        newScores[i++] = (float) Math.exp(lambda * (score - maxScore));
      }

      // Create a discrete_distribution based on the returned weights. This class handles the
      // case where the sum of the weights is < or > 1, by normalizing agains the sum.
      float total = 0f;
      for (i = 0; i < numScores; i++) {
        total += newScores[i];
      }

      float draw = random.uniformUnitInterval();

      float sum = 0f;
      actionProbability = 0f;
      actionIndex = numScores - 1;
      for (i = 0; i < numScores; i++) {
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
