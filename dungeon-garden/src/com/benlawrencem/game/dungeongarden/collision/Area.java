package com.benlawrencem.game.dungeongarden.collision;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.benlawrencem.game.dungeongarden.entity.Entity;

public abstract class Area {
	public static enum Type { POINT, CIRCULAR, RECTANGULAR, POLYGONAL };

	private Entity parent;
	private float offsetX;
	private float offsetY;

	public Area() {
		parent = null;
		this.offsetX = 0;
		this.offsetY = 0;
	}

	public Area(float offsetX, float offsetY) {
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

	public boolean isIntersecting(Area other) {
		boolean intersecting = checkForIntersection(other, false);
		return intersecting;
	}

	public boolean checkForHit(Area other) {
		boolean intersecting = checkForIntersection(other, false);
		if(intersecting) {
			getParent().onHit(other.getParent());
			other.getParent().onHit(getParent());
		}
		return intersecting;
	}

	public boolean checkForCollision(Area other) {
		boolean intersecting = checkForIntersection(other, true);
		return intersecting;
	}

	protected abstract boolean checkForIntersection(Area other, boolean callOnCollisionAfter);

	public abstract Type getType();

	public abstract void render(Graphics g, Color color);

	public abstract float getTop();

	public abstract float getBottom();

	public abstract float getLeft();

	public abstract float getRight();
}