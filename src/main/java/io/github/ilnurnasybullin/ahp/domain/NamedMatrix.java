package io.github.ilnurnasybullin.ahp.domain;

public record NamedMatrix<R>(String[] rowNames, String[] columnNames, R[][] matrix) {

    public int rows() {
        return rowNames.length;
    }

    public int columns() {
        return columnNames.length;
    }

}
