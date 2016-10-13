package com.mwt.explorers;

import com.mwt.consumers.ConsumePolicies;
import com.mwt.misc.DecisionTuple;
import com.mwt.policies.Policy;
import com.mwt.utilities.PRG;

import java.util.List;

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

  public DecisionTuple chooseAction(long saltedSeed, T context) {
    final int numActionsForContext = getNumActions(context);
    final PRG random = new PRG(saltedSeed);

    // Select bag
    final int numPolicies = policies.size();
    final int chosenBag = random.uniformInt(0, numPolicies - 1);

    // Invoke the default policy function to get the action
    int chosenAction = 0;
    float actionProbability = 0f;

    if (explore) {
      int actionFromBag = 0;
      final int[] actionsSelected = new int[numActionsForContext];

      // Invoke the default policy function to get the action
      int currentBag = 0;
      for (Policy<T> policy: policies) {
        // TODO: can VW predict for all bags on one call? (returning all actions at once)
        // if we trigger into VW passing an index to invoke bootstrap scoring, and if VW model
        // changes while we are doing so, we could end up calling the wrong bag
        actionFromBag = policy.chooseAction(context);

        if (actionFromBag <= 0 || actionFromBag > numActionsForContext) {
          throw new RuntimeException("Action chosen by default policy is not within valid range.");
        }

        if (currentBag == chosenBag) {
          chosenAction = actionFromBag;
        }

        //this won't work if actions aren't 0 to Count
        actionsSelected[actionFromBag - 1] = actionsSelected[actionFromBag - 1] + 1; // action id is one-based
        ++currentBag;
      }

      actionProbability = (float)actionsSelected[chosenAction - 1] / numPolicies; // action id is one-based
    } else {
      chosenAction = policies.get(0).chooseAction(context);
      actionProbability = 1f;
    }

    return new DecisionTuple(chosenAction, actionProbability, true);
  }

  public void enableExplore(boolean explore) {
    this.explore = explore;
  }
}
