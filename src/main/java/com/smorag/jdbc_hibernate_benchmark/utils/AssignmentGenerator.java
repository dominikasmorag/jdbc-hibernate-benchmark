package com.smorag.jdbc_hibernate_benchmark.utils;

import java.util.Random;

public class AssignmentGenerator {

    private AssignmentGenerator() {}

    public static int[] generateAssignments(int orderCount, int userCount) {
        Random random = new Random(12345);
        int[] assignments = new int[orderCount];
        for (int i = 0; i < orderCount; i++) {
            assignments[i] = random.nextInt(userCount);
        }

        return assignments;
    }
}