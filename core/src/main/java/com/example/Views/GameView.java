package com.example.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.example.Controllers.GameController;
import com.example.Main;
import com.example.Models.utilities.GameAssetManager;

public class GameView implements Screen, InputProcessor {
    private Stage stage;
    private GameController controller;
    private OrthographicCamera camera;
    private BitmapFont countdownFont;
    private Animation<Texture> heartAnimation;
    private Animation<Texture> blackHeartAnimation;
    private float heartAnimationTime = 0f;
    private BitmapFont healthFont;
    private BitmapFont ammoFont;
    private BitmapFont zombieKillFont;
    private BitmapFont blackHeartFont;
    private BitmapFont xpFont;
    private OrthographicCamera uiCamera;

    private Texture xpBarBackground;
    private Texture xpBarFill;
    private Texture xpBarBorder;
    private Texture elderBarrierTexture;

    private PauseMenuWindow pauseMenuWindow;
    private CheatCodeWindow cheatCodeWindow;
    private boolean isPauseMenuVisible = false;
    private boolean isCheatCodeWindowVisible = false;

    public GameView(GameController controller, Skin skin) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.controller = controller;
        controller.setView(this);
        camera.position.set(controller.getPlayerController().getPlayer().getPosX(), controller.getPlayerController().getPlayer().getPosY(), 0);
        camera.update();

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        heartAnimation = GameAssetManager.getGameAssetManager().heartAnimation();
        blackHeartAnimation = GameAssetManager.getGameAssetManager().blackHeartAnimation();

        healthFont = new BitmapFont();
        healthFont.getData().setScale(2.0f);

        ammoFont = new BitmapFont();
        ammoFont.getData().setScale(2.0f);

        zombieKillFont = new BitmapFont();
        zombieKillFont.getData().setScale(2.0f);

        blackHeartFont = new BitmapFont();
        blackHeartFont.getData().setScale(2.0f);

        xpFont = new BitmapFont();
        xpFont.getData().setScale(1.8f);

        countdownFont = new BitmapFont();
        countdownFont.getData().setScale(3.0f);

        createXPBarTextures();

        createElderBarrierTexture();
    }

    private void createXPBarTextures() {
        Pixmap backgroundPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        backgroundPixmap.setColor(0.2f, 0.2f, 0.2f, 0.8f);
        backgroundPixmap.fill();
        xpBarBackground = new Texture(backgroundPixmap);
        backgroundPixmap.dispose();

        Pixmap fillPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        fillPixmap.setColor(0.2f, 0.6f, 1.0f, 0.9f);
        fillPixmap.fill();
        xpBarFill = new Texture(fillPixmap);
        fillPixmap.dispose();

        Pixmap borderPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        borderPixmap.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        borderPixmap.fill();
        xpBarBorder = new Texture(borderPixmap);
        borderPixmap.dispose();
    }

    private void createElderBarrierTexture() {
        int textureSize = 512;
        Pixmap barrierPixmap = new Pixmap(textureSize, textureSize, Pixmap.Format.RGBA8888);

        barrierPixmap.setColor(0, 0, 0, 0);
        barrierPixmap.fill();

        barrierPixmap.setColor(1.0f, 0.0f, 0.0f, 0.6f);

        int centerX = textureSize / 2;
        int centerY = textureSize / 2;
        int radius = textureSize / 2 - 10;

        for (int thickness = 0; thickness < 8; thickness++) {
            drawCircleOutline(barrierPixmap, centerX, centerY, radius - thickness);
        }

        elderBarrierTexture = new Texture(barrierPixmap);
        barrierPixmap.dispose();
    }

    private void drawCircleOutline(Pixmap pixmap, int centerX, int centerY, int radius) {
        for (int angle = 0; angle < 360; angle += 2) {
            double radian = Math.toRadians(angle);
            int x = (int) (centerX + radius * Math.cos(radian));
            int y = (int) (centerY + radius * Math.sin(radian));

            if (x >= 0 && x < pixmap.getWidth() && y >= 0 && y < pixmap.getHeight()) {
                pixmap.drawPixel(x, y);
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            if (!isPauseMenuVisible && !controller.getEnemyController().isGamePaused()) {
                showPauseMenu();
            } else if (isPauseMenuVisible) {
                hidePauseMenu();
            }
            return true;
        }

        if (keycode == Input.Keys.C) {
            if (!isCheatCodeWindowVisible && !controller.getEnemyController().isGamePaused()) {
                showCheatMenu();
            } else if (isCheatCodeWindowVisible) {
                hideCheatMenu();
            }
            return true;
        }
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

            heartAnimationTime += delta;
        }

        Main.getBatch().setProjectionMatrix(camera.combined);
        Main.getBatch().begin();

        controller.getWorldController().update();
        controller.getPlayerController().renderLight();
        controller.update(delta);

        if (!isPaused) {
            controller.getPlayerController().update();
            controller.getWeaponController().update();
            controller.getEnemyController().update(Gdx.graphics.getDeltaTime());
        } else {
            controller.getPlayerController().getPlayer().getHeroSprite().draw(Main.getBatch());
        }

        controller.getEnemyController().render(Main.getBatch());
        controller.getWeaponController().renderReloadBar(Main.getBatch());

        Main.getBatch().end();

        renderElderBarrier();

        renderHealthAndAmmoUI();

        renderCountdownTimer();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    private void renderCountdownTimer() {
        Main.getBatch().setProjectionMatrix(uiCamera.combined);
        Main.getBatch().begin();

        int totalSeconds = Math.max(0, (int) controller.getTimeRemaining());
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        String timeText = String.format("%02d:%02d", minutes, seconds);

        countdownFont.setColor(1, 1, 1, 1); // white
        countdownFont.draw(Main.getBatch(), timeText, Gdx.graphics.getWidth() / 2f - 40, Gdx.graphics.getHeight() - 50);

        Main.getBatch().end();
    }


    private void renderHealthAndAmmoUI() {
        Main.getBatch().setProjectionMatrix(uiCamera.combined);
        Main.getBatch().begin();

        float currentHealth = controller.getPlayerController().getPlayer().getPlayerHealth();
        int healthAsInt = Math.max(0, (int) Math.ceil(currentHealth));

        int currentAmmo = controller.getWeaponController().getWeapon().getAmmo();
        int maxAmmo = controller.getWeaponController().getWeapon().getWeaponType().getAmmoMax();

        int killCount = controller.getPlayerController().getPlayer().getKillCount();
        int level = controller.getPlayerController().getPlayer().getLevel();
        int currentXP = controller.getPlayerController().getPlayer().getXp();
        int neededXP = 20 * (level + 1);

        float heartX = 30;
        float heartY = Gdx.graphics.getHeight() - 80;
        float healthTextX = heartX + 80;
        float healthTextY = heartY + 40;

        float ammoIconX = 40;
        float ammoIconY = heartY - 80;
        float ammoTextX = ammoIconX + 70;
        float ammoTextY = ammoIconY + 40;

        float zombieKillX = 30;
        float zombieKillY = ammoIconY - 80;
        float zombieKillTextX = zombieKillX + 80;
        float zombieKillTextY = zombieKillY + 40;

        float blackHeartX = 30;
        float blackHeartY = zombieKillY - 80;
        float blackHeartTextX = blackHeartX + 80;
        float blackHeartTextY = blackHeartY + 40;

        float xpBarX = 30;
        float xpBarY = 30;
        float xpBarWidth = 300;
        float xpBarHeight = 20;

        if (heartAnimation != null) {
            Texture heartFrame = heartAnimation.getKeyFrame(heartAnimationTime, true);
            if (heartFrame != null) {
                float heartScale = 2.0f;
                float heartWidth = heartFrame.getWidth() * heartScale;
                float heartHeight = heartFrame.getHeight() * heartScale;
                Main.getBatch().draw(heartFrame, heartX, heartY, heartWidth, heartHeight);
            }
        }

        if (healthFont != null) {
            String healthText = String.valueOf(healthAsInt);
            healthFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            healthFont.draw(Main.getBatch(), healthText, healthTextX, healthTextY);
        }

        Texture ammoIcon = GameAssetManager.getGameAssetManager().getAmmoIcon();
        if (ammoIcon != null) {
            float ammoScale = 2.0f;
            float ammoWidth = ammoIcon.getWidth() * ammoScale;
            float ammoHeight = ammoIcon.getHeight() * ammoScale;
            Main.getBatch().draw(ammoIcon, ammoIconX, ammoIconY, ammoWidth, ammoHeight);
        }

        if (ammoFont != null) {
            String ammoText = currentAmmo + "/" + maxAmmo;
            ammoFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            ammoFont.draw(Main.getBatch(), ammoText, ammoTextX, ammoTextY);
        }

        Texture zombieKill = GameAssetManager.getGameAssetManager().getZombieSkull();
        if (zombieKill != null) {
            float zombieScale = 4.5f;
            float zombieWidth = zombieKill.getWidth() * zombieScale;
            float zombieHeight = zombieKill.getHeight() * zombieScale;
            Main.getBatch().draw(zombieKill, zombieKillX, zombieKillY, zombieWidth, zombieHeight);
        }

        if (zombieKillFont != null) {
            String zombieText = String.valueOf(killCount);
            zombieKillFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            zombieKillFont.draw(Main.getBatch(), zombieText, zombieKillTextX, zombieKillTextY);
        }

        if (blackHeartAnimation != null) {
            Texture heartFrame = blackHeartAnimation.getKeyFrame(heartAnimationTime, true);
            if (heartFrame != null) {
                float heartScale = 1.5f;
                float heartWidth = heartFrame.getWidth() * heartScale;
                float heartHeight = heartFrame.getHeight() * heartScale;
                Main.getBatch().draw(heartFrame, blackHeartX, blackHeartY, heartWidth, heartHeight);
            }
        }

        if (blackHeartFont != null) {
            String blackHeartText = "Level: " + level;
            blackHeartFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            blackHeartFont.draw(Main.getBatch(), blackHeartText, blackHeartTextX, blackHeartTextY);
        }

        renderXPBar(xpBarX, xpBarY, xpBarWidth, xpBarHeight, currentXP, neededXP);

        if (xpFont != null) {
            String xpText = "XP: " + currentXP + "/" + neededXP;
            float xpTextX = xpBarX + (xpBarWidth - xpText.length() * 12) / 2;
            float xpTextY = xpBarY + xpBarHeight + 25;
            xpFont.setColor(1.0f, 0.9f, 0.0f, 1.0f);
            xpFont.draw(Main.getBatch(), xpText, xpTextX, xpTextY);
        }

        Main.getBatch().end();
    }

    private void renderXPBar(float x, float y, float width, float height, int currentXP, int neededXP) {
        try {
            float xpPercentage = neededXP > 0 ? Math.min((float) currentXP / neededXP, 1.0f) : 0.0f;

            Main.getBatch().draw(xpBarBackground, x, y, width, height);

            int segments = 50;
            float segmentWidth = width / segments;
            float fillWidth = width * xpPercentage;

            for (int i = 0; i < segments; i++) {
                float segmentX = x + i * segmentWidth;

                if (segmentX > x + fillWidth) break;

                float t = (float) i / (segments - 1);
                Color color;
                color = hsvToRgb(t * 360f, 1f, 1f);

                Main.getBatch().setColor(color);
                Main.getBatch().draw(xpBarFill, segmentX, y, segmentWidth + 1, height);
            }

            Main.getBatch().setColor(Color.WHITE);

            float borderThickness = 2f;
            Main.getBatch().draw(xpBarBorder, x, y + height - borderThickness, width, borderThickness); // Top
            Main.getBatch().draw(xpBarBorder, x, y, width, borderThickness); // Bottom
            Main.getBatch().draw(xpBarBorder, x, y, borderThickness, height); // Left
            Main.getBatch().draw(xpBarBorder, x + width - borderThickness, y, borderThickness, height);

        } catch (Exception e) {
            System.err.println("Error rendering XP bar: " + e.getMessage());
        }
    }

    private Color hsvToRgb(float h, float s, float v) {
        int i = (int) Math.floor(h / 60f) % 6;
        float f = (h / 60f) - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);

        float r = 0, g = 0, b = 0;

        switch (i) {
            case 0:
                r = v;
                g = t;
                b = p;
                break;
            case 1:
                r = q;
                g = v;
                b = p;
                break;
            case 2:
                r = p;
                g = v;
                b = t;
                break;
            case 3:
                r = p;
                g = q;
                b = v;
                break;
            case 4:
                r = t;
                g = p;
                b = v;
                break;
            case 5:
                r = v;
                g = p;
                b = q;
                break;
        }

        return new Color(r, g, b, 1f);
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        uiCamera.setToOrtho(false, width, height);
        uiCamera.update();
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
        if (controller != null && controller.getWeaponController() != null) {
            controller.getWeaponController().dispose();
        }
        if (pauseMenuWindow != null) {
            pauseMenuWindow.remove();
        }

        if (healthFont != null) {
            healthFont.dispose();
        }
        if (ammoFont != null) {
            ammoFont.dispose();
        }
        if (zombieKillFont != null) {
            zombieKillFont.dispose();
        }
        if (blackHeartFont != null) {
            blackHeartFont.dispose();
        }
        if (xpFont != null) {
            xpFont.dispose();
        }

        if (xpBarBackground != null) {
            xpBarBackground.dispose();
        }
        if (xpBarFill != null) {
            xpBarFill.dispose();
        }
        if (xpBarBorder != null) {
            xpBarBorder.dispose();
        }

        if (elderBarrierTexture != null) {
            elderBarrierTexture.dispose();
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

    private void showPauseMenu() {
        if (pauseMenuWindow != null) {
            pauseMenuWindow.remove();
        }

        controller.getEnemyController().pauseGame();

        pauseMenuWindow = new PauseMenuWindow(
            GameAssetManager.getGameAssetManager().getSkin(),
            controller
        );

        pauseMenuWindow.setOnResume(this::resumeGame);
        pauseMenuWindow.setOnQuit(this::quitToMainMenu);

        stage.addActor(pauseMenuWindow);
        isPauseMenuVisible = true;

        Gdx.input.setInputProcessor(stage);
    }

    public void showCheatMenu() {
        if (cheatCodeWindow != null) {
            cheatCodeWindow.remove();
        }

        controller.getEnemyController().pauseGame();
        cheatCodeWindow = new CheatCodeWindow(GameAssetManager.getGameAssetManager().getSkin(), controller);


        stage.addActor(cheatCodeWindow);
        isCheatCodeWindowVisible = true;

        Gdx.input.setInputProcessor(stage);

    }

    public void hideCheatMenu() {
        if (cheatCodeWindow != null) {
            cheatCodeWindow.remove();
            cheatCodeWindow = null;
        }
        isCheatCodeWindowVisible = false;
        resumeGame();
    }

    private void hidePauseMenu() {
        if (pauseMenuWindow != null) {
            pauseMenuWindow.remove();
            pauseMenuWindow = null;
        }
        isPauseMenuVisible = false;
        resumeGame();
    }

    private void resumeGame() {
        controller.getEnemyController().resumeGame();
        isPauseMenuVisible = false;

        Gdx.input.setInputProcessor(this);

        if (pauseMenuWindow != null) {
            pauseMenuWindow.remove();
            pauseMenuWindow = null;
        }
    }

    private void quitToMainMenu() {
        controller.getEnemyController().navigateToMainMenu();
    }

    private void renderElderBarrier() {
        if (!controller.getEnemyController().isElderBarrierActive()) {
            return;
        }

        try {
            float barrierRadius = controller.getEnemyController().getElderBarrierRadius();
            float screenCenterX = Gdx.graphics.getWidth() / 2f;
            float screenCenterY = Gdx.graphics.getHeight() / 2f;

            Main.getBatch().setProjectionMatrix(uiCamera.combined);
            Main.getBatch().begin();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            Color barrierColor = new Color();
            if (barrierRadius < 200f) {
                barrierColor.set(1f, 0f, 0f, 0.8f); // Bright red when dangerous
            } else {
                barrierColor.set(1f, 0.3f, 0.3f, 0.5f); // Dimmer red when safe
            }

            Main.getBatch().setColor(barrierColor);

            float textureSize = barrierRadius * 2;
            float textureX = screenCenterX - barrierRadius;
            float textureY = screenCenterY - barrierRadius;

            if (elderBarrierTexture != null) {
                Main.getBatch().draw(elderBarrierTexture, textureX, textureY, textureSize, textureSize);
            }

            Main.getBatch().setColor(Color.WHITE);
            Main.getBatch().end();

            Gdx.gl.glDisable(GL20.GL_BLEND);

        } catch (Exception e) {
            System.err.println("Error rendering elder barrier: " + e.getMessage());
        }
    }
}
