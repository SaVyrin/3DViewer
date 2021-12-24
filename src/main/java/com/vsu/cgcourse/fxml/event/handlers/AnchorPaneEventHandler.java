package com.vsu.cgcourse.fxml.event.handlers;

import com.vsu.cgcourse.AppStates;
import com.vsu.cgcourse.fxml.states.State;
import com.vsu.cgcourse.math.vectors.Vector3f;
import com.vsu.cgcourse.model.TransformMesh;
import com.vsu.cgcourse.render_engine.GraphicConveyor;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.List;

public class AnchorPaneEventHandler implements EventHandler<KeyEvent> {

    @Override
    public void handle(KeyEvent keyEvent) {
        try {
            Vector3f getVector = keyPressedEvent(keyEvent);
            Vector3f zeroVector = new Vector3f(0, 0, 0);
            Vector3f unitVector = new Vector3f(1, 1, 1);
            if (AppStates.currState == State.SCALE) {
                scaleRotateTranslate(getVector, zeroVector, zeroVector);
            }
            if (AppStates.currState == State.ROTATE) {
                scaleRotateTranslate(unitVector, getVector, zeroVector);
            }
            if (AppStates.currState == State.TRANSLATE) {
                scaleRotateTranslate(unitVector, zeroVector, getVector);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Vector3f keyPressedEvent(KeyEvent evt) throws IOException {
        if (evt.getCode() == KeyCode.NUMPAD8) {
            if (AppStates.currState != State.SCALE) {
                return new Vector3f(AppStates.TRANSLATION, 0, 0);
            }
            return new Vector3f(AppStates.TRANSLATION, 1, 1);
        }
        if (evt.getCode() == KeyCode.NUMPAD5) {
            if (AppStates.currState != State.SCALE) {
                return new Vector3f(-AppStates.TRANSLATION, 0, 0);
            }
            return new Vector3f(0, 1, 1);
        }
        if (evt.getCode() == KeyCode.NUMPAD4) {
            if (AppStates.currState != State.SCALE) {
                return new Vector3f(0, AppStates.TRANSLATION, 0);
            }
            return new Vector3f(1, AppStates.TRANSLATION, 1);
        }
        if (evt.getCode() == KeyCode.NUMPAD6) {
            if (AppStates.currState != State.SCALE) {
                return new Vector3f(0, -AppStates.TRANSLATION, 0);
            }
            return new Vector3f(1, 0, 1);
        }
        if (evt.getCode() == KeyCode.W) {
            if (AppStates.currState != State.SCALE) {
                return new Vector3f(0, 0, AppStates.TRANSLATION);
            }
            return new Vector3f(1, 1, AppStates.TRANSLATION);
        }
        if (evt.getCode() == KeyCode.S) {
            if (AppStates.currState != State.SCALE) {
                return new Vector3f(0, 0, -AppStates.TRANSLATION);
            }
            return new Vector3f(1, 1, 0);
        }
        if (AppStates.currState != State.SCALE) {
            return new Vector3f(0, 0, 0);
        }
        return new Vector3f(1, 1, 1);
    }

    private void scaleRotateTranslate(Vector3f scale, Vector3f rotate, Vector3f translate) {
        List<TransformMesh> activeMeshList = AppStates.activeMeshList;
        for (TransformMesh transformMesh : activeMeshList) {

            Vector3f addScale = new Vector3f(-1,-1,-1);
            addScale.add(scale);
            transformMesh.getScale().add(addScale);
            transformMesh.getRotation().add(rotate);
            transformMesh.getTranslation().add(translate);

            transformMesh.setTransformationMatrix(
                    GraphicConveyor.modelMatrix(
                            transformMesh.getScale(),
                            transformMesh.getRotation(),
                            transformMesh.getTranslation()
                    ).getMatrix()
            );
        }
    }
}
