package com.benlawrencem.game.dungeongarden.entity;

import com.benlawrencem.game.dungeongarden.collision.Area;
import com.benlawrencem.game.dungeongarden.level.Level;

public abstract class MovableEntity extends Entity {
	private float velX;
	private float velY;

	public MovableEntity(Level level) {
		super(level);
		setVelocity(0, 0);
	}

	public MovableEntity(Level level, float x, float y) {
		super(level, x, y);
		setVelocity(0, 0);
	}

	public MovableEntity(Level level, Area hitBox) {
		super(level);
		setHitArea(hitBox);
		setCollisionArea(hitBox);
		setVelocity(0, 0);
	}

	public MovableEntity(Level level, float x, float y, Area hitBox) {
		super(level, x, y);
		setHitArea(hitBox);
		setCollisionArea(hitBox);
		setVelocity(0, 0);
	}

	public MovableEntity(Level level, float x, float y, float velX, float velY) {
		super(level, x, y);
		setVelocity(velX, velY);
	}

	public MovableEntity(Level level, float x, float y, float velX, float velY, Area hitBox) {
		super(level, x, y);
		setHitArea(hitBox);
		setCollisionArea(hitBox);
		setVelocity(velX, velY);
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

	public float getVelocity() {
		return (float) Math.sqrt(getSquareVelocity());
	}

	public float getSquareVelocity() {
		return velX * velX + velY * velY;
	}

	public void setVelocity(float velocity) {
		if(velX != 0 || velY != 0) {
			float ratio = velocity / getVelocity();
			velX *= ratio;
			velY *= ratio;
		}
	}

	public void setVelocity(float velX, float velY) {
		this.velX = velX;
		this.velY = velY;
	}

	public void updatePositionBasedOnVelocity(int delta) {
		adjustX(velX * delta/1000);
		adjustY(velY * delta/1000);
	}

	@Override
	public boolean isImmobile() {
		return false;
	}
}