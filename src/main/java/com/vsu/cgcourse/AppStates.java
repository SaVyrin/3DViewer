package com.vsu.cgcourse;

import com.vsu.cgcourse.fxml.states.CurrentTheme;
import com.vsu.cgcourse.fxml.states.State;
import com.vsu.cgcourse.math.vectors.Vector3f;
import com.vsu.cgcourse.model.TransformMesh;
import com.vsu.cgcourse.render_engine.Camera;

import java.util.ArrayList;
import java.util.List;

public abstract class AppStates {
    public static State currState = State.MOVE;
    public static CurrentTheme currentTheme;
    final public static float TRANSLATION = 2F;
    final public static float FPS = 1000f/30f;

    public static final List<TransformMesh> meshList = new ArrayList<>();
    public static final List<TransformMesh> activeMeshList = new ArrayList<>();

    public static final Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);
}
