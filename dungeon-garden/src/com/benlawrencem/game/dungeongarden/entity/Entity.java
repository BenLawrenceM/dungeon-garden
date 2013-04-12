package com.benlawrencem.game.dungeongarden.entity;

import org.newdawn.slick.Graphics;

import com.benlawrencem.game.dungeongarden.collision.HitBox;

public abstract class Entity {
	private HitBox hitBox;
	private float x;
	private float y;

	public Entity() {
		this(0, 0);
	}

	public Entity(float x, float y) {
		this(0, 0, null);
	}

	public Entity(HitBox hitBox) {
		this(0, 0, hitBox);
	}

	public Entity(float x, float y, HitBox hitBox) {
		this.x = x;
		this.y = y;
		setHitBox(hitBox);
	}

	public float getX() {
		return x;
	}

	public void adjustX(float amt) {
		x += amt;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void adjustY(float amt) {
		y += amt;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public HitBox getHitBox() {
		return hitBox;
	}

	public void setHitBox(HitBox hitBox) {
		if(hitBox != null)
			hitBox.setParent(this);
		this.hitBox = hitBox;
	}

	public boolean isCollidingWith(Entity other) {
		if(getHitBox() != null && other.getHitBox() != null)
			return getHitBox().isCollidingWith(other.getHitBox());
		return false;
	}

	public void handleCollisionWith(Entity other, float dislodgeWeight) {
		if(getHitBox() != null && other.getHitBox() != null)
			getHitBox().handleCollisionWith(other.getHitBox(), dislodgeWeight);
	}

	public void dislodgeFrom(Entity other, float dislodgeX, float dislodgeY) {
		adjustX(dislodgeX);
		adjustY(dislodgeY);
	}

	public abstract void update(int delta);

	public abstract void render(Graphics g);

	public void applyBounds(float left, float right, float top, float bottom) {
		applyLeftBound(left);
		applyRightBound(right);
		applyTopBound(top);
		applyBottomBound(bottom);
	}

	public void applyLeftBound(float left) {
		if(hitBox != null && hitBox.getLeft() < left)
			adjustX(left - hitBox.getLeft());
	}

	public void applyRightBound(float right) {
		if(hitBox != null && hitBox.getRight() > right)
			adjustX(right - hitBox.getRight());
	}

	public void applyTopBound(float top) {
		if(hitBox != null && hitBox.getTop() < top)
			adjustY(top - hitBox.getTop());
	}

	public void applyBottomBound(float bottom) {
		if(hitBox != null && hitBox.getBottom() > bottom)
			adjustY(bottom - hitBox.getBottom());
	}

	public boolean isImmobile() {
		return true;
	}
}