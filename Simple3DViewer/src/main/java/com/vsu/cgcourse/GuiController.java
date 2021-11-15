package com.vsu.cgcourse;

import com.vsu.cgcourse.math.Matrix4f;
import com.vsu.cgcourse.math.Point2f;
import com.vsu.cgcourse.math.Vector3f;
import com.vsu.cgcourse.model.Mesh;
import com.vsu.cgcourse.obj_reader.ObjReader;
import com.vsu.cgcourse.obj_writer.ObjWriter;
import com.vsu.cgcourse.render_engine.Camera;
import com.vsu.cgcourse.render_engine.GraphicConveyor;
import com.vsu.cgcourse.render_engine.RenderEngine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static com.vsu.cgcourse.render_engine.GraphicConveyor.multiplyMatrix4ByVector3;
import static com.vsu.cgcourse.render_engine.GraphicConveyor.vertexToPoint;

public class GuiController {

    final private float TRANSLATION = 0.5F;
    float sc = 1.1f;
    boolean grow = true;
    int count = 0;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    private Mesh mesh = null;

    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (mesh != null) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
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

        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            // todo: обработка ошибок
        } catch (IOException exception) {

        }
    }

    @FXML
    public void onSaveModelMenuItemClick() {
        if (mesh != null) {

            Matrix4f matrix4f = new Matrix4f(mesh.rotateScaleTranslate);
            for (int i = 0; i < mesh.vertices.size(); i++) {
                mesh.vertices.set(i, multiplyMatrix4ByVector3(matrix4f, mesh.vertices.get(i)));
            }

            ObjWriter.write(mesh, "MyModel");
        } else {
            // todo Exception
            System.out.println("Еще не загрузили модельку");
        }
    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }

    @FXML
    public void moveViewTargetLeft(ActionEvent actionEvent) {
        Vector3f target = camera.getTarget();
        target.add(new Vector3f(0.2f, 0f, 0f));
        camera.setTarget(target);
    }

    @FXML
    public void moveViewTargetUp(ActionEvent actionEvent) {
        Vector3f target = camera.getTarget();
        target.add(new Vector3f(0f, 0.2f, 0f));
        camera.setTarget(target);
    }

    @FXML
    public void moveViewTargetRight(ActionEvent actionEvent) {
        Vector3f target = camera.getTarget();
        target.add(new Vector3f(-0.2f, 0f, 0f));
        camera.setTarget(target);
    }

    @FXML
    public void moveViewTargetDown(ActionEvent actionEvent) {
        Vector3f target = camera.getTarget();
        target.add(new Vector3f(0f, -0.2f, 0f));
        camera.setTarget(target);
    }

    @FXML
    public void rotateScaleTranslate(ActionEvent actionEvent) {
        Vector3f scale;
        Vector3f rotate = new Vector3f(0, 0, 3);
        Vector3f translate;
        if (count == 5) {
            grow = !grow;
            count=-1;
        }
        if (grow) {
            translate = new Vector3f(3*sc, 3*sc, 3*sc);
           scale = new Vector3f(sc, sc, sc);
        } else {
            translate = new Vector3f(-3*sc, -3*sc, -3*sc);
            scale = new Vector3f(1/sc, 1/sc, 1/sc);
        }
        mesh.rotateScaleTranslate = GraphicConveyor.modelMatrix(scale, rotate, translate, mesh).getMatrix();
        count++;
    }

    @FXML
    public void scaleByYAxes(ActionEvent actionEvent) {
        mesh.rotateScaleTranslate[1][1] += 0.05;
    }

    @FXML
    public void scaleByZAxes(ActionEvent actionEvent) {
        mesh.rotateScaleTranslate[2][2] += 0.05;
    }
}