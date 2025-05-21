package com.example.Models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LightHalo {
    private Sprite lightSprite;
    private float scale = 1.0f;
    private float pulseSpeed = 0.5f;
    private float pulseAmount = 0.2f;
    private float pulseTime = 0f;
    private float baseScale;
    private Color lightColor;
    private boolean pulsing = true;

    public LightHalo(Texture texture, float radius, Color color) {
        lightSprite = new Sprite(texture);
        lightSprite.setOriginCenter();

        float size = radius * 2;
        lightSprite.setSize(size, size);

        baseScale = scale;
        lightColor = new Color(color);

        lightSprite.setColor(lightColor);
    }


    public void update(float x, float y, float delta) {
        lightSprite.setPosition(x - lightSprite.getWidth() / 2, y - lightSprite.getHeight() / 2);

        if (pulsing) {
            pulseTime += delta;
            float pulseFactor = (float) Math.sin(pulseTime * pulseSpeed * Math.PI * 2) * pulseAmount;
            lightSprite.setScale(baseScale * (1 + pulseFactor));
        }
    }


    public void render(SpriteBatch batch) {
        lightSprite.draw(batch);
    }


    public void setColor(Color color) {
        lightColor.set(color);
        lightSprite.setColor(lightColor);
    }


    public void setAlpha(float alpha) {
        lightColor.a = alpha;
        lightSprite.setColor(lightColor);
    }


    public void setPulsing(boolean pulsing) {
        this.pulsing = pulsing;
    }


    public void setScale(float scale) {
        this.baseScale = scale;
        if (!pulsing) {
            lightSprite.setScale(scale);
        }
    }


    public void setPulseSpeed(float speed) {
        this.pulseSpeed = speed;
    }


    public void setPulseAmount(float amount) {
        this.pulseAmount = amount;
    }


    public Sprite getLightSprite() {
        return lightSprite;
    }
}
