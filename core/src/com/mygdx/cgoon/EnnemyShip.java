package com.mygdx.cgoon;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class EnnemyShip extends Ship {
    boolean isGoingRight;

    public EnnemyShip(int posX, int posY, int health, Sprite sprite) {
        super(posX, posY, health, sprite);
        isGoingRight = true;
    }

    void move(int offset, int max) {
        if(isGoingRight) {
            posX = posX + offset;
            //La hitbox n'est pas prise en compte dans la position mais seulement dans la collision
            if (posX + hitBoxX > max) {
                isGoingRight = false;
                //On garde l'offset restant pour avoir une animation fluide
                posX = max - ((posX + hitBoxX) - max) - hitBoxX;
            }
        } else {
            posX = posX - offset;
            if(posX < 0) {
                isGoingRight = true;
                posX = -posX;
            }
        }

    }
}
