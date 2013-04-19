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
	public void init(GameContainer container, StateBasedGame game) throws SlickException {}

	public void init(GameContainer container, StateBasedGame game, Level level) throws SlickException {
		init(container, game);
		this.level = level;
		level.init();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		level.update(delta);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		level.render(g);
	}

	@Override
	public void keyPressed(int key, char c) {
		level.keyPressed(key, c);
	}

	@Override
	public void keyReleased(int key, char c) {
		level.keyReleased(key, c);
	}

	@Override
	public int getID() {
		return stateId;
	}
}