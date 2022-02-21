package io.github.ilnurnasybullin.ahp.math;

import io.github.ilnurnasybullin.ahp.domain.IDoubleMatrix;
import org.jblas.DoubleMatrix;

import java.util.Arrays;

public record DoubleMatrixImpl(DoubleMatrix matrix) implements IDoubleMatrix<DoubleMatrixImpl> {

    @Override
    public DoubleMatrixImpl columnsSum() {
        return new DoubleMatrixImpl(matrix.columnSums());
    }

    @Override
    public DoubleMatrixImpl divideRows(IDoubleMatrix<?> matrix) {
        DoubleMatrix dMatrix = toDoubleMatrix(matrix);
        return new DoubleMatrixImpl(this.matrix.diviRowVector(dMatrix));
    }

    @Override
    public DoubleMatrixImpl rowsAverage() {
        return new DoubleMatrixImpl(matrix.rowMeans());
    }

    @Override
    public DoubleMatrixImpl appendColumns(IDoubleMatrix<?>... appenders) {
        DoubleMatrix resultMatrix = Arrays.stream(appenders)
                .map(this::toDoubleMatrix)
                .reduce(matrix, DoubleMatrix::concatHorizontally);
        return new DoubleMatrixImpl(resultMatrix);
    }

    //new Double Matrix!!!
    private DoubleMatrix toDoubleMatrix(IDoubleMatrix<?> matrix) {
        return new DoubleMatrix(matrix.toArray());
    }

    @Override
    public double[][] toArray() {
        return matrix.toArray2();
    }

    @Override
    public DoubleMatrixImpl matrixMult(IDoubleMatrix<?> matrix) {
        return new DoubleMatrixImpl(this.matrix.mmul(toDoubleMatrix(matrix)));
    }

    public static DoubleMatrixImpl of(double[][] matrix) {
        return new DoubleMatrixImpl(new DoubleMatrix(matrix));
    }
}
