package io.github.ilnurnasybullin.ahp.controller;

import javafx.scene.Node;

public interface PageController {
    Node node();
    <T extends PageController> T self(Class<T> tClass);
}
