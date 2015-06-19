package com.mwt.sample;

import com.mwt.contexts.SimpleContext;
import com.mwt.policies.Policy;

/**
 * Example of a custom policy which implements the IPolicy<SimpleContext>,
 * declaring that this policy only interacts with SimpleContext objects.
 */
class StringPolicy implements Policy<SimpleContext> {
  public int chooseAction(SimpleContext context) {
    // Always returns the same action regardless of context
    return 1;
  }
}
