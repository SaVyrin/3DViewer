package com.vsu.cgcourse.fxml.controllers;

import com.vsu.cgcourse.AppStates;
import com.vsu.cgcourse.Simple3DViewer;
import com.vsu.cgcourse.fxml.event.handlers.AnchorPaneEventHandler;
import com.vsu.cgcourse.fxml.event.handlers.CheckBoxEventHandler;
import com.vsu.cgcourse.fxml.states.CurrentTheme;
import com.vsu.cgcourse.fxml.states.State;
import com.vsu.cgcourse.math.vectors.Vector3f;
import com.vsu.cgcourse.model.TransformMesh;
import com.vsu.cgcourse.obj_reader.ObjReader;
import com.vsu.cgcourse.render_engine.Camera;
import com.vsu.cgcourse.render_engine.RenderEngine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class GuiController {
    @FXML
    private AnchorPane meshProperties;
    @FXML
    private TextField ScaleByX;
    @FXML
    private TextField ScaleByY;
    @FXML
    private TextField ScaleByZ;
    @FXML
    private TextField RotateByX;
    @FXML
    private TextField RotateByY;
    @FXML
    private TextField RotateByZ;
    @FXML
    private TextField TranslateByX;
    @FXML
    private TextField TranslateByY;
    @FXML
    private TextField TranslateByZ;

    @FXML
    private Label currentStateLabel;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private VBox meshCheckBoxes;
    @FXML
    private Canvas canvas;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        changeToLightTheme();

        anchorPane.addEventHandler(KeyEvent.KEY_PRESSED, new AnchorPaneEventHandler());

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(AppStates.FPS), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            AppStates.camera.setAspectRatio((float) (width / height));

            if (AppStates.meshToShowProperties != null){
                meshProperties.setVisible(true);

                TransformMesh transformMesh = AppStates.meshToShowProperties;
                setMeshProperties(
                        transformMesh.getScale(),
                        transformMesh.getRotation(),
                        transformMesh.getTranslation()
                );
            } else {
                meshProperties.setVisible(false);
            }

            List<TransformMesh> meshList = AppStates.meshList;
            Camera camera = AppStates.camera;
            if (!meshList.isEmpty()) {
                for (TransformMesh mesh : meshList) {
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
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        TransformMesh transformMesh = ObjReader.read(fileContent, file.getName());
        List<TransformMesh> meshList = AppStates.meshList;
        meshList.add(transformMesh);

        CheckBox checkBox = new CheckBox(meshList.size() + ". " + file.getName());

        checkBox.addEventFilter(MouseEvent.MOUSE_CLICKED, new CheckBoxEventHandler());
        meshCheckBoxes.getChildren().add(checkBox);
    }

    @FXML
    private void onSaveModelMenuItemClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Simple3DViewer.class.getResource("fxml/saveModal.fxml"));
        Scene modalScene = new Scene(fxmlLoader.load(), 400, 300);

        ModalController controller = fxmlLoader.getController();
        controller.setTheme(AppStates.currentTheme);
        List<TransformMesh> meshList = AppStates.meshList;
        controller.setMeshList(meshList);

        Stage stage = new Stage();
        stage.setScene(modalScene);
        stage.setTitle("My modal window");
        stage.show();
    }

    @FXML
    private void handleCameraForward() {
        if (AppStates.currState == State.MOVE) {
            Camera camera = AppStates.camera;
            camera.movePosition(new Vector3f(0, 0, -AppStates.TRANSLATION));
        }
    }

    @FXML
    private void handleCameraBackward() {
        if (AppStates.currState == State.MOVE) {
            Camera camera = AppStates.camera;
            camera.movePosition(new Vector3f(0, 0, AppStates.TRANSLATION));
        }
    }

    @FXML
    private void handleCameraLeft() {
        if (AppStates.currState == State.MOVE) {
            Camera camera = AppStates.camera;
            camera.movePosition(new Vector3f(AppStates.TRANSLATION, 0, 0));
        }
    }

    @FXML
    private void handleCameraRight() {
        if (AppStates.currState == State.MOVE) {
            Camera camera = AppStates.camera;
            camera.movePosition(new Vector3f(-AppStates.TRANSLATION, 0, 0));
        }
    }

    @FXML
    private void handleCameraUp() {
        if (AppStates.currState == State.MOVE) {
            Camera camera = AppStates.camera;
            camera.movePosition(new Vector3f(0, AppStates.TRANSLATION, 0));
        }
    }

    @FXML
    private void handleCameraDown() {
        if (AppStates.currState == State.MOVE) {
            Camera camera = AppStates.camera;
            camera.movePosition(new Vector3f(0, -AppStates.TRANSLATION, 0));
        }
    }

    @FXML
    private void moveViewTargetLeft() {
        if (AppStates.currState == State.MOVE) {
            Camera camera = AppStates.camera;
            Vector3f target = camera.getTarget();
            target.add(new Vector3f(0.2f, 0f, 0f));
            camera.setTarget(target);
        }
    }

    @FXML
    private void moveViewTargetUp() {
        if (AppStates.currState == State.MOVE) {
            Camera camera = AppStates.camera;
            Vector3f target = camera.getTarget();
            target.add(new Vector3f(0f, 0.2f, 0f));
            camera.setTarget(target);
        }
    }

    @FXML
    private void moveViewTargetRight() {
        if (AppStates.currState == State.MOVE) {
            Camera camera = AppStates.camera;
            Vector3f target = camera.getTarget();
            target.add(new Vector3f(-0.2f, 0f, 0f));
            camera.setTarget(target);
        }
    }

    @FXML
    private void moveViewTargetDown() {
        if (AppStates.currState == State.MOVE) {
            Camera camera = AppStates.camera;
            Vector3f target = camera.getTarget();
            target.add(new Vector3f(0f, -0.2f, 0f));
            camera.setTarget(target);
        }
    }

    @FXML
    private void scale() {
        if (AppStates.currState != State.SCALE) {
            AppStates.currState = State.SCALE;
            currentStateLabel.setText("Scale Mode");
        } else {
            AppStates.currState = State.MOVE;
            currentStateLabel.setText("Move Mode");
        }
    }

    @FXML
    private void rotate() {
        if (AppStates.currState != State.ROTATE) {
            AppStates.currState = State.ROTATE;
            currentStateLabel.setText("Rotation Mode");
        } else {
            AppStates.currState = State.MOVE;
            currentStateLabel.setText("Move Mode");
        }
    }

    @FXML
    private void translate() {
        if (AppStates.currState != State.TRANSLATE) {
            AppStates.currState = State.TRANSLATE;
            currentStateLabel.setText("Translation Mode");
        } else {
            AppStates.currState = State.MOVE;
            currentStateLabel.setText("Move Mode");
        }
    }

    @FXML
    private void changeToDarkTheme() {
        if (AppStates.currentTheme != CurrentTheme.DARK_THEME) {
            AppStates.currentTheme = CurrentTheme.DARK_THEME;
            String css = Objects.requireNonNull(Simple3DViewer.class.getResource("css/dark_theme.css")).toExternalForm();
            anchorPane.getStylesheets().clear();
            anchorPane.getStylesheets().add(css);
            canvas.getGraphicsContext2D().setStroke(Color.WHITE);
        }
    }

    @FXML
    private void changeToLightTheme() {
        if (AppStates.currentTheme != CurrentTheme.LIGHT_THEME) {
            AppStates.currentTheme = CurrentTheme.LIGHT_THEME;
            String css = Objects.requireNonNull(Simple3DViewer.class.getResource("css/light_theme.css")).toExternalForm();
            anchorPane.getStylesheets().clear();
            anchorPane.getStylesheets().add(css);
            canvas.getGraphicsContext2D().setStroke(Color.BLACK);
        }
    }

    public void setMeshProperties(Vector3f scale, Vector3f rotate, Vector3f translate){
        ScaleByX.setText(Float.toString(scale.getX()));
        ScaleByY.setText(Float.toString(scale.getY()));
        ScaleByZ.setText(Float.toString(scale.getZ()));
        RotateByX.setText(Float.toString(rotate.getX()));
        RotateByY.setText(Float.toString(rotate.getY()));
        RotateByZ.setText(Float.toString(rotate.getZ()));
        TranslateByX.setText(Float.toString(translate.getX()));
        TranslateByY.setText(Float.toString(translate.getY()));
        TranslateByZ.setText(Float.toString(translate.getZ()));
    }
}