package com.benlawrencem.game.dungeongarden.entity;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import com.benlawrencem.game.dungeongarden.DungeonGardenGame;
import com.benlawrencem.game.dungeongarden.collision.RectangularArea;
import com.benlawrencem.game.dungeongarden.level.Level;

public class FrostTitan extends MovableEntity {
	private Animation walkUp, walkUpLeft, walkLeft, walkDownLeft,
			walkDown, walkDownRight, walkRight, walkUpRight;
	private float targetX;
	private float targetY;

	private static enum Direction { NONE, UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT };

	public FrostTitan(Level level, float x, float y) {
		super(level, x, y, new RectangularArea(0, 20, 100, 130));
		targetX = x;
		targetY = y;
		try {
			Image img = new Image("images/frosttitan.gif", false, Image.FILTER_NEAREST);
			SpriteSheet ss = new SpriteSheet(img, 64, 64);
			int[] walkDurations = new int[] { 200, 200, 200, 200 };
			walkUp = new Animation(ss, new int[] { 1,1, 2,1, 3,1, 4,1 }, walkDurations);
			walkDown = new Animation(ss, new int[] { 1,0, 2,0, 3,0, 4,0 }, walkDurations);
			walkLeft = new Animation(ss, new int[] { 1,3, 2,3, 3,3, 4,3 }, walkDurations);
			walkRight = new Animation(ss, new int[] { 1,2, 2,2, 3,2, 4,2 }, walkDurations);
			walkUpLeft = new Animation(ss, new int[] { 1,5, 2,5, 3,5, 4,5 }, walkDurations);
			walkDownLeft = new Animation(ss, new int[] { 1,6, 2,6, 3,6, 4,6 }, walkDurations);
			walkUpRight = new Animation(ss, new int[] { 1,4, 2,4, 3,4, 4,4 }, walkDurations);
			walkDownRight = new Animation(ss, new int[] { 1,7, 2,7, 3,7, 4,7 }, walkDurations);
		} catch (SlickException e) {
			
		}
	}

	@Override
	public void update(int delta) {
		if(targetX - 10 < getX() && getX() < targetX + 10 &&
				targetY - 10 < getY() && getY() < targetY + 10) {
			targetX = (float) (150 + Math.random() * (DungeonGardenGame.GAME_WIDTH - 300));
			targetY = (float) (150 + Math.random() * (DungeonGardenGame.GAME_HEIGHT - 300));
			float horizontalDistance = targetX - getX();
			float verticalDistance = targetY - getY();
			float totalDistance = (float) Math.sqrt(horizontalDistance * horizontalDistance + verticalDistance * verticalDistance);
			setVelX(180 * horizontalDistance/totalDistance);
			setVelY(180 * verticalDistance/totalDistance);
		}
		updatePositionBasedOnVelocity(delta);
	}

	private Direction getDirection() {
		float x = getVelX();
		float y = getVelY();
		if(x == 0) {
			if(y == 0)
				return Direction.NONE;
			else if(y > 0)
				return Direction.DOWN;
			else
				return Direction.UP;
		}
		else if(x > 0) {
			if(y == 0)
				return Direction.RIGHT;
			else if(y > 0) {
				if(x / y > 2.414)
					return Direction.RIGHT;
				else if(y / x > 2.414)
					return Direction.DOWN;
				else
					return Direction.DOWN_RIGHT;
			}
			else {
				if(x / -y > 2.414)
					return Direction.RIGHT;
				else if(-y / x > 2.414)
					return Direction.UP;
				else
					return Direction.UP_RIGHT;
			}
		}
		else {
			if(y == 0)
				return Direction.LEFT;
			else if(y > 0) {
				if(-x / y > 2.414)
					return Direction.LEFT;
				else if(y / -x > 2.414)
					return Direction.DOWN;
				else
					return Direction.DOWN_LEFT;
			}
			else {
				if(x / y > 2.414)
					return Direction.LEFT;
				else if(y / x > 2.414)
					return Direction.UP;
				else
					return Direction.UP_LEFT;
			}
		}
	}

	@Override
	public void render(Graphics g) {
		//this.getHitBox().render(g, Color.red);
		Animation anim;
		switch(getDirection()) {
			case LEFT:
				anim = walkLeft;
				break;
			case RIGHT:
				anim = walkRight;
				break;
			case DOWN:
				anim = walkDown;
				break;
			case UP:
				anim = walkUp;
				break;
			case UP_LEFT:
				anim = walkUpLeft;
				break;
			case UP_RIGHT:
				anim = walkUpRight;
				break;
			case DOWN_LEFT:
				anim = walkDownLeft;
				break;
			case DOWN_RIGHT:
				anim = walkDownRight;
				break;
			default:
				anim = walkUp;
				break;
		}
		anim.draw(getX() - 64 * 3, getY() - 64 * 3, 64 * 6, 64 * 6);
	}

	@Override
	public void onHit(Entity other) {
		
	}
}