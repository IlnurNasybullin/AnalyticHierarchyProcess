package io.github.ilnurnasybullin.ahp.controller;

import io.github.ilnurnasybullin.ahp.domain.NamedMatrix;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Arrays;

public class ResultController {

    @FXML
    private GridPane gridTable;

    @FXML
    private Button closeButton;

    private final NamedMatrix<Double> matrix;

    public ResultController(NamedMatrix<Double> matrix) {
        this.matrix = matrix;
    }

    @FXML
    private void initialize() {
        Double[][] data = this.matrix.matrix();
        gridTable.addRow(0, Arrays.stream(matrix.columnNames()).map(this::notEditableTextField).toArray(Node[]::new));
        String[] rowNames = matrix.rowNames();

        for (int i = 0; i < data.length; i++) {
            gridTable.add(notEditableTextField(rowNames[i]), 0, i + 1);
            for (int j = 0; j < data[i].length; j++) {
                gridTable.add(notEditableTextField(Double.toString(data[i][j])), j + 1, i + 1);
            }
        }

        closeButton.setOnAction(event -> closeApp());
    }

    private void closeApp() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private TextField notEditableTextField(String text) {
        TextField textField = new TextField(text);
        textField.setEditable(false);

        return textField;
    }

}
