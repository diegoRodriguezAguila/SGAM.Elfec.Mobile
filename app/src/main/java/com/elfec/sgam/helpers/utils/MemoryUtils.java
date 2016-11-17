package com.elfec.sgam.helpers.utils;

/**
 * Created by drodriguez on 17/11/2016.
 * Utils for memory sizes
 */

public class MemoryUtils {

    private static final int[] MEMORY_SIZES = {0, 2, 4, 8, 16, 32, 64, 128, 200, 256, 512, 1024};

    /**
     * Transform the memory size to a retail equivalent. I.e.
     * realSize = 13.7 Gb, its retail size would be 16 Gb
     *
     * @param realSize size
     * @return retail size
     */
    public static double getRetailMemorySize(double realSize) {
        if (realSize < 1)
            return realSize;
        for (int i = 0; i < MEMORY_SIZES.length - 1; i++) {
            if (MEMORY_SIZES[i] < realSize && MEMORY_SIZES[i + 1] >= realSize) {
                return MEMORY_SIZES[i + 1];
            }
        }
        return realSize;
    }
}
