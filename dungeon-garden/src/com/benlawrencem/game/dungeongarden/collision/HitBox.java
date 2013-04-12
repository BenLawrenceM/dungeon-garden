package com.benlawrencem.game.dungeongarden.collision;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.benlawrencem.game.dungeongarden.entity.Entity;

public abstract class HitBox {
	public static enum Type { POINT, CIRCULAR, RECTANGULAR, POLYGON };

	private Entity parent;
	private float offsetX;
	private float offsetY;

	public HitBox() {
		parent = null;
		this.offsetX = 0;
		this.offsetY = 0;
	}

	public HitBox(float offsetX, float offsetY) {
		parent = null;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public Entity getParent() {
		return parent;
	}

	public void setParent(Entity parent) {
		this.parent = parent;
	}

	public float getX() {
		return offsetX + (parent != null ? parent.getX() : 0);
	}

	public float getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}

	public float getY() {
		return offsetY + (parent != null ? parent.getY() : 0);
	}

	public float getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}

	public abstract boolean isCollidingWith(HitBox other);

	public abstract boolean handleCollisionWith(HitBox other, float dislodgeWeight);

	public abstract Type getType();

	public abstract void render(Graphics g, Color color);

	public abstract float getTop();

	public abstract float getBottom();

	public abstract float getLeft();

	public abstract float getRight();
}