package com.mwt.tests;

import com.mwt.contexts.VariableActionContext;
import com.mwt.policies.Policy;
import com.mwt.scorers.Scorer;

import java.util.ArrayList;

// Explore Infrastructure
public class EI {

    public static class RegularTestContext {
        public Integer Id;

        @Override
        public String toString() {
            return Id.toString();
        }
    }

    public static class VariableActionTestContext extends RegularTestContext implements VariableActionContext {
        public int NumberOfActions;

        public VariableActionTestContext(int numberOfActions)
        {
            NumberOfActions = numberOfActions;
        }

        public int getNumberOfActions()
        {
            return NumberOfActions;
        }
    }

    public static class TestPolicy<TContext> implements Policy<TContext>
    {
        public TestPolicy() {
            this(-1);
        }

        public TestPolicy(int index)
        {
            this.index = index;
            this.ActionToChoose = Integer.MAX_VALUE;
        }

        public int chooseAction(TContext context)
        {
            return (this.ActionToChoose != Integer.MAX_VALUE) ? this.ActionToChoose : 5;
        }

        public int ActionToChoose;
        private int index;
    }

    public static class TestScorer<TContext> implements Scorer<TContext>
    {
        public TestScorer(int param, int numActions) {
            this(param, numActions, true);
        }

        public TestScorer(int param, int numActions, boolean uniform) {
            this.param = param;
            this.uniform = uniform;
            this.numActions = numActions;
        }
        public ArrayList<Float> scoreActions(TContext context)
        {
            if (uniform)
            {
                ArrayList<Float> scores = new ArrayList<Float>(numActions);
                for (int i = 0; i < numActions; i ++) {
                    scores.add((float)param);
                }
                return scores;
            }
            else
            {
                ArrayList<Float> scores = new ArrayList<Float>(numActions);
                for (int i = 0; i < numActions; i ++) {
                    scores.add((float)param + i);
                }
                return scores;
            }
        }
        private int param;
        private int numActions;
        private boolean uniform;
    }
}
