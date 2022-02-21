package io.github.ilnurnasybullin.ahp.service;

import io.github.ilnurnasybullin.ahp.domain.IDoubleMatrix;
import io.github.ilnurnasybullin.ahp.math.DoubleMatrixImpl;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;

@Service
public class MathAlternativeWeightsCalculationServiceImpl implements MathAlternativeWeightsCalculationService {

    @Override
    public double[][] weights(double[][] criteria, double[][]... alternatives) {
        double[][][] matrices = new double[alternatives.length + 1][][];
        matrices[0] = criteria;
        System.arraycopy(alternatives, 0, matrices, 1, alternatives.length);

        @SuppressWarnings("unchecked")
        IDoubleMatrix<DoubleMatrixImpl>[] dMatrices = new IDoubleMatrix[matrices.length];

        //can be optimized by dataflow programming
        for (int i = 0; i < matrices.length; i++) {
            IDoubleMatrix<DoubleMatrixImpl> matrix = DoubleMatrixImpl.of(matrices[i]);
            IDoubleMatrix<DoubleMatrixImpl> columnsSum = matrix.columnsSum();
            IDoubleMatrix<DoubleMatrixImpl> dividedMatrix = matrix.divideRows(columnsSum);

            dMatrices[i] = dividedMatrix.rowsAverage();
        }

        IDoubleMatrix<DoubleMatrixImpl> criteriaMatrix = dMatrices[0];
        IDoubleMatrix<DoubleMatrixImpl> appender = dMatrices[1];

        int appendersCount = alternatives.length - 1;

        @SuppressWarnings("unchecked")
        IDoubleMatrix<DoubleMatrixImpl>[] appenders = new IDoubleMatrix[appendersCount];
        System.arraycopy(dMatrices, 2, appenders, 0, appendersCount);

        IDoubleMatrix<DoubleMatrixImpl> result = appender.appendColumns(appenders).matrixMult(criteriaMatrix);

        return result.toArray();
    }
}
