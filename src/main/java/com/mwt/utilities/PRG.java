package com.mwt.utilities;

/// <summary>
/// Translated implemetation of C++ random generator in explore-cpp.
/// </summary>
public class PRG {
    private static final long A = 0xeece66d5deece66dL;
    private static final long C = 2147483647;
    private static final int Bias = 127 << 23;

    private long V;

    /// <summary>
    /// Constructs the random number generator using the default seed.
    /// </summary>
    public PRG() {
        this(C);
    }

    /// <summary>
    /// Constructs the random number generator using the specified seed.
    /// </summary>
    public PRG(long initial) {
        this.V = initial;
    }

    private float merand48() {
        V = A * V + C;
        return Float.intBitsToFloat((int)(((V >>> 25) & 0x7FFFFF) | Bias)) - 1;
    }

    /// <summary>
    /// Returns a real number drawn uniformly from [0,1].
    /// </summary>
    /// <returns>The random number as a float.</returns>
    public float uniformUnitInterval() {
        return merand48();
    }

    /// <summary>
    /// Returns an integer drawn uniformly from the specified interval.
    /// </summary>
    /// <param name="low">The inclusive start of the interval.</param>
    /// <param name="high">The inclusive end of the interval.</param>
    /// <returns>The random number as an unsigned integer.</returns>
    public int uniformInt(int low, int high) {
        merand48();
        return low + (int)((V >>> 25) % (high - low + 1));
    }
}
