package com.benlawrencem.game.dungeongarden.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.benlawrencem.game.dungeongarden.collision.HitBox;

public class Debris extends MovableEntity {
	private Color color;

	public Debris(Color color, float x, float y, float velX, float velY, HitBox hitBox) {
		super(x, y, velX, velY, hitBox);
		setColor(color);
	}

	@Override
	public void update(int delta) {
		updatePositionBasedOnVelocity(delta);
	}

	@Override
	public void render(Graphics g) {
		if(getHitBox() != null)
			getHitBox().render(g, color);
	}

	public void setColor(Color color) {
		this.color = color;
	}
}