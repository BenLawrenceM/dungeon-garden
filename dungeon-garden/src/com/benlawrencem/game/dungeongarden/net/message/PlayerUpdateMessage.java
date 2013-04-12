package com.benlawrencem.game.dungeongarden.net.message;

public class PlayerUpdateMessage extends Message {
	private int playerId;
	private float x;
	private float y;
	private boolean isMovingUp;
	private boolean isMovingDown;
	private boolean isMovingLeft;
	private boolean isMovingRight;

	public PlayerUpdateMessage() {
		super();
		playerId = -1;
		x = 0;
		y = 0;
		isMovingUp = false;
		isMovingDown = false;
		isMovingLeft = false;
		isMovingRight = false;
	}

	@Override
	public String encode() {
		return getMessageId() + " "
				+ Message.PLAYER_UPDATE_PREFIX + " "
				+ playerId + " "
				+ (int) x + " "
				+ (int) y + " "
				+ (isMovingUp ? "U" : (isMovingDown ? "D" : "-")) + " "
				+ (isMovingLeft ? "L" : (isMovingRight ? "R" : "-")) + " ";
	}

	@Override
	public void decode(String message) {
		String[] tokens = message.split(" ");
		setMessageId(Integer.parseInt(tokens[0]));
		setPlayerId(Integer.parseInt(tokens[2]));
		setX(Float.parseFloat(tokens[3]));
		setY(Float.parseFloat(tokens[4]));
		setIsMovingUp("U".equals(tokens[5]));
		setIsMovingDown("D".equals(tokens[5]));
		setIsMovingLeft("L".equals(tokens[6]));
		setIsMovingRight("R".equals(tokens[6]));
	}

	@Override
	public Type getType() {
		return Type.PLAYER_UPDATE;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public boolean isMovingUp() {
		return isMovingUp;
	}

	public void setIsMovingUp(boolean isMovingUp) {
		this.isMovingUp = isMovingUp;
	}

	public boolean isMovingDown() {
		return isMovingDown;
	}

	public void setIsMovingDown(boolean isMovingDown) {
		this.isMovingDown = isMovingDown;
	}

	public boolean isMovingLeft() {
		return isMovingLeft;
	}

	public void setIsMovingLeft(boolean isMovingLeft) {
		this.isMovingLeft = isMovingLeft;
	}

	public boolean isMovingRight() {
		return isMovingRight;
	}

	public void setIsMovingRight(boolean isMovingRight) {
		this.isMovingRight = isMovingRight;
	}
}