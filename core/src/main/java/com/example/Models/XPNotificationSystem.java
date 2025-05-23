package com.example.Models;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;


public class XPNotificationSystem {
    private Array<XPNotification> notifications;
    private BitmapFont font;

    public XPNotificationSystem() {
        notifications = new Array<>();
        font = new BitmapFont();
        font.getData().setScale(1.5f);
    }


    public void addXPGain(float x, float y, int xpAmount) {
        XPNotification notification = new XPNotification(x, y, xpAmount);
        notifications.add(notification);
    }


    public void update(float deltaTime) {
        Iterator<XPNotification> iterator = notifications.iterator();
        while (iterator.hasNext()) {
            XPNotification notification = iterator.next();
            notification.update(deltaTime);

            if (notification.isFinished()) {
                iterator.remove();
            }
        }
    }


    public void render(SpriteBatch batch) {
        for (XPNotification notification : notifications) {
            notification.render(batch, font);
        }
    }


    public void dispose() {
        if (font != null) {
            font.dispose();
        }
        notifications.clear();
    }


    private static class XPNotification {
        private float x, y;
        private int xpAmount;
        private float timer;
        private float duration = 2.0f;
        private float initialY;

        public XPNotification(float x, float y, int xpAmount) {
            this.x = x;
            this.y = y;
            this.initialY = y;
            this.xpAmount = xpAmount;
            this.timer = 0f;
        }

        public void update(float deltaTime) {
            timer += deltaTime;

            y = initialY + (timer * 50f);
        }

        public void render(SpriteBatch batch, BitmapFont font) {
            if (timer < duration) {
                float alpha = Math.max(0f, 1f - (timer / duration));

                font.setColor(1f, 0.8f, 0f, alpha);

                String text = "+" + xpAmount + " XP";
                font.draw(batch, text, x, y);
            }
        }

        public boolean isFinished() {
            return timer >= duration;
        }
    }
}
