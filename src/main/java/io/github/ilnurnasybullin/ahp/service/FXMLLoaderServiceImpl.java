package io.github.ilnurnasybullin.ahp.service;

import javafx.fxml.FXMLLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class FXMLLoaderServiceImpl implements FXMLLoaderService {
    @Override
    public <T> T loadController(URL location, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(location);
        loader.setController(controller);
        return loader.load();
    }
}
