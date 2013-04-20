package com.benlawrencem.game.dungeongarden.entity;

import com.benlawrencem.game.dungeongarden.level.Level;

public abstract class MovableEntity extends Entity {
	public static enum Direction { NONE, UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT };
	private float velX;
	private float velY;
	private Direction lastMovementDirection;

	public MovableEntity(Level level) {
		super(level);
		setVelocity(0, 0);
		lastMovementDirection = Direction.NONE;
	}

	public MovableEntity(Level level, float x, float y) {
		super(level, x, y);
		setVelocity(0, 0);
		lastMovementDirection = Direction.NONE;
	}

	public MovableEntity(Level level, float x, float y, float velX, float velY) {
		super(level, x, y);
		setVelocity(velX, velY);
		lastMovementDirection = Direction.NONE;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
		recalculateMovementDirection();
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
		recalculateMovementDirection();
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
		recalculateMovementDirection();
	}

	public void updatePositionBasedOnVelocity(int delta) {
		adjustX(velX * delta/1000);
		adjustY(velY * delta/1000);
	}

	private void recalculateMovementDirection() {
		if(velX > 0) {
			if(velY > 0) {
				if(velX / velY > 2.414)
					lastMovementDirection = Direction.RIGHT;
				else if(velY / velX > 2.414)
					lastMovementDirection = Direction.DOWN;
				else
					lastMovementDirection = Direction.DOWN_RIGHT;
			}
			else if(velY < 0) {
				if(velX / -velY > 2.414)
					lastMovementDirection = Direction.RIGHT;
				else if(-velY / velX > 2.414)
					lastMovementDirection = Direction.UP;
				else
					lastMovementDirection = Direction.UP_RIGHT;
			}
			else
				lastMovementDirection = Direction.RIGHT;
		}
		else if(velX < 0) {
			if(velY > 0) {
				if(-velX / velY > 2.414)
					lastMovementDirection = Direction.LEFT;
				else if(velY / -velX > 2.414)
					lastMovementDirection = Direction.DOWN;
				else
					lastMovementDirection = Direction.DOWN_LEFT;
			}
			else if(velY < 0) {
				if(velX / velY > 2.414)
					lastMovementDirection = Direction.LEFT;
				else if(velY / velX > 2.414)
					lastMovementDirection = Direction.UP;
				else
					lastMovementDirection = Direction.UP_LEFT;
			}
			else
				lastMovementDirection = Direction.LEFT;
		}
		else {
			if(velY > 0)
				lastMovementDirection = Direction.DOWN;
			else if(velY < 0)
				lastMovementDirection = Direction.UP;
			//velX == 0 && velY == 0 omitted because lastMovementDirection shouldn't be NONE
			// (it stores the LAST movement, even when an entity is stopped in place)
		}
	}

	public Direction getMovementDirection() {
		if(velX == 0 && velY == 0)
			return Direction.NONE;
		return lastMovementDirection;
	}
}