package com.benlawrencem.game.dungeongarden;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.benlawrencem.game.dungeongarden.level.Level;

public class GameplayState extends BasicGameState {
	private int stateId;
	private Level level;

	public GameplayState(int stateId) {
		this.stateId = stateId;
		level = null;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		//initialize players
		//players = new PlayerHandler();
		//players.addPlayer(new OldPlayerEntity(DungeonGardenGame.GAME_WIDTH, DungeonGardenGame.GAME_HEIGHT), true);
	}

	public void init(GameContainer container, StateBasedGame game, Level level) throws SlickException {
		init(container, game);

		//initialize the level
		this.level = level;
		level.init();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		//update player positions
		//players.updateAll();

		//update game logic
		level.update(delta);

		//perform player-to-player hit detection
		//players.checkForPlayerCollisions();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		level.render(g);
	}

	@Override
	public void keyPressed(int key, char c) {
		level.keyPressed(key, c);
		/*switch(key) {
			case Input.KEY_UP:
				players.startMovingCurrentPlayerUp();
				break;
			case Input.KEY_DOWN:
				players.startMovingCurrentPlayerDown();
				break;
			case Input.KEY_LEFT:
				players.startMovingCurrentPlayerLeft();
				break;
			case Input.KEY_RIGHT:
				players.startMovingCurrentPlayerRight();
				break;
			case Input.KEY_ESCAPE:
				gameContainer.exit();
				break;
		}*/
	}

	@Override
	public void keyReleased(int key, char c) {
		level.keyReleased(key, c);
		/*switch(key) {
			case Input.KEY_UP:
				players.stopMovingCurrentPlayerUp();
				break;
			case Input.KEY_DOWN:
				players.stopMovingCurrentPlayerDown();
				break;
			case Input.KEY_LEFT:
				players.stopMovingCurrentPlayerLeft();
				break;
			case Input.KEY_RIGHT:
				players.stopMovingCurrentPlayerRight();
				break;
		}*/
	}

	@Override
	public int getID() {
		return stateId;
	}
}