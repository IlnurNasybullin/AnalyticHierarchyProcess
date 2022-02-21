package io.github.ilnurnasybullin.ahp.controller;

import io.github.ilnurnasybullin.ahp.domain.NamedMatrix;
import io.github.ilnurnasybullin.ahp.domain.QualitativeComparison;
import io.github.ilnurnasybullin.ahp.service.FXMLLoaderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Collections;

public class MatrixController implements PageController, DataMiningController<NamedMatrix<QualitativeComparison>> {

    private final ObservableList<String> rowHeaders;
    private final ObservableList<String> columnHeaders;

    private final Node node;
    private final String title;
    private Matrix<ComboBox<QualitativeComparison>> comparisonMatrix;

    private record Matrix<T>(T[][] matrix) {

        public T get(int row, int column) {
            return matrix[row][column];
        }


    }
    @FXML
    private GridPane matrix;

    @FXML
    private Label titleLabel;

    public MatrixController(FXMLLoaderService loaderService, String title,
                            ObservableList<String> rowHeaders, ObservableList<String> columnHeaders) throws IOException {
        this.rowHeaders = rowHeaders;
        this.columnHeaders = columnHeaders;
        this.title = title;

        node = loaderService.loadController(MatrixController.class.getResource("Matrix.fxml"), this);
    }

    @FXML
    private void initialize() {
        titleLabel.setText(title);

        matrix.addColumn(0, notEditableTextField(""));
        matrix.addColumn(0, rowHeaders.stream().map(this::notEditableTextField).toArray(Node[]::new));

        int i = 1;
        for (String columnHeader: columnHeaders) {
            matrix.addColumn(i, notEditableTextField(columnHeader));
            i++;
        }

        this.comparisonMatrix = createMatrix(rowHeaders.size());
        renderMatrix(comparisonMatrix);

        matrix.getColumnConstraints().addAll(Collections.nCopies(columnHeaders.size() + 1, new ColumnConstraints(100.0)));
    }

    private void renderMatrix(Matrix<ComboBox<QualitativeComparison>> comparisonMatrix) {
        for (int i = 1; i < matrix.getRowCount(); i++) {
            for (int j = 1; j < matrix.getColumnCount(); j++) {
                matrix.add(comparisonMatrix.get(j - 1, i - 1), i, j);
            }
        }
    }

    private Matrix<ComboBox<QualitativeComparison>> createMatrix(int size) {
        ObservableList<QualitativeComparison> comparisons = FXCollections.observableArrayList(QualitativeComparison.values());

        @SuppressWarnings("unchecked")
        ComboBox<QualitativeComparison>[][] comboBoxes = new ComboBox[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                comboBoxes[i][j] = new ComboBox<>(comparisons);
                comboBoxes[i][j].setStyle("-fx-font-weight: bold");
            }
        }

        for (int i = 0; i < size; i++) {
            ComboBox<QualitativeComparison> comboBox = comboBoxes[i][i];
            comboBox.valueProperty().set(QualitativeComparison.EQUAL);
            comboBox.setDisable(true);
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ComboBox<QualitativeComparison> comboBox = comboBoxes[i][j];
                if (j < i + 1) {
                    comboBox.setDisable(true);
                    continue;
                }

                int finalJ = j;
                int finalI = i;
                comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (oldValue != newValue && newValue != null) {
                        ComboBox<QualitativeComparison> alternativeComboBox = comboBoxes[finalJ][finalI];
                        alternativeComboBox.valueProperty().set(newValue.neg());
                    }
                });
            }
        }

        return new Matrix<>(comboBoxes);
    }

    private TextField notEditableTextField(String title) {
        TextField textField = new TextField(title);
        textField.setEditable(false);

        return textField;
    }

    @Override
    public Node node() {
        return node;
    }

    @Override
    public <T extends PageController> T self(Class<T> tClass) {
        if (tClass != MatrixController.class) {
            throw new IllegalArgumentException();
        }

        return self();
    }

    @SuppressWarnings("unchecked")
    private <T extends PageController> T self() {
        return (T) this;
    }

    @Override
    public NamedMatrix<QualitativeComparison> data() {
        int sizeX = rowHeaders.size();
        int sizeY = columnHeaders.size();
        QualitativeComparison[][] matrix = new QualitativeComparison[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                matrix[i][j] = comparisonMatrix.get(i, j).getValue();
            }
        }

        return new NamedMatrix<>(rowHeaders.toArray(String[]::new), columnHeaders.toArray(String[]::new), matrix);
    }
}
