package com.mygdx.cgoon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Iterator;

public class CaptainGoon extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture background;
	private Ship hero;
	private Array<Projectile> heroFired;
	private Array<EnnemyShip> ennemies;
	private Array<Projectile> ennemiesFired;

	public BitmapFont font;

	private Texture heroProjectile;
	private Texture heroTexture;
	private Texture littleEnnemyTexture;
	private Texture bigEnnemyTexture;
	private Texture ennemiBomb;

	private int width = 1024;
	private int height = 768;

	boolean gameFinished = false;


	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();

		font = new BitmapFont();
		font.getData().setScale(2.5f,2.5f);

		ennemies = new Array<EnnemyShip>();
		heroFired = new Array<Projectile>();
		ennemiesFired = new Array<Projectile>();

		heroProjectile = new Texture("sprites/ammo/bullets/glowtube_small.png");
		ennemiBomb = new Texture("sprites/ammo/wship-4.png");

		background = new Texture("backgrounds/Planets.jpg");
		heroTexture = new Texture("sprites/ships/spco_small.png");
		littleEnnemyTexture = new Texture("sprites/ships/blueships1_small.png");

		hero = new HeroShip(0,0,100,new Sprite(heroTexture));

		bigEnnemyTexture = new Texture("sprites/ships/roundysh_small.png");

		ennemies.add(new EnnemyShip(50,height-200,100,new Sprite(littleEnnemyTexture)));
		ennemies.add(new EnnemyShip(width-50,height-200,100,new Sprite(littleEnnemyTexture)));
		ennemies.add(new EnnemyShip(width/2,height-100,200,new Sprite(bigEnnemyTexture)));

		camera.setToOrtho(false, width, height);
	}

	private boolean updatePositions() {
		float delta = Gdx.graphics.getDeltaTime();
		//Mise à jour du hero par rapport à la souris
		int newPos = Gdx.input.getX();
		if(newPos + hero.getHitBoxX() > width)
			newPos = (int) (width - hero.getHitBoxX());
		hero.setPosX(newPos);

		//Mise à jour des ennemies
		float ennemyDelta = 200 * delta;
		for(EnnemyShip ennemy: ennemies) {
			ennemy.move((int)ennemyDelta, width);
		}

		//Mise à jour des projectiles
		float projectileDelta = 500 * delta;
		for (Iterator<Projectile> iter = heroFired.iterator(); iter.hasNext(); ) {
			Projectile p = iter.next();
			p.setPosY((int)(p.getPosY()+projectileDelta));
			//On supprime les projectiles qui sortent de l'écran
			if(p.getPosY() > height) {
				p.dispose();
				iter.remove();
				continue;
			}
			//Detection de collision avec un ennemi
			for(EnnemyShip s: ennemies) {
				if ((s.getPosX() <= p.getPosX() && p.getPosX() <= s.getPosX()+s.getHitBoxX()) &&
					(p.getPosY()+p.getHitBoxY()>= s.getPosY())) {
						//Collision !!!
						iter.remove();
						if(s.hit(30)) {
							//L'ennemie est mort mon capitaine
							s.dispose();
							ennemies.removeValue(s,true);
							if(ennemies.size ==0)
								return true;
						}
						break;

				}
			}
		}

		//Mise à jour des projectiles enemies
		float dropDelta = 400 * delta;
		for (Iterator<Projectile> iter = ennemiesFired.iterator(); iter.hasNext(); ) {
			Projectile p = iter.next();
			p.setPosY((int)(p.getPosY()-dropDelta));
			//On supprime les projectiles qui sortent de l'écran
			if(p.getPosY() < 0) {
				p.dispose();
				iter.remove();
				continue;
			}
			//Detection de collision avec le héros
			if ((hero.getPosX() <= p.getPosX() && p.getPosX() <= hero.getPosX()+hero.getHitBoxX()) &&
				(p.getPosY() <= hero.getPosY()+hero.getHitBoxY())) {
				//Collision !!!
				iter.remove();
				if (hero.hit(30)) {
					//On est mort mon capitaine
					hero.dispose();
					return true;
				}
			}
		}
		return false;
	}

	private void readClick() {
		if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			heroFired.add(new Projectile(hero.getPosX()+hero.getHitBoxX()/2,
						hero.getHitBoxY(),
						new Sprite(heroProjectile)));
		}
	}

	private void randomEvent() {
		int prob = MathUtils.random(100);
		Ship emmitter = ennemies.random();
		//On veut 2% de probabilité de générer un projectile
		if(prob < 2) {
			ennemiesFired.add(new Projectile(
					emmitter.getPosX()+emmitter.getHitBoxX()/2,
					emmitter.getPosY(),
					new Sprite(ennemiBomb)));
		}
	}

	@Override
	public void render () {
		//Affichage du message de fin
		if(gameFinished) {
			camera.update();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			if(ennemies.size==0) {
				ScreenUtils.clear(0.2f, 1, 0.2f, 1);
				font.draw(batch,"VOUS AVEZ GAGNÉ", 120, height/2);
			} else {
				ScreenUtils.clear(0.2f, 0.2f, 1, 1);
				font.draw(batch,"VOUS AVEZ PERDU", 120, height/2);
			}
			batch.end();
			return;
		}
		readClick();
		randomEvent();
		gameFinished = updatePositions();
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background,0,0);
		//Rendu du héros
		hero.render(batch);
		//Rendu des ennemies
		for(Ship ennemi : ennemies) {
			ennemi.render(batch);
		}
		//Rendu des projectiles
		for(Projectile p: heroFired){
			p.render(batch);
		}
		for(Projectile p: ennemiesFired){
			p.render(batch);
		}
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		heroTexture.dispose();
		littleEnnemyTexture.dispose();
		bigEnnemyTexture.dispose();
		ennemiBomb.dispose();
		font.dispose();
	}
}
