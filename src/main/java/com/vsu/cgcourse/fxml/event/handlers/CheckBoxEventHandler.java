package com.vsu.cgcourse.fxml.event.handlers;

import com.vsu.cgcourse.AppStates;
import com.vsu.cgcourse.model.TransformMesh;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class CheckBoxEventHandler implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        List<TransformMesh> meshList = AppStates.meshList;
        List<TransformMesh> activeMeshList = AppStates.activeMeshList;

        CheckBox checkBox = ((CheckBox) event.getSource());
        String[] name = checkBox.getText().split("\\.");
        int index = Integer.parseInt(name[0]) - 1;

        if (checkBox.isSelected()) {
            activeMeshList.add(meshList.get(index));
        }
        if (!checkBox.isSelected()) {
            activeMeshList.remove(meshList.get(index));
        }
    }
}
