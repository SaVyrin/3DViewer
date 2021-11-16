package com.vsu.cgcourse;

import com.vsu.cgcourse.math.Matrix4f;
import com.vsu.cgcourse.math.Vector3f;
import com.vsu.cgcourse.model.Mesh;
import com.vsu.cgcourse.obj_reader.ObjReader;
import com.vsu.cgcourse.obj_writer.ObjWriter;
import com.vsu.cgcourse.render_engine.Camera;
import com.vsu.cgcourse.render_engine.GraphicConveyor;
import com.vsu.cgcourse.render_engine.RenderEngine;
import com.vsu.cgcourse.render_engine.State;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.vsu.cgcourse.render_engine.GraphicConveyor.multiplyMatrix4ByVector3;


public class GuiController {

    final private float TRANSLATION = 2F;
    private State currState = State.MOVE;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private VBox mesh_list;

    @FXML
    private Canvas canvas;

    private final List<Mesh> meshList = new ArrayList<>();
    private final List<Mesh> activeMeshList = new ArrayList<>();

    private final Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        anchorPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
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
            }
        });

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
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

        try {
            String fileContent = Files.readString(fileName);
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
            mesh_list.getChildren().add(checkBox);
            // todo: обработка ошибок
        } catch (IOException exception) {

        }
    }

    @FXML
    public void onSaveModelMenuItemClick() {
        if (!meshList.isEmpty()) {
            for (Mesh mesh : meshList) {
                Matrix4f matrix4f = new Matrix4f(mesh.rotateScaleTranslate);
                for (int i = 0; i < mesh.vertices.size(); i++) {
                    mesh.vertices.set(i, multiplyMatrix4ByVector3(matrix4f, mesh.vertices.get(i)));
                }
            }

            ObjWriter.write(meshList, "MyModel");
        } else {
            // todo Exception
            System.out.println("Еще не загрузили модельку");
        }
    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        if (currState == State.MOVE) {
            camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
        }
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        if (currState == State.MOVE) {
            camera.movePosition(new Vector3f(0, 0, TRANSLATION));
        }
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        if (currState == State.MOVE) {
            camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
        }
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        if (currState == State.MOVE) {
            camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
        }
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        if (currState == State.MOVE) {
            camera.movePosition(new Vector3f(0, TRANSLATION, 0));
        }
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        if (currState == State.MOVE) {
            camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
        }
    }

    @FXML
    public void moveViewTargetLeft(ActionEvent actionEvent) {
        if (currState == State.MOVE) {
            Vector3f target = camera.getTarget();
            target.add(new Vector3f(0.2f, 0f, 0f));
            camera.setTarget(target);
        }
    }

    @FXML
    public void moveViewTargetUp(ActionEvent actionEvent) {
        if (currState == State.MOVE) {
            Vector3f target = camera.getTarget();
            target.add(new Vector3f(0f, 0.2f, 0f));
            camera.setTarget(target);
        }
    }

    @FXML
    public void moveViewTargetRight(ActionEvent actionEvent) {
        if (currState == State.MOVE) {
            Vector3f target = camera.getTarget();
            target.add(new Vector3f(-0.2f, 0f, 0f));
            camera.setTarget(target);
        }
    }

    @FXML
    public void moveViewTargetDown(ActionEvent actionEvent) {
        if (currState == State.MOVE) {
            Vector3f target = camera.getTarget();
            target.add(new Vector3f(0f, -0.2f, 0f));
            camera.setTarget(target);
        }
    }

    @FXML
    public void scale(ActionEvent actionEvent) {
        if (currState != State.SCALE) {
            currState = State.SCALE;
        } else {
            currState = State.MOVE;
        }
    }

    @FXML
    public void rotate(ActionEvent actionEvent) {
        if (currState != State.ROTATE) {
            currState = State.ROTATE;
        } else {
            currState = State.MOVE;
        }
    }

    @FXML
    public void translate(ActionEvent actionEvent) {
        if (currState != State.TRANSLATE) {
            currState = State.TRANSLATE;
        } else {
            currState = State.MOVE;
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
}