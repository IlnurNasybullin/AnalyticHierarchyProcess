package io.github.ilnurnasybullin.ahp;

import io.github.ilnurnasybullin.ahp.controller.MainController;
import io.github.ilnurnasybullin.ahp.domain.QualitativeComparison;
import io.github.ilnurnasybullin.ahp.service.FXMLLoaderService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

public class JavaFXApp extends Application {
    private ConfigurableApplicationContext applicationContext;
    private FXMLLoaderService fxmlLoaderService;

    @Override
    public void init() throws Exception {
        super.init();
        String[] args = getParameters().getRaw().toArray(String[]::new);
        applicationContext = new SpringApplicationBuilder()
                .sources(App.class)
                .run(args);

        fxmlLoaderService = applicationContext.getBean(FXMLLoaderService.class);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainController controller = applicationContext.getBean(MainController.class);
        Parent root = fxmlLoaderService.loadController(MainController.class.getResource("Main.fxml"), controller);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle("Main App");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        applicationContext.close();
        Platform.exit();
    }
}
