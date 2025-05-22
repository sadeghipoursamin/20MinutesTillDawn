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
        // Only handle shooting if the game is not paused
        if (!controller.getEnemyController().isGamePaused()) {
            Vector3 touchPos = new Vector3(screenX, screenY, 0);
            camera.unproject(touchPos);
            controller.getWeaponController().handleWeaponShoot((int) touchPos.x, (int) touchPos.y);
        }
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
        // Only handle weapon rotation if the game is not paused
        if (!controller.getEnemyController().isGamePaused()) {
            Vector3 touchPos = new Vector3(screenX, screenY, 0);
            camera.unproject(touchPos);
            controller.getWeaponController().handleWeaponRotation((int) touchPos.x, (int) touchPos.y);
        }
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        // Set this view as the input processor initially
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);

        boolean isPaused = controller.getEnemyController().isGamePaused();

        if (!isPaused) {
            float playerX = controller.getPlayerController().getPlayer().getPosX();
            float playerY = controller.getPlayerController().getPlayer().getPosY();

            float cameraHalfWidth = camera.viewportWidth / 2.0f;
            float cameraHalfHeight = camera.viewportHeight / 2.0f;

            Texture background = controller.getWorldController().getBackground();
            float clampX = MathUtils.clamp(playerX, cameraHalfWidth, background.getWidth() - cameraHalfWidth);
            float clampY = MathUtils.clamp(playerY, cameraHalfHeight, background.getHeight() - cameraHalfHeight);

            camera.position.set(clampX, clampY, 0);
            camera.update();
        }

        Main.getBatch().setProjectionMatrix(camera.combined);
        Main.getBatch().begin();

        controller.getWorldController().update();
        controller.getPlayerController().renderLight();

        if (!isPaused) {
            controller.getPlayerController().update();
            controller.getWeaponController().update();
            controller.getEnemyController().update(Gdx.graphics.getDeltaTime());
        } else {
            // When paused, still draw the player sprite but don't update it
            controller.getPlayerController().getPlayer().getHeroSprite().draw(Main.getBatch());
        }

        controller.getEnemyController().render(Main.getBatch());

        Main.getBatch().end();

        // Always update and draw the stage (for UI elements like the level up window)
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        if (stage != null) {
            stage.dispose();
        }
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public GameController getController() {
        return controller;
    }

    public Stage getStage() {
        return stage;
    }
}
