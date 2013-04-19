package com.benlawrencem.game.dungeongarden.level;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.benlawrencem.game.dungeongarden.collision.CircularArea;
import com.benlawrencem.game.dungeongarden.collision.Area;
import com.benlawrencem.game.dungeongarden.collision.PointArea;
import com.benlawrencem.game.dungeongarden.collision.RectangularArea;
import com.benlawrencem.game.dungeongarden.entity.Entity;

public class CollisionTestLevel implements Level {
	private List<SimpleEntity> entities;
	private List<SimpleEntity> walls;
	private static final boolean USE_FASTER_COLLISION_CHECKING = true; //no noticeable effect on performance

	@Override
	public void init() {
		entities = new ArrayList<SimpleEntity>();
		walls = new ArrayList<SimpleEntity>();

		for(int i = 0; i < 200; i++) {
			entities.add(rollNew());
		}
		for(int i = 0; i < 25; i++) {
			walls.add(rollWall());
		}
	}

	private SimpleEntity rollNew() {
		double r = Math.random();
		float posX = (Math.random() < .5 ? -100 : 900);
		float posY = (Math.random() < .5 ? -100 : 700);
		float velX = 200 * (float) Math.random() - 100;
		float velY = 200 * (float) Math.random() - 100;
		if(r < .45)
			return new SimpleEntity(this, posX, posY, velX, velY,
					new RectangularArea(0, 0, 10 + 50 * (float) Math.random(), 10 + 50 * (float) Math.random()),
					Color.white, false);
		else if(r < .9)
			return new SimpleEntity(this, posX, posY, velX, velY,
					new CircularArea(0, 0, 10 + 30 * (float) Math.random()),
					Color.white, false);
		else
			return new SimpleEntity(this, posX, posY, velX, velY,
					new PointArea(0, 0),
					Color.white, false);
	}

	private SimpleEntity rollWall() {
		double r = Math.random();
		float posX = 50 + 700 * (float) Math.random();
		float posY = 50 + 500 * (float) Math.random();
		float velX = 0;
		float velY = 0;
		if(r < .45)
			return new SimpleEntity(this, posX, posY, velX, velY,
					new RectangularArea(0, 0, 10 + 50 * (float) Math.random(), 10 + 50 * (float) Math.random()),
					Color.gray, true);
		else if(r < .9)
			return new SimpleEntity(this, posX, posY, velX, velY,
					new CircularArea(0, 0, 10 + 30 * (float) Math.random()),
					Color.gray, true);
		else
			return new SimpleEntity(this, posX, posY, velX, velY,
					new PointArea(0, 0),
					Color.gray, true);
	}

	@Override
	public void update(int delta) {
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).update(delta);
			entities.get(i).setColor(Color.white);
			//entities.get(i).applyBounds(0, 800, 0, 600);
			if(entities.get(i).getX() < -100 ||
					entities.get(i).getX() > 900 ||
					entities.get(i).getY() < -100 ||
					entities.get(i).getY() > 700) {
				entities.set(i, rollNew());
			}
		}

		for(int i = 0; i < walls.size(); i++) {
			walls.get(i).setColor(Color.gray);
		}

		for(int i = 0; i < entities.size(); i++) {
			for(int j = i + 1; j < entities.size(); j++) {
				if(USE_FASTER_COLLISION_CHECKING) {
					if(entities.get(i).checkForHitAndCollision(entities.get(j))) {
						entities.get(i).setColor(Color.red);
						entities.get(j).setColor(Color.red);
					}
				}
				else {
					if(entities.get(i).isCollidingWith(entities.get(j))) {
						entities.get(i).setColor(Color.red);
						entities.get(j).setColor(Color.red);
						entities.get(i).checkForHit(entities.get(j), true);
						entities.get(i).checkForCollision(entities.get(j));
					}
				}
			}
		}

		for(int i = 0; i < entities.size(); i++) {
			for(int j = 0; j < walls.size(); j++) {
				if(USE_FASTER_COLLISION_CHECKING) {
					if(entities.get(i).checkForHitAndCollision(walls.get(j))) {
						entities.get(i).setColor(Color.red);
						walls.get(j).setColor(Color.blue);
					}
				}
				else {
					if(entities.get(i).isCollidingWith(walls.get(j))) {
						entities.get(i).setColor(Color.red);
						walls.get(j).setColor(Color.blue);
						entities.get(i).checkForHit(walls.get(j), true);
						entities.get(i).checkForCollision(walls.get(j));
					}
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		for(Entity entity : entities)
			entity.render(g);
		for(Entity wall : walls)
			wall.render(g);
	}

	public static class SimpleEntity extends Entity {
		private float velX;
		private float velY;
		private Color color;
		private boolean isWall;
		private float hitX;
		private float hitY;
		private float collideX;
		private float collideY;

		public SimpleEntity(Level level, float x, float y, float velX, float velY, Area hitBox, Color color, boolean isWall) {
			super(level, x, y);
			setHitAndCollisionArea(hitBox);
			this.velX = velX;
			this.velY = velY;
			this.color = color;
			this.isWall = isWall;
			hitX = 0;
			hitY = 0;
			collideX = 0;
			collideY = 0;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		@Override
		public void update(int delta) {
			adjustX(velX * delta/1000);
			adjustY(velY * delta/1000);
			hitX = 0;
			hitY = 0;
			collideX = 0;
			collideY = 0;
		}

		@Override
		public void render(Graphics g) {
			if(getHitArea() != null)
				getHitArea().render(g, color);
			g.setColor(Color.yellow);
			g.drawLine(getX(), getY(), getX() + hitX, getY() + hitY);
			g.setColor(Color.green);
			g.drawLine(getX() + 1, getY() + 1, getX() + 1 + collideX, getY() + 1 + collideY);
		}

		@Override
		public void onHit(Entity other, float directionX, float directionY) {
			hitX = 20 * directionX;
			hitY = 20 * directionY;
		}

		@Override
		public void onCollision(Entity other, float overlapX, float overlapY) {
			super.onCollision(other, overlapX, overlapY);
			collideX = 40 * overlapX;
			collideY = 40 * overlapY;
		}

		@Override
		public int getCollisionWeightClass() {
			return (isWall ? Entity.COLLISION_WEIGHT_CLASS_IMMOVABLE : Entity.COLLISION_WEIGHT_CLASS_MEDIUM);
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int key, char c) {
		// TODO Auto-generated method stub
		
	}
}