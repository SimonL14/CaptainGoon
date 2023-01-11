package com.mygdx.cgoon;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Projectile extends Entity {

    public Projectile(int posX, int posY, Sprite sprite) {
        super(posX, posY, sprite);
        sprite.setOrigin(hitBoxX/2,0);
    }
}
