package com.benlawrencem.game.dungeongarden.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.benlawrencem.game.dungeongarden.DungeonGardenGame;
import com.benlawrencem.game.dungeongarden.collision.CircularHitBox;

public class Hill extends MovableEntity {
	private float targetX;
	private float targetY;

	public Hill(float x, float y) {
		super(x, y, 0, 0, new CircularHitBox(50));
		targetX = x;
		targetY = y;
	}

	@Override
	public void update(int delta) {
		if(-10 < targetX - getX() && targetX - getX() < 10
				&& -10 < targetY - getY() && targetY - getY() < 10) {
			targetX = 100 + (float) Math.random() * (DungeonGardenGame.GAME_WIDTH - 100);
			targetY = 100 + (float) Math.random() * (DungeonGardenGame.GAME_HEIGHT - 100);
			float horizontalDistance = targetX - getX();
			float verticalDistance = targetY - getY();
			float distance = (float) Math.sqrt(horizontalDistance * horizontalDistance + verticalDistance * verticalDistance);
			float xNorm = horizontalDistance / distance;
			float yNorm = verticalDistance / distance;
			float speed = 90 + 250 * (float) Math.random();
			setVelX(speed * xNorm);
			setVelY(speed * yNorm);
		}
		updatePositionBasedOnVelocity(delta);
	}

	@Override
	public void render(Graphics g) {
		if(getHitBox() != null)
			getHitBox().render(g, Color.yellow);
	}
}