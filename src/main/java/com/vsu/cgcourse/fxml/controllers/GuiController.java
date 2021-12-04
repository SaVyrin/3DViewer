package com.vsu.cgcourse.fxml.controllers;

import com.vsu.cgcourse.Simple3DViewer;
import com.vsu.cgcourse.math.Vector3f;
import com.vsu.cgcourse.model.Mesh;
import com.vsu.cgcourse.obj_reader.ObjReader;
import com.vsu.cgcourse.render_engine.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuiController {
    @FXML
    private Label currentStateLabel;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private VBox meshCheckBoxes;
    @FXML
    private Canvas canvas;

    final private float TRANSLATION = 2F;
    private State currState = State.MOVE;
    private CurrentTheme currentTheme;

    private final List<Mesh> meshList = new ArrayList<>();
    private final List<Mesh> activeMeshList = new ArrayList<>();

    private final Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        changeToLightTheme();

        anchorPane.setOnKeyPressed(keyEvent -> {
            try {
                Vector3f getVector = keyPressedEvent(keyEvent);
                Vector3f zeroVector = new Vector3f(0,0,0);
                Vector3f unitVector = new Vector3f(1,1,1);
                if (currState == State.SCALE) {
                    scaleRotateTranslate(getVector, zeroVector, zeroVector);
                }
                if (currState == State.ROTATE) {
                    scaleRotateTranslate(unitVector, getVector, zeroVector);
                }
                if (currState == State.TRANSLATE) {
                    scaleRotateTranslate(unitVector, zeroVector, getVector);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(34), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (!meshList.isEmpty()) {
                for (Mesh mesh : meshList) {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        String fileContent = null;
        try {
            fileContent = Files.readString(fileName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        meshList.add(ObjReader.read(fileContent, file.getName()));

            CheckBox checkBox = new CheckBox(meshList.size() + ". " + file.getName());

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
    }

    @FXML
    private void onSaveModelMenuItemClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Simple3DViewer.class.getResource("fxml/saveModal.fxml"));
        Scene modalScene = new Scene(fxmlLoader.load(), 400, 300);

        ModalController controller = fxmlLoader.getController();
        controller.setTheme(currentTheme);
        controller.setMeshList(meshList);

        Stage stage = new Stage();
        stage.setScene(modalScene);
        stage.setTitle("My modal window");
        stage.show();
    }

    @FXML
    private void handleCameraForward() {
        if (currState == State.MOVE) {
            camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
        }
    }

    @FXML
    private void handleCameraBackward() {
        if (currState == State.MOVE) {
            camera.movePosition(new Vector3f(0, 0, TRANSLATION));
        }
    }

    @FXML
    private void handleCameraLeft() {
        if (currState == State.MOVE) {
            camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
        }
    }

    @FXML
    private void handleCameraRight() {
        if (currState == State.MOVE) {
            camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
        }
    }

    @FXML
    private void handleCameraUp() {
        if (currState == State.MOVE) {
            camera.movePosition(new Vector3f(0, TRANSLATION, 0));
        }
    }

    @FXML
    private void handleCameraDown() {
        if (currState == State.MOVE) {
            camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
        }
    }

    @FXML
    private void moveViewTargetLeft() {
        if (currState == State.MOVE) {
            Vector3f target = camera.getTarget();
            target.add(new Vector3f(0.2f, 0f, 0f));
            camera.setTarget(target);
        }
    }

    @FXML
    private void moveViewTargetUp() {
        if (currState == State.MOVE) {
            Vector3f target = camera.getTarget();
            target.add(new Vector3f(0f, 0.2f, 0f));
            camera.setTarget(target);
        }
    }

    @FXML
    private void moveViewTargetRight() {
        if (currState == State.MOVE) {
            Vector3f target = camera.getTarget();
            target.add(new Vector3f(-0.2f, 0f, 0f));
            camera.setTarget(target);
        }
    }

    @FXML
    private void moveViewTargetDown() {
        if (currState == State.MOVE) {
            Vector3f target = camera.getTarget();
            target.add(new Vector3f(0f, -0.2f, 0f));
            camera.setTarget(target);
        }
    }

    @FXML
    private void scale() {
        if (currState != State.SCALE) {
            currState = State.SCALE;
            currentStateLabel.setText("Scale Mode");
        } else {
            currState = State.MOVE;
            currentStateLabel.setText("Move Mode");
        }
    }

    @FXML
    private void rotate() {
        if (currState != State.ROTATE) {
            currState = State.ROTATE;
            currentStateLabel.setText("Rotation Mode");
        } else {
            currState = State.MOVE;
            currentStateLabel.setText("Move Mode");
        }
    }

    @FXML
    private void translate() {
        if (currState != State.TRANSLATE) {
            currState = State.TRANSLATE;
            currentStateLabel.setText("Translation Mode");
        } else {
            currState = State.MOVE;
            currentStateLabel.setText("Move Mode");
        }
    }

    private Vector3f keyPressedEvent(KeyEvent evt) throws IOException {
        if (evt.getCode() == KeyCode.NUMPAD8) {
            if (currState != State.SCALE) {
                return new Vector3f(TRANSLATION, 0, 0);
            }
            return new Vector3f(TRANSLATION, 1, 1);
        }
        if (evt.getCode() == KeyCode.NUMPAD5) {
            if (currState != State.SCALE) {
                return new Vector3f(-TRANSLATION, 0, 0);
            }
            return new Vector3f(1/TRANSLATION, 1, 1);
        }
        if (evt.getCode() == KeyCode.NUMPAD4) {
            if (currState != State.SCALE) {
                return new Vector3f(0, TRANSLATION, 0);
            }
            return new Vector3f(1, TRANSLATION, 1);
        }
        if (evt.getCode() == KeyCode.NUMPAD6) {
            if (currState != State.SCALE) {
                return new Vector3f(0, -TRANSLATION, 0);
            }
            return new Vector3f(1, 1/TRANSLATION, 1);
        }
        if (evt.getCode() == KeyCode.W) {
            if (currState != State.SCALE) {
                return new Vector3f(0, 0, TRANSLATION);
            }
            return new Vector3f(1, 1, TRANSLATION);
        }
        if (evt.getCode() == KeyCode.S) {
            if (currState != State.SCALE) {
                return new Vector3f(0, 0, -TRANSLATION);
            }
            return new Vector3f(1, 1, 1/TRANSLATION);
        }
        if (currState != State.SCALE) {
            return new Vector3f(0, 0, 0);
        }
        return new Vector3f(1, 1, 1);
    }

    private void scaleRotateTranslate(Vector3f scale, Vector3f rotate, Vector3f translate) {
        for (Mesh mesh : activeMeshList) {
            mesh.rotateScaleTranslate = GraphicConveyor.modelMatrix(scale, rotate, translate, mesh).getMatrix();
        }
    }

    @FXML
    private void changeToDarkTheme() {
        if (currentTheme != CurrentTheme.DARK_THEME){
            currentTheme = CurrentTheme.DARK_THEME;
            String css = Objects.requireNonNull(Simple3DViewer.class.getResource("css/dark_theme.css")).toExternalForm();
            anchorPane.getStylesheets().clear();
            anchorPane.getStylesheets().add(css);
            canvas.getGraphicsContext2D().setStroke(Color.WHITE);
        }
    }

    @FXML
    private void changeToLightTheme() {
        if (currentTheme != CurrentTheme.LIGHT_THEME){
            currentTheme = CurrentTheme.LIGHT_THEME;
            String css = Objects.requireNonNull(Simple3DViewer.class.getResource("css/light_theme.css")).toExternalForm();
            anchorPane.getStylesheets().clear();
            anchorPane.getStylesheets().add(css);
            canvas.getGraphicsContext2D().setStroke(Color.BLACK);
        }
    }
}