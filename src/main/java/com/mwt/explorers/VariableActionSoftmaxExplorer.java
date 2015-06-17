package com.mwt.explorers;

import com.mwt.consumers.ConsumeScorer;
import com.mwt.contexts.VariableActionContext;
import com.mwt.misc.ChosenAction;
import com.mwt.scorers.Scorer;

import java.util.ArrayList;
import java.util.Random;

/**
 * In some cases, different actions have a different scores, and you would prefer to
 * choose actions with large scores. Softmax allows you to do that.
 */
public class VariableActionSoftmaxExplorer<T extends VariableActionContext> implements Explorer<T>, ConsumeScorer<T> {
  private Scorer<T> defaultScorer;
  private boolean explore = true;
  private final float lambda;

  /**
   * Constructor
   *
   * @param defaultScorer  A function which outputs a score for each action.
   * @param lambda          lambda = 0 implies uniform distribution.  Large lambda is equivalent to a max.
   */
  public VariableActionSoftmaxExplorer(Scorer<T> defaultScorer, float lambda) {
    this.defaultScorer = defaultScorer;
    this.lambda = lambda;
  }

  public void updateScorer(Scorer<T> newScorer) {
    defaultScorer = newScorer;
  }

  public ChosenAction chooseAction(long saltedSeed, T context) {
    Random random = new Random(saltedSeed);

    // Invoke the default scorer function
    ArrayList<Float> scores = defaultScorer.scoreActions(context);
    int numScores = scores.size();
    if (numScores != context.getNumberOfActions()) {
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
        newScores[i] = (float)Math.exp(lambda * (newScores[i] - maxScore));
      }

      // Create a discrete_distribution based on the returned weights. This class handles the
      // case where the sum of the weights is < or > 1, by normalizing agains the sum.
      float total = 0.f;
      for (int i = 0; i < numScores; i++) {
        total += newScores[i];
      }

      float draw = random.nextFloat();

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
    return new ChosenAction(actionIndex + 1, actionProbability, true);
  }

  public void enableExplore(boolean explore) {
    this.explore = explore;
  }
}
