package com.mwt.explore;

import java.util.List;

public interface ConsumePolicies<T> {
  void updatePolicy(List<Policy<T>> newPolicies);
}
