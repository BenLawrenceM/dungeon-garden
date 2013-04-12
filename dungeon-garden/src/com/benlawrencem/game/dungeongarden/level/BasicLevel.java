package com.benlawrencem.game.dungeongarden.level;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.benlawrencem.game.dungeongarden.DungeonGardenGame;
import com.benlawrencem.game.dungeongarden.collision.PolygonHitBox;
import com.benlawrencem.game.dungeongarden.entity.Player;
import com.benlawrencem.game.dungeongarden.entity.ColorEntity;
import com.benlawrencem.game.dungeongarden.net.Client;
import com.benlawrencem.game.dungeongarden.net.ClientListener;
import com.benlawrencem.game.dungeongarden.net.message.Message;
import com.benlawrencem.game.dungeongarden.net.message.Message.Type;
import com.benlawrencem.game.dungeongarden.net.message.PlayerUpdateMessage;

public class BasicLevel implements Level, ClientListener {
	private Player currPlayer;
	private List<Player> players;
	private boolean isConnected;
	private ColorEntity widget;

	@Override
	public void init() {
		currPlayer = new Player(Color.black, 200, 200);
		players = new ArrayList<Player>();
		isConnected = false;
		Client.getInstance().addListener(this);
		Client.getInstance().connect("198.46.153.211", 9876);
		widget = new ColorEntity(400, 400, new PolygonHitBox(
				new float[] {200,0, 0,50, -100,25, -50,-100, 0,-500}),
				Color.red);
	}

	@Override
	public void update(int delta) {
		//update widget
		widget.update(delta);

		//update players
		for(Player player : players)
			player.update(delta);
		currPlayer.update(delta);

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
	}

	@Override
	public void render(Graphics g) {
		g.setBackground(Color.white);

		//render widget
		widget.render(g);

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
				sendUpdateToServer();
				break;
			case Input.KEY_S:
				currPlayer.startMovingDown();
				sendUpdateToServer();
				break;
			case Input.KEY_A:
				currPlayer.startMovingLeft();
				sendUpdateToServer();
				break;
			case Input.KEY_D:
				currPlayer.startMovingRight();
				sendUpdateToServer();
				break;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		switch(key) {
		case Input.KEY_W:
			currPlayer.stopMovingUp();
			sendUpdateToServer();
			break;
		case Input.KEY_S:
			currPlayer.stopMovingDown();
			sendUpdateToServer();
			break;
		case Input.KEY_A:
			currPlayer.stopMovingLeft();
			sendUpdateToServer();
			break;
		case Input.KEY_D:
			currPlayer.stopMovingRight();
			sendUpdateToServer();
			break;
		}
	}

	@Override
	public void onConnected(int playerId) {
		currPlayer.setColor(calculatePlayerColor(playerId));
		isConnected = true;
	}

	@Override
	public void onMessageReceived(Message message) {
		if(message.getType() == Type.PLAYER_UPDATE) {
			PlayerUpdateMessage playerUpdate = (PlayerUpdateMessage) message;
			Player player = null;
			for(Player p : players) {
				if(p.getPlayerId() == playerUpdate.getPlayerId()) {
					player = p;
					break;
				}
			}
			if(player == null) {
				player = new Player(calculatePlayerColor(playerUpdate.getPlayerId()));
				player.setPlayerId(playerUpdate.getPlayerId());
				players.add(player);
			}
			player.setX(playerUpdate.getX());
			player.setY(playerUpdate.getY());
			if(playerUpdate.isMovingUp())
				player.startMovingUp();
			else
				player.stopMovingUp();
			if(playerUpdate.isMovingDown())
				player.startMovingDown();
			else
				player.stopMovingDown();
			if(playerUpdate.isMovingLeft())
				player.startMovingLeft();
			else
				player.stopMovingLeft();
			if(playerUpdate.isMovingRight())
				player.startMovingRight();
			else
				player.stopMovingRight();
		}
	}

	private Color calculatePlayerColor(int playerId) {
		if(playerId == -1)
			return Color.gray;
		if(playerId % 4 == 0)
			return Color.red;
		if(playerId % 4 == 1)
			return Color.blue;
		if(playerId % 4 == 2)
			return Color.yellow;
		return Color.green;
	}

	private void sendUpdateToServer() {
		if(isConnected && Client.getInstance().isConnected())
			Client.getInstance().sendPlayerUpdate(currPlayer.getX(), currPlayer.getY(), currPlayer.isMovingUp(), currPlayer.isMovingDown(), currPlayer.isMovingLeft(), currPlayer.isMovingRight());
	}
}