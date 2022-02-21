package io.github.ilnurnasybullin.ahp.domain;

public interface IDoubleMatrix<T extends IDoubleMatrix<T>> {
    T columnsSum();

    T divideRows(IDoubleMatrix<?> columnsSum);

    T rowsAverage();

    T appendColumns(IDoubleMatrix<?> ... appenders);

    double[][] toArray();

    T matrixMult(IDoubleMatrix<?> matrix);
}
