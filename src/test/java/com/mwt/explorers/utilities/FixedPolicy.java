package com.mwt.explorers.utilities;

import com.mwt.policies.Policy;

/**
 * Created by jmorra on 2/29/16.
 */
public class FixedPolicy<T> implements Policy<T> {
    private final int action;

    public FixedPolicy(final int action) {
        this.action = action;
    }

    @Override
    public int chooseAction(T context) {
        return action;
    }
}
