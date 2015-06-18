package com.mwt.consumers;

import com.mwt.policies.Policy;

public interface ConsumePolicy<T> {
  void updatePolicy(Policy<T> newPolicy);
}
