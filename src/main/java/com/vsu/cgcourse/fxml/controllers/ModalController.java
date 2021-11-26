package com.vsu.cgcourse.fxml.controllers;

import com.vsu.cgcourse.Simple3DViewer;
import com.vsu.cgcourse.math.Matrix4f;
import com.vsu.cgcourse.model.Mesh;
import com.vsu.cgcourse.obj_writer.ObjWriter;
import com.vsu.cgcourse.render_engine.CurrentTheme;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModalController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label errorsLabel;
    @FXML
    private VBox meshCheckBoxes;
    @FXML
    private ToggleGroup changes;
    @FXML
    private TextField fileName;

    private final List<Mesh> activeMeshList = new ArrayList<>();


    public void setTheme(CurrentTheme currentTheme) {
        anchorPane.getStylesheets().clear();
        String css = "";
        if (currentTheme == CurrentTheme.DARK_THEME) {
            css = Objects.requireNonNull(Simple3DViewer.class.getResource("css/dark_theme.css")).toExternalForm();
        }
        if (currentTheme == CurrentTheme.LIGHT_THEME){
            css = Objects.requireNonNull(Simple3DViewer.class.getResource("css/light_theme.css")).toExternalForm();
        }
        anchorPane.getStylesheets().add(css);
    }

    public void setMeshList(List<Mesh> meshList) {
        int meshCounter = 1;
        for (Mesh mesh : meshList) {
            CheckBox checkBox = new CheckBox(meshCounter + ". " + mesh.name);

            EventHandler<MouseEvent> checkBoxEventHandler = e -> {
                String[] name = checkBox.getText().split("\\.");
                int index = Integer.parseInt(name[0]) - 1;

                if (checkBox.isSelected()) {
                    activeMeshList.add(meshList.get(index));
                }
                if (!checkBox.isSelected()) {
                    activeMeshList.remove(meshList.get(index));
                }
            };
            checkBox.addEventFilter(MouseEvent.MOUSE_CLICKED, checkBoxEventHandler);
            meshCheckBoxes.getChildren().add(checkBox);
            meshCounter++;
        }
    }

    @FXML
    private void onSaveButtonClick(ActionEvent actionEvent) {
        errorsLabel.setText("");
        if (!activeMeshList.isEmpty()) {
            saveToFile();
        } else {
            errorsLabel.setText("Еще не загрузили модельку");
        }
    }

    private void saveToFile() {
        errorsLabel.setText("");

        RadioButton toggle = (RadioButton) changes.getSelectedToggle();

        if (toggle.getText().equals("Сохранить изменения")) {

            for (Mesh mesh : activeMeshList) {
                Matrix4f matrix4f = new Matrix4f(mesh.rotateScaleTranslate);
                for (int i = 0; i < mesh.vertices.size(); i++) {
                    mesh.vertices.set(i, Matrix4f.multiplyMatrix4ByVector3(matrix4f, mesh.vertices.get(i)));
                }

                mesh.rotateScaleTranslate = new float[][]{
                        {1, 0, 0, 0},
                        {0, 1, 0, 0},
                        {0, 0, 1, 0},
                        {0, 0, 0, 1}
                };
            }
        }

        String fileNameText = fileName.getText();
        if (!fileNameText.equals("")) {
            ObjWriter.write(activeMeshList, fileNameText);
        } else {
            errorsLabel.setText("Не введено название файла");
        }
    }
}
