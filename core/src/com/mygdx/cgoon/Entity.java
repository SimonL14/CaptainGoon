package com.mygdx.cgoon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Entity {
    int posX;
    int posY;
    int hitBoxY;
    int hitBoxX;
    Sprite sprite;

    public Entity(int posX, int posY, Sprite sprite) { //une entité est un objet qui se déplace à l'écran
        this.posX = posX;
        this.posY = posY;
        this.sprite = sprite;
        this.sprite.setPosition(posX,posY);
        this.hitBoxX = (int) sprite.getBoundingRectangle().width;
        this.hitBoxY = (int) sprite.getBoundingRectangle().height;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getHitBoxY() {
        return hitBoxY;
    }

    public int getHitBoxX() {
        return hitBoxX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosX() {
        return posX;
    }

    void render(Batch batch) {
        this.sprite.setPosition(posX,posY);
        sprite.draw(batch);
    }

    void dispose() {
    }
}
