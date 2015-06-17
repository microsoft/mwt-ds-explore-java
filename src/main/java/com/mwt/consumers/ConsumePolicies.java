package com.mwt.consumers;

import com.mwt.policies.Policy;

import java.util.List;

public interface ConsumePolicies<T> {
  void updatePolicy(List<Policy<T>> newPolicies);
}
