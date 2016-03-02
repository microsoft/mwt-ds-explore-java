package com.mwt.externalTests;

/**
 * Represents a configuration object containing the type of test case & parameters to run.
 */
public class TestConfiguration {
    // base parameters
    public int Type;
    public String OutputFile;

    // hash parameters
    public String[] Values;

    // prg parameters
    public long Seed;
    public int Iterations;
    public Tuple<Integer, Integer> UniformInterval;

    // base exploration parameters
    public PolicyConfiguration PolicyConfiguration;
    public String AppId;
    public int ContextType;
    public int NumberOfActions;
    public String[] ExperimentalUnitIdList;

    // epsilon greedy parameters
    public float Epsilon;

    // tau-first parameters
    public int Tau;

    // bootstrap parameters
    public PolicyConfiguration[] PolicyConfigurations;

    // softmax parameters
    public float Lambda;
    public ScorerConfiguration ScorerConfiguration;
}
