package com.mwt.explorers.utilities;

import com.mwt.scorers.Scorer;

import java.util.ArrayList;

/**
 * Created by jmorra on 2/29/16.
 */
public class FixedScorer<T> implements Scorer<T> {

    private final ArrayList<Float> scores;

    public FixedScorer(final float... scores) {
        this.scores = new ArrayList<Float>();
        for (float score : scores) {
            this.scores.add(score);
        }
    }

    @Override
    public ArrayList<Float> scoreActions(T context) {
        return scores;
    }
}
