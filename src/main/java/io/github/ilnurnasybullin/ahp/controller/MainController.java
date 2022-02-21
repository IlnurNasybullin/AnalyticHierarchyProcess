package io.github.ilnurnasybullin.ahp.controller;

import io.github.ilnurnasybullin.ahp.domain.NamedMatrix;
import io.github.ilnurnasybullin.ahp.domain.QualitativeComparison;
import io.github.ilnurnasybullin.ahp.service.AlternativeWeightsCalculationService;
import io.github.ilnurnasybullin.ahp.service.FXMLLoaderService;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

@Controller
public class MainController {

    @FXML
    private Button prevButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button cancelButton;

    @FXML
    private AnchorPane contentPane;

    private final ObservableList<PageController> controllers;

    private final FXMLLoaderService loaderService;
    private final AlternativeWeightsCalculationService calculationService;

    private final ObservableList<String> criteria;
    private final ObservableList<String> alternatives;

    private final IntegerBinding matricesCount;
    private final IntegerProperty currentPage = new SimpleIntegerProperty(0);

    public MainController(FXMLLoaderService loaderService, AlternativeWeightsCalculationService calculationService) throws IOException {
        criteria = FXCollections.observableArrayList();
        alternatives = FXCollections.observableArrayList();

        this.loaderService = loaderService;
        this.calculationService = calculationService;
        matricesCount = getMatricesBinding();
        controllers = FXCollections.observableArrayList(new DataNamingController(loaderService, criteria, alternatives));
    }

    private IntegerBinding getMatricesBinding() {
        return new IntegerBinding() {
            {
                super.bind(criteria, alternatives);
            }

            @Override
            protected int computeValue() {
                if (criteria.isEmpty()) {
                    return 0;
                }

                if (alternatives.isEmpty()) {
                    return 1;
                }

                return criteria.size() + 1;
            }
        };
    }

    @FXML
    public void initialize() {
        contentPane.getChildren().add(controllers.get(currentPage.get()).node());
        BooleanBinding noPrevControllers = Bindings.size(controllers).lessThan(2);

        prevButton.disableProperty().bind(noPrevControllers);
        prevButton.setOnAction(event -> showPrevPane());

        BooleanBinding oneOfListIsEmpty = Bindings.isEmpty(criteria).or(Bindings.isEmpty(alternatives));
        nextButton.disableProperty().bind(oneOfListIsEmpty);
        nextButton.setOnAction(event -> showNextPane());

        cancelButton.setOnAction(event -> closeApp());
    }

    private void showPrevPane() {
        controllers.remove(currentPage.get());
        decrementCurrentPage();
        Node currentNode = controllers.get(currentPage.get()).node();

        showNode(currentNode);
    }

    private void decrementCurrentPage() {
        currentPage.set(currentPage.get() - 1);
    }

    private void showNextPane() {
        if (currentPage.get() == matricesCount.get()) {
            showResultStage();
            return;
        }

        incrementCurrentPage();

        String title;
        ObservableList<String> rowHeaders;
        ObservableList<String> columnHeaders;
        if (currentPage.get() == 1) {
            title = "Criteria's matrix";
            rowHeaders = columnHeaders = criteria;
        } else {
            title = String.format("Alternatives' matrix for %s", criteria.get(currentPage.get() - 2));
            rowHeaders = columnHeaders = alternatives;
        }

        MatrixController controller;
        try {
            controller = new MatrixController(loaderService, title, rowHeaders, columnHeaders);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        controllers.add(controller);

        showNode(controller.node());
    }

    private void showNode(Node node) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(node);
    }

    private void incrementCurrentPage() {
        currentPage.set(currentPage.get() + 1);
    }

    private void showResultStage() {
        NamedMatrix<QualitativeComparison> criteria = controllers.get(1).self(MatrixController.class).data();

        @SuppressWarnings("unchecked")
        NamedMatrix<QualitativeComparison>[] alternatives = controllers.stream().skip(2)
                .map(controller -> controller.self(MatrixController.class).data())
                .toArray(NamedMatrix[]::new);

        NamedMatrix<Double> weights = calculationService.weights(criteria, alternatives);
        ResultController controller = new ResultController(weights);
        try {
            Parent root = loaderService.loadController(ResultController.class.getResource("Result.fxml"), controller);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.setTitle("Result");
            stage.show();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    private void closeApp() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
