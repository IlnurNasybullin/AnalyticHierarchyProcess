package io.github.ilnurnasybullin.ahp.controller;

import io.github.ilnurnasybullin.ahp.service.FXMLLoaderService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;

import java.io.IOException;
import java.util.ListIterator;

public class DataNamingController implements PageController {

    private final ObservableList<String> criteria;
    private final ObservableList<String> alternatives;

    private final Parent node;

    @FXML
    private ListView<String> criteriaListView;

    @FXML
    private ListView<String> alternativesListView;

    @FXML
    private Button addCriteria;

    @FXML
    private Button removeCriteria;

    @FXML
    private Button addAlternative;

    @FXML
    private Button removeAlternative;

    public DataNamingController(FXMLLoaderService loaderService,
                                ObservableList<String> criteria, ObservableList<String> alternatives) throws IOException {
        this.criteria = criteria;
        this.alternatives = alternatives;

        this.node = loaderService.loadController(DataNamingController.class.getResource("DataNaming.fxml"), this);
    }

    @FXML
    private void initialize() {
        criteriaListView.setItems(criteria);
        alternativesListView.setItems(alternatives);

        criteriaListView.cellFactoryProperty().set(TextFieldListCell.forListView());
        alternativesListView.cellFactoryProperty().set(TextFieldListCell.forListView());

        criteriaListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        alternativesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        criteriaListView.setEditable(true);
        alternativesListView.setEditable(true);

        addCriteria.setOnAction(event -> addCriteria());
        addAlternative.setOnAction(event -> addAlternative());

        removeCriteria.disableProperty().bind(Bindings.isEmpty(criteria));
        removeAlternative.disableProperty().bind(Bindings.isEmpty(alternatives));

        removeCriteria.setOnAction(event -> removeCriteria());
        removeAlternative.setOnAction(event -> removeAlternatives());
    }

    private void removeAlternatives() {
        ObservableList<Integer> indices = alternativesListView.getSelectionModel().getSelectedIndices();
        ListIterator<Integer> iterator = indices.listIterator(indices.size());
        while (iterator.hasPrevious()) {
            alternatives.remove((int) iterator.previous());
        }
    }

    private void removeCriteria() {
        ObservableList<Integer> indices = criteriaListView.getSelectionModel().getSelectedIndices();
        ListIterator<Integer> iterator = indices.listIterator(indices.size());
        while (iterator.hasPrevious()) {
            criteria.remove((int) iterator.previous());
        }
    }

    private void addCriteria() {
        String criteria = "some criteria";
        this.criteria.add(criteria);
    }

    private void addAlternative() {
        String alternative = "some alternative";
        alternatives.add(alternative);
    }

    @Override
    public Node node() {
        return node;
    }

    @Override
    public <T extends PageController> T self(Class<T> tClass) {
        if (tClass != DataNamingController.class) {
            throw new IllegalArgumentException();
        }

        return self();
    }

    @SuppressWarnings("unchecked")
    private <T extends PageController> T self() {
        return (T) this;
    }

}
