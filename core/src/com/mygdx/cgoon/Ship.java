package com.mygdx.cgoon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Ship extends Entity {
    float health;
    private float maxHealth;

    public Ship(int posX, int posY, int health, Sprite sprite) {
        super(posX,posY,sprite);
        this.maxHealth = health;
        this.health = maxHealth;
    }

    boolean hit(int damage) {
        health -= damage;
        if(health < 0)
            return true;
        return false;
    }

    @Override
    void render(Batch batch) {
        this.sprite.setColor(1,health/maxHealth,health/maxHealth,1);
        super.render(batch);
    }

}
