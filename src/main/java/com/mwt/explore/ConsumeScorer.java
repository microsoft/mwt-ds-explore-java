package com.mwt.explore;

public interface ConsumeScorer<T> {
  void updateScorer(Scorer<T> newScorer);
}
