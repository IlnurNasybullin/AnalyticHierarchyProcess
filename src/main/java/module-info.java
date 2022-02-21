module io.github.ilnurnasybullin.ahp {
    requires spring.boot;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires jblas;

    exports io.github.ilnurnasybullin.ahp to javafx.graphics, spring.beans, spring.context;
    exports io.github.ilnurnasybullin.ahp.controller to spring.beans;
    exports io.github.ilnurnasybullin.ahp.service to spring.beans;
    opens io.github.ilnurnasybullin.ahp to spring.core;
    opens io.github.ilnurnasybullin.ahp.controller to javafx.fxml;
}