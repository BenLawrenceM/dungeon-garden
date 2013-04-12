package com.benlawrencem.game.dungeongarden.level;

import org.newdawn.slick.Graphics;

public interface Level {
	void init();
	void update(int delta);
	void render(Graphics g);
	void keyPressed(int key, char c);
	void keyReleased(int key, char c);
}