package io.github.ilnurnasybullin.ahp;

import org.jblas.DoubleMatrix;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        double[][] a = {
                {1, 1, 1},
                {2, 2, 2},
                {3, 3, 3}
        };

        DoubleMatrix matrix = new DoubleMatrix(a);

        double[][] b = {{2, 2, 2}};
        DoubleMatrix row = new DoubleMatrix(b);

        System.out.println(Arrays.deepToString(matrix.rowMeans().toArray2()));
        System.out.println(Arrays.deepToString(row.toArray2()));

        System.out.println(Arrays.deepToString(matrix.columnSums().toArray2()));
    }
}
