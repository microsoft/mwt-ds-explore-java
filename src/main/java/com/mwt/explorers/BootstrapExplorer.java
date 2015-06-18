package com.mwt.explorers;

import com.mwt.consumers.ConsumePolicies;
import com.mwt.misc.ChosenAction;
import com.mwt.policies.Policy;

import java.util.List;
import java.util.Random;

/**
 * The Bootstrap explorer randomizes over the actions chosen by a set of default policies. 
 * This performs well statistically but can be computationally expensive.
 * Should be used with the MwtExplorer.
 */
public class BootstrapExplorer<T> implements Explorer<T>, ConsumePolicies<T> {
  private List<Policy<T>> policies;
  private boolean explore = true;
  final private int numActions;

  /**
   * @param policies  A set of default policies to be uniform random over.
   * The policy pointers must be valid throughout the lifetime of this explorer.
   * @param numActions The number of actions to randomize over.
   */
  public BootstrapExplorer(List<Policy<T>> policies, int numActions) {
    if (policies.size() < 1) {
      throw new IllegalArgumentException("Must have a non-empty policy list");
    }
    this.policies = policies;
    this.numActions = numActions;
  }

  protected int getNumActions(T context) {
    return numActions;
  }

  public void updatePolicy(List<Policy<T>> newPolicies) {
    this.policies = newPolicies;
  }

  public ChosenAction chooseAction(long saltedSeed, T context) {
    Random random = new Random(saltedSeed);

    // Select bag
    int chosenBag = random.nextInt(policies.size());

    // Invoke the default policy function to get the action
    int chosenAction = 0;
    float actionProbability = 0.0f;

    if (explore) {
      int actionFromBag = 0;
      int[] actionsSelected = new int[getNumActions(context)];

      // Invoke the default policy function to get the action
      for (int currentBag = 0; currentBag < policies.size(); currentBag++) {
        // TODO: can VW predict for all bags on one call? (returning all actions at once)
        // if we trigger into VW passing an index to invoke bootstrap scoring, and if VW model changes while we are doing so,
        // we could end up calling the wrong bag
        actionFromBag = policies.get(currentBag).chooseAction(context);

        if (actionFromBag <= 0 || actionFromBag > getNumActions(context)) {
          throw new RuntimeException("Action chosen by default policy is not within valid range.");
        }

        if (currentBag == chosenBag) {
          chosenAction = actionFromBag;
        }

        //this won't work if actions aren't 0 to Count
        actionsSelected[actionFromBag - 1] = actionsSelected[actionFromBag - 1] + 1; // action id is one-based
      }
      actionProbability = (float)actionsSelected[chosenAction - 1] / policies.size(); // action id is one-based
    } else {
      chosenAction = policies.get(0).chooseAction(context);
      actionProbability = 1.f;
    }

    return new ChosenAction(chosenAction, actionProbability, true);
  }

  public void enableExplore(boolean explore) {
    this.explore = explore;
  }
}
