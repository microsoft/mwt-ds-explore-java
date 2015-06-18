package com.mwt.consumers;

import com.mwt.scorers.Scorer;

public interface ConsumeScorer<T> {
  void updateScorer(Scorer<T> newScorer);
}
