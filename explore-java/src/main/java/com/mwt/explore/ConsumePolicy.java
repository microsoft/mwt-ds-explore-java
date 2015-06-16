package com.mwt.explore;

public interface ConsumePolicy<T> {
  void updatePolicy(Policy<T> newPolicy);
}
