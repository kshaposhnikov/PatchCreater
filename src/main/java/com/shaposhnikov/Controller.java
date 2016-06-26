package com.shaposhnikov;

import com.shaposhnikov.util.Callable;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField sourcePath;
    @FXML
    private TextField targetPath;
    @FXML
    private Button createBt;
    @FXML
    private Button addClassBt;
    @FXML
    private ListView classesForSearch;
    @FXML
    private TextField newClassName;
    @FXML
    private TextArea logText;

    @Override
    public void initialize(final URL location, ResourceBundle resources) {
        final ObservableList classesForSearchItems = classesForSearch.getItems();
        addClassBt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                classesForSearchItems.add(newClassName.getText());
            }
        });

        final PatchConfig loadedConfig = loadConfig();
        injectUIFields(loadedConfig);

        createBt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Callable<String> callable = new Callable<String>() {
                    @Override
                    public void call(String arg) {
                        logText.appendText(arg + "\n");
                    }
                };

                PatchCreater creater = new PatchCreater(callable);

                if (loadedConfig == null) {
                    PatchConfig newConfig = new PatchConfig(sourcePath.getText(), targetPath.getText(), classesForSearch.getItems());
                    creater.create(newConfig);
                } else {
                    creater.create(loadedConfig);
                }
            }
        });
    }

    private void injectUIFields(PatchConfig loadedConfig) {
        if (loadedConfig != null) {
            sourcePath.setText(loadedConfig.getSourcePath());
            targetPath.setText(loadedConfig.getTargetPath());
            classesForSearch.setItems(new ObservableListWrapper(loadedConfig.getClassNames()));
        }
    }

    private PatchConfig loadConfig() {
        try {
            JAXBContext JAXB = JAXBContext.newInstance(PatchConfig.class);
            Unmarshaller unmarshaller = JAXB.createUnmarshaller();
            return (PatchConfig) unmarshaller.unmarshal(new File(System.getProperty("user.dir") + "/patch_creater_config.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }
}
