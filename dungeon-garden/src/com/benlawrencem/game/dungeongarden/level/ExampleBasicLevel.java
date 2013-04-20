package com.benlawrencem.game.dungeongarden.level;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.benlawrencem.game.dungeongarden.DungeonGardenGame;
import com.benlawrencem.game.dungeongarden.collision.RectangularArea;
import com.benlawrencem.game.dungeongarden.entity.Entity;
import com.benlawrencem.game.dungeongarden.entity.MovableEntity;

public class ExampleBasicLevel extends BasicLevel {
	private ExamplePlayer player;

	@Override
	public void init() {
		super.init();
		for(int i = 0; i < 30; i++)
			addEnemyProjectile(new ExampleArrow(this,
					(float) Math.random() * DungeonGardenGame.GAME_WIDTH,
					(float) Math.random() * DungeonGardenGame.GAME_HEIGHT,
					(Math.random() > 0.5)));
		for(int i = 0; i < 12; i++)
			addWall(new ExampleWall(this,
					(float) Math.random() * DungeonGardenGame.GAME_WIDTH,
					(float) Math.random() * DungeonGardenGame.GAME_HEIGHT));
		player = new ExamplePlayer(this, 400, 300);
		addPlayer(player);
	}

	@Override
	public void keyPressed(int key, char c) {
		switch(key) {
			case Input.KEY_W:
				player.startMovingUp();
				break;
			case Input.KEY_S:
				player.startMovingDown();
				break;
			case Input.KEY_A:
				player.startMovingLeft();
				break;
			case Input.KEY_D:
				player.startMovingRight();
				break;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		switch(key) {
			case Input.KEY_W:
				player.stopMovingUp();
				break;
			case Input.KEY_S:
				player.stopMovingDown();
				break;
			case Input.KEY_A:
				player.stopMovingLeft();
				break;
			case Input.KEY_D:
				player.stopMovingRight();
				break;
			}
	}

	private static class ExampleArrow extends MovableEntity {
		public ExampleArrow(Level level, float x, float y, boolean facingLeft) {
			super(level, x, y, (facingLeft ? -300 : 300), 0);
			setHitArea(new RectangularArea(30, 10));
		}

		@Override
		public void onHit(Entity other, float directionX, float directionY) {
			setHitArea(null);
		}

		@Override
		public void update(int delta) {
			updatePositionBasedOnVelocity(delta);
			if(getX() < -30 || getX() > DungeonGardenGame.GAME_WIDTH + 30) {
				setVelX(-getVelX());
				if(getHitArea() == null)
					setHitArea(new RectangularArea(30, 10));
				setY(DungeonGardenGame.GAME_HEIGHT * (float) Math.random());
			}
		}

		@Override
		public void render(Graphics g) {
			if(getHitArea() != null)
				getHitArea().render(g, Color.red);
		}
	}

	private static class ExampleWall extends Entity {
		public ExampleWall(Level level, float x, float y) {
			super(level, x, y);
			setHitAndCollisionArea(new RectangularArea(60, 60));
		}

		@Override
		public void onHit(Entity other, float directionX, float directionY) {}

		@Override
		public void update(int delta) {}

		@Override
		public void render(Graphics g) {
			getCollisionArea().render(g, Color.blue);
		}

		@Override
		public int getRenderLayer() {
			return Entity.RENDER_LAYER_BACKGROUND;
		}

		@Override
		public int getCollisionWeightClass() {
			return Entity.COLLISION_WEIGHT_CLASS_IMMOVABLE;
		}
	}

	private static class ExamplePlayer extends MovableEntity {
		private static enum Direction { UP, DOWN, LEFT, RIGHT, NONE };

		private Direction horizontalDirection;
		private Direction verticalDirection;
		private boolean isAttemptingToMoveUp;
		private boolean isAttemptingToMoveDown;
		private boolean isAttemptingToMoveLeft;
		private boolean isAttemptingToMoveRight;
		private float moveSpeed;
		private float diagonalMoveSpeed;
		private int invincibilityFrames;
		private int flinchFrames;

		public ExamplePlayer(Level level, float x, float y) {
			super(level, x, y);
			setHitAndCollisionArea(new RectangularArea(30, 30));
			invincibilityFrames = 0;
			flinchFrames = 0;
			setMoveSpeed(200);
		}

		@Override
		public void onHit(Entity other, float directionX, float directionY) {
			if(invincibilityFrames == 0) {
				invincibilityFrames = 1500;
				flinchFrames = 300;
				setVelX(150 * -directionX);
				setVelY(150 * -directionY);
			}
		}

		@Override
		public void update(int delta) {
			if(invincibilityFrames > 0)
				invincibilityFrames = (invincibilityFrames < delta ? 0 : invincibilityFrames - delta);
			if(flinchFrames > 0)
				flinchFrames = (flinchFrames < delta ? 0 : flinchFrames - delta);
			else {
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
			}
			this.updatePositionBasedOnVelocity(delta);
		}

		@Override
		protected boolean canHit(Entity other) {
			if(invincibilityFrames > 0)
				return false;
			return super.canHit(other);
		}

		@Override
		public void render(Graphics g) {
			if(flinchFrames > 0)
				getHitArea().render(g, Color.red);
			else if(invincibilityFrames > 0)
				getHitArea().render(g, Color.cyan);
			else
				getHitArea().render(g, Color.yellow);
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

		public void setMoveSpeed(float moveSpeed) {
			this.moveSpeed = moveSpeed;
			diagonalMoveSpeed = (float) (moveSpeed/Math.sqrt(2));
		}
	}
}