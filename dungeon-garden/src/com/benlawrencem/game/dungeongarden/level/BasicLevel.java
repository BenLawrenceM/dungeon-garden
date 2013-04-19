package com.benlawrencem.game.dungeongarden.level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.newdawn.slick.Graphics;

import com.benlawrencem.game.dungeongarden.entity.Entity;

public abstract class BasicLevel implements Level {
	private List<Entity> walls;
	private List<Entity> players;
	private List<Entity> playerProjectiles;
	private List<Entity> enemies;
	private List<Entity> enemyProjectiles;

	@Override
	public void init() {
		walls = new ArrayList<Entity>();
		players = new ArrayList<Entity>();
		playerProjectiles = new ArrayList<Entity>();
		enemies = new ArrayList<Entity>();
		enemyProjectiles = new ArrayList<Entity>();
	}

	public void addWall(Entity wall) {
		walls.add(wall);
	}

	public void addPlayer(Entity player) {
		players.add(player);
	}

	public void addPlayerProjectile(Entity projectile) {
		playerProjectiles.add(projectile);
	}

	public void addEnemy(Entity enemy) {
		enemies.add(enemy);
	}

	public void addEnemyProjectile(Entity projectile) {
		enemyProjectiles.add(projectile);
	}

	@Override
	public void update(int delta) {
		//update players
		for(Entity player : players)
			player.update(delta);

		//check player-to-player and player-to-wall collisions
		for(int i = 0; i < players.size(); i++) {
			for(int j = i + 1; j < players.size(); j++)
				players.get(i).checkForCollision(players.get(j));
			for(Entity wall : walls)
				players.get(i).checkForCollision(wall);
		}

		//update enemies
		for(Entity enemy : enemies)
			enemy.update(delta);

		//check enemy-to-enemy and enemy-to-wall collisions and check for hits
		for(int i = 0; i < enemies.size(); i++) {
			for(int j = i + 1; j < enemies.size(); j++)
				enemies.get(i).checkForCollision(enemies.get(j));
			for(Entity wall : walls)
				enemies.get(i).checkForCollision(wall);
			for(Entity player : players)
				enemies.get(i).checkForHit(player);
		}

		//update player projectiles and check for hits
		for(Entity projectile : playerProjectiles) {
			projectile.update(delta);
			for(Entity wall : walls)
				projectile.checkForHit(wall);
			for(Entity enemy : enemies)
				projectile.checkForHit(enemy);
		}

		//update enemy projectiles and check for hits
		for(Entity projectile : enemyProjectiles) {
			projectile.update(delta);
			for(Entity wall : walls)
				projectile.checkForHit(wall);
			for(Entity player : players)
				projectile.checkForHit(player);
		}
	}

	@Override
	public void render(Graphics g) {
		Entity[] arr = getAllEntitiesByRenderOrder();
		for(int i = 0; i < arr.length; i++)
			arr[i].render(g);
	}

	private Entity[] getAllEntitiesByRenderOrder() {
		Entity[] arr = new Entity[walls.size() + players.size() + enemies.size() + playerProjectiles.size() + enemyProjectiles.size()];
		for(int i = 0; i < walls.size(); i++)
			arr[i] = walls.get(i);
		int j = walls.size();
		for(int i = 0; i < players.size(); i++)
			arr[i + j] = players.get(i);
		j += players.size();
		for(int i = 0; i < enemies.size(); i++)
			arr[i + j] = enemies.get(i);
		j += enemies.size();
		for(int i = 0; i < playerProjectiles.size(); i++)
			arr[i + j] = playerProjectiles.get(i);
		j += playerProjectiles.size();
		for(int i = 0; i < enemyProjectiles.size(); i++)
			arr[i + j] = enemyProjectiles.get(i);
		j += enemyProjectiles.size();

		Arrays.sort(arr, new Comparator<Entity>() {
			@Override
			public int compare(Entity o1, Entity o2) {
				if(o1.getRenderLayer() != o2.getRenderLayer())
					return o1.getRenderLayer() - o2.getRenderLayer();
				return (o1.getRenderDepth() - o2.getRenderDepth() > 0 ? 1 : -1);
			}
		});

		return arr;
	}

	@Override
	public void keyPressed(int key, char c) {}

	@Override
	public void keyReleased(int key, char c) {}
}