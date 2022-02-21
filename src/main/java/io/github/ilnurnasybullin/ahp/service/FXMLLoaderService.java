package io.github.ilnurnasybullin.ahp.service;

import java.io.IOException;
import java.net.URL;

public interface FXMLLoaderService {
    <T> T loadController(URL location, Object controller) throws IOException;
}
