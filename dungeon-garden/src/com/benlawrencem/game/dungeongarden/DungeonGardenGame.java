package com.benlawrencem.game.dungeongarden;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class DungeonGardenGame extends StateBasedGame {
	public static final int LOADING_STATE = 0;
	public static final int GAMEPLAY_STATE = 1;
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new DungeonGardenGame());
		app.setAlwaysRender(true);
		app.setDisplayMode(800, 600, false);
		app.setMaximumLogicUpdateInterval(40);
		app.start();
	}

	public DungeonGardenGame() {
		super("Dungeon Garden");
		addState(new LoadingState(DungeonGardenGame.LOADING_STATE));
		addState(new GameplayState(DungeonGardenGame.GAMEPLAY_STATE));
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		getState(DungeonGardenGame.LOADING_STATE).init(container, this);
		getState(DungeonGardenGame.GAMEPLAY_STATE).init(container, this);
	}
}