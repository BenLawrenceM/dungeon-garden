package com.benlawrencem.game.dungeongarden.level;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.benlawrencem.game.dungeongarden.DungeonGardenGame;
import com.benlawrencem.game.dungeongarden.collision.RectangularArea;
import com.benlawrencem.game.dungeongarden.entity.Entity;
import com.benlawrencem.game.dungeongarden.entity.MovableEntity;

public class ExampleBasicLevel extends BasicLevel {
	@Override
	public void init() {
		super.init();
		for(int i = 0; i < 30; i++)
			addEnemyProjectile(new ExampleArrow(this,
					(float) Math.random() * DungeonGardenGame.GAME_WIDTH,
					(float) Math.random() * DungeonGardenGame.GAME_HEIGHT,
					(Math.random() > 0.5)));
		for(int i = 0; i < 12; i++)
			addEnemyProjectile(new ExampleWall(this,
					(float) Math.random() * DungeonGardenGame.GAME_WIDTH,
					(float) Math.random() * DungeonGardenGame.GAME_HEIGHT));
	}

	private static class ExampleArrow extends MovableEntity {
		private Color color;

		public ExampleArrow(Level level, float x, float y, boolean facingLeft) {
			super(level, x, y, (facingLeft ? -80 : 80), 0);
			setHitArea(new RectangularArea(30, 10));
			color = Color.red;
		}

		@Override
		public void onHit(Entity other, float directionX, float directionY) {
			color = Color.gray;
		}

		@Override
		public void update(int delta) {
			updatePositionBasedOnVelocity(delta);
			if(getX() < 0 || getX() > DungeonGardenGame.GAME_WIDTH) {
				setVelX(-getVelX());
				color = Color.red;
			}
		}

		@Override
		public void render(Graphics g) {
			getHitArea().render(g, color);
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
	}
}