package com.mwt.sample;

import com.mwt.policies.Policy;

/**
 * Example of a custom policy which implements the IPolicy<MyContext>,
 * declaring that this policy only interacts with MyContext objects.
 */
class MyPolicy implements Policy<MyContext> {
  private int index;

  public MyPolicy() {
    this(-1);
  }

  public MyPolicy(int index) {
    this.index = index;
  }

  public int chooseAction(MyContext context) {
    // Always returns the same action regardless of context
    return 5;
  }
}
