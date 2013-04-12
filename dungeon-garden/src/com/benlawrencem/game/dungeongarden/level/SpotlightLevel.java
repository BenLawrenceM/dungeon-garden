package com.benlawrencem.game.dungeongarden.level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.benlawrencem.game.dungeongarden.DungeonGardenGame;
import com.benlawrencem.game.dungeongarden.collision.CircularHitBox;
import com.benlawrencem.game.dungeongarden.collision.HitBox;
import com.benlawrencem.game.dungeongarden.collision.RectangularHitBox;
import com.benlawrencem.game.dungeongarden.entity.Debris;
import com.benlawrencem.game.dungeongarden.entity.Hill;
import com.benlawrencem.game.dungeongarden.entity.Player;

public class SpotlightLevel implements Level {
	private Player currPlayer;
	private List<Player> players;
	private List<Debris> debris;
	private Hill hill;

	@Override
	public void init() {
		currPlayer = new Player(Color.blue, 200, 200);
		players = new ArrayList<Player>();
		players.add(new Player(Color.red, 400, 400));
		players.add(new Player(Color.green, 300, 300));
		debris = new ArrayList<Debris>();
		for(int i = 0; i < 50; i++)
			spawnDebris();
		hill = new Hill(DungeonGardenGame.GAME_WIDTH / 2, DungeonGardenGame.GAME_HEIGHT / 2);
	}

	public void spawnDebris() {
		//float velocityScale = 1.0;
		//Color color, float x, float y, float velX, float velY, HitBox hitBox
		double r = Math.random();
		float x;
		float y;
		float velX;
		float velY;
		float size;
		if(r < 0.25) {
			x = -200;
			y = (float) Math.random() * DungeonGardenGame.GAME_HEIGHT;
			velX = 50 + 150 * (float) Math.random();
			velY = -200 + 400 * (float) Math.random();
		}
		else if(r < 0.50) {
			x = DungeonGardenGame.GAME_WIDTH + 200;
			y = (float) Math.random() * DungeonGardenGame.GAME_HEIGHT;
			velX = -50 - 150 * (float) Math.random();
			velY = -200 + 400 * (float) Math.random();
		}
		else if(r < 0.75) {
			x = (float) Math.random() * DungeonGardenGame.GAME_WIDTH;
			y = -200;
			velX = -200 + 400 * (float) Math.random();
			velY = 50 + 150 * (float) Math.random();
		}
		else {
			x = (float) Math.random() * DungeonGardenGame.GAME_WIDTH;
			y = DungeonGardenGame.GAME_HEIGHT + 200;
			velX = -200 + 400 * (float) Math.random();
			velY = -50 - 150 * (float) Math.random();
		}
		size = 30 + 70 * (float) Math.random();
		if(Math.random() < 0.1) {
			size *= 4;
		}
		if(Math.random() < 0.2) {
			velX *= 2;
		}
		if(Math.random() < 0.2) {
			velY *= 2;
		}
		HitBox hitBox;
		if(Math.random() < 0.5) {
			if(Math.random() < 0.5)
				hitBox = new RectangularHitBox(size, size * (float) Math.max(Math.random(), 0.25));
			else
				hitBox = new RectangularHitBox(size * (float) Math.max(Math.random(), 0.25), size);
		}
		else
			hitBox = new CircularHitBox(size);
		Debris d = new Debris(Color.white, x, y, velX, velY, hitBox);
		debris.add(d);
	}

	@Override
	public void update(int delta) {
		//update hill
		hill.update(delta);

		//update players
		for(Player player : players)
			player.update(delta);
		currPlayer.update(delta);

		//update debris
		for(Debris d : debris)
			d.update(delta);

		//check debris collisions
		for(int i = 0; i < debris.size(); i++) {
			//for(int j = i + 1; j < debris.size(); j++)
			//	debris.get(i).handleCollisionWith(debris.get(j), 0.5f);
			for(int j = 0; j < players.size(); j++)
				debris.get(i).handleCollisionWith(players.get(j), 0f);
			debris.get(i).handleCollisionWith(currPlayer, 0f);
		}

		//check player collisions
		for(int i = 0; i < players.size(); i++) {
			for(int j = i + 1; j < players.size(); j++)
				players.get(i).handleCollisionWith(players.get(j), 0.5f);
			players.get(i).handleCollisionWith(currPlayer, 0.5f);
		}

		//keep players in bounds
		for(Player player : players)
			player.applyBounds(0, DungeonGardenGame.GAME_WIDTH, 0, DungeonGardenGame.GAME_HEIGHT);
		currPlayer.applyBounds(0, DungeonGardenGame.GAME_WIDTH, 0, DungeonGardenGame.GAME_HEIGHT);

		//respawn debris that is out of bounds
		int numToSpawn = 0;
		for(Iterator<Debris> iter = debris.iterator(); iter.hasNext();) {
			Debris d = iter.next();
			if(d.getX() < -400 || d.getX() > DungeonGardenGame.GAME_WIDTH + 400
					|| d.getY() < -400 || d.getY() > DungeonGardenGame.GAME_HEIGHT + 400) {
				iter.remove();
				numToSpawn++;
			}
		}
		for(int i = 0; i < numToSpawn; i++)
			spawnDebris();
	}

	@Override
	public void render(Graphics g) {
		g.setBackground(Color.black);

		//render hill
		hill.render(g);

		//render debris
		for(Debris d : debris)
			d.render(g);

		//render players
		for(Player player : players)
			player.render(g);
		currPlayer.render(g);
	}

	@Override
	public void keyPressed(int key, char c) {
		switch(key) {
			case Input.KEY_W:
				currPlayer.startMovingUp();
				break;
			case Input.KEY_S:
				currPlayer.startMovingDown();
				break;
			case Input.KEY_A:
				currPlayer.startMovingLeft();
				break;
			case Input.KEY_D:
				currPlayer.startMovingRight();
				break;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		switch(key) {
		case Input.KEY_W:
			currPlayer.stopMovingUp();
			break;
		case Input.KEY_S:
			currPlayer.stopMovingDown();
			break;
		case Input.KEY_A:
			currPlayer.stopMovingLeft();
			break;
		case Input.KEY_D:
			currPlayer.stopMovingRight();
			break;
		}
	}
}