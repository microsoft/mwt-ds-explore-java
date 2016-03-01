package com.mwt.explorers.utilities;

import com.mwt.contexts.VariableActionContext;

/**
 * Created by jmorra on 2/29/16.
 */
public class FixedVariableActionContext implements VariableActionContext {
    private final int numActions;

    public FixedVariableActionContext(final int numActions) {
        this.numActions = numActions;
    }

    @Override
    public int getNumberOfActions() {
        return numActions;
    }
}
