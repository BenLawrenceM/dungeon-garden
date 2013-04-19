package com.benlawrencem.game.dungeongarden.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.benlawrencem.game.dungeongarden.collision.CircularArea;
import com.benlawrencem.game.dungeongarden.level.Level;

public class Player extends MovableEntity {
	private static enum Direction { UP, DOWN, LEFT, RIGHT, NONE };

	private int playerId;
	private Direction horizontalDirection;
	private Direction verticalDirection;
	private boolean isAttemptingToMoveUp;
	private boolean isAttemptingToMoveDown;
	private boolean isAttemptingToMoveLeft;
	private boolean isAttemptingToMoveRight;
	private float moveSpeed;
	private float diagonalMoveSpeed;
	private Color color;

	public Player(Level level, Color color) {
		this(level, color, 0, 0);
	}

	public Player(Level level, Color color, float x, float y) {
		super(level, x, y);
		setHitAndCollisionArea(new CircularArea(20));
		playerId = -1;
		this.color = color;
		horizontalDirection = Direction.NONE;
		verticalDirection = Direction.NONE;
		isAttemptingToMoveUp = false;
		isAttemptingToMoveDown = false;
		isAttemptingToMoveLeft = false;
		isAttemptingToMoveRight = false;
		setMoveSpeed(400);
	}

	@Override
	public void update(int delta) {
		if(isMovingUp())
			setVelY((isMovingDiagonally() ? -diagonalMoveSpeed : -moveSpeed));
		else if(isMovingDown())
			setVelY((isMovingDiagonally() ? diagonalMoveSpeed : moveSpeed));
		else
			setVelY(0);
		if(isMovingLeft())
			setVelX((isMovingDiagonally() ? -diagonalMoveSpeed : -moveSpeed));
		else if(isMovingRight())
			setVelX((isMovingDiagonally() ? diagonalMoveSpeed : moveSpeed));
		else
			setVelX(0);
		updatePositionBasedOnVelocity(delta);
	}

	@Override
	public void render(Graphics g) {
		if(getHitArea() != null)
			getHitArea().render(g, color);
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public boolean isMovingUp() {
		return verticalDirection == Direction.UP;
	}

	public void startMovingUp() {
		isAttemptingToMoveUp = true;
		verticalDirection = Direction.UP;
	}

	public void stopMovingUp() {
		isAttemptingToMoveUp = false;
		verticalDirection = (isAttemptingToMoveDown ? Direction.DOWN : Direction.NONE);
	}

	public boolean isMovingDown() {
		return verticalDirection == Direction.DOWN;
	}

	public void startMovingDown() {
		isAttemptingToMoveDown = true;
		verticalDirection = Direction.DOWN;
	}

	public void stopMovingDown() {
		isAttemptingToMoveDown = false;
		verticalDirection = (isAttemptingToMoveUp ? Direction.UP : Direction.NONE);
	}

	public boolean isMovingLeft() {
		return horizontalDirection == Direction.LEFT;
	}

	public void startMovingLeft() {
		isAttemptingToMoveLeft = true;
		horizontalDirection = Direction.LEFT;
	}

	public void stopMovingLeft() {
		isAttemptingToMoveLeft = false;
		horizontalDirection = (isAttemptingToMoveRight ? Direction.RIGHT : Direction.NONE);
	}

	public boolean isMovingRight() {
		return horizontalDirection == Direction.RIGHT;
	}

	public void startMovingRight() {
		isAttemptingToMoveRight = true;
		horizontalDirection = Direction.RIGHT;
	}

	public void stopMovingRight() {
		isAttemptingToMoveRight = false;
		horizontalDirection = (isAttemptingToMoveLeft ? Direction.LEFT : Direction.NONE);
	}

	public boolean isMovingDiagonally() {
		return horizontalDirection != Direction.NONE && verticalDirection != Direction.NONE;
	}

	public float getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
		diagonalMoveSpeed = (float) (moveSpeed/Math.sqrt(2));
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void onHit(Entity other, float directionX, float directionY) {
		
	}
}
