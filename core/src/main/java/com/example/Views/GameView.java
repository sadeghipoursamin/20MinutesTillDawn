package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.GameController;
import com.example.Main;

public class GameView implements Screen, InputProcessor {
    private Stage stage;
    private GameController controller;

    private OrthographicCamera camera;

    public GameView(GameController controller, Skin skin) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.controller = controller;
        controller.setView(this);
        camera.position.set(controller.getPlayerController().getPlayer().getPosX(), controller.getPlayerController().getPlayer().getPosY(), 0);
        camera.update();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos);
        controller.getWeaponController().handleWeaponShoot((int) touchPos.x, (int) touchPos.y);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 touchPos = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos);

        controller.getWeaponController().handleWeaponRotation((int) touchPos.x, (int) touchPos.y);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        float playerX = controller.getPlayerController().getPlayer().getPosX();
        float playerY = controller.getPlayerController().getPlayer().getPosY();

        float cameraHalfWidth = camera.viewportWidth / 2.0f;
        float cameraHalfHeight = camera.viewportHeight / 2.0f;

        Texture background = controller.getWorldController().getBackground();
        float clampX = MathUtils.clamp(playerX, cameraHalfWidth, background.getWidth() - cameraHalfWidth);
        float clampY = MathUtils.clamp(playerY, cameraHalfHeight, background.getHeight() - cameraHalfHeight);

        camera.position.set(clampX, clampY, 0);
        camera.update();

        Main.getBatch().setProjectionMatrix(camera.combined);
        Main.getBatch().begin();
        controller.updateGame();
        Main.getBatch().end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Dispose of the controller resources
//        if (controller != null) {
//            if (controller.getEnemyController() != null) {
//                controller.getEnemyController().dispose();
//            }
//            // Dispose other controllers as needed
//        }
//
//        // Dispose of the stage
//        if (stage != null) {
//            stage.dispose();
//        }
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
