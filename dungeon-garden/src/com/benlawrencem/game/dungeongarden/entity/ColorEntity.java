package com.benlawrencem.game.dungeongarden.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.benlawrencem.game.dungeongarden.collision.Area;
import com.benlawrencem.game.dungeongarden.level.Level;

public class ColorEntity extends Entity {
	private Color color;

	public ColorEntity(Level level, float x, float y, Area hitBox, Color color) {
		super(level, x, y, hitBox);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void update(int delta) {
		
	}

	@Override
	public void render(Graphics g) {
		if(getHitBox() != null)
			getHitBox().render(g, color);
	}
}