package com.mwt.tests;

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
}