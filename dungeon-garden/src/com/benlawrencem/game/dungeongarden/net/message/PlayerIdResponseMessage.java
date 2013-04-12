package com.benlawrencem.game.dungeongarden.net.message;

public class PlayerIdResponseMessage extends Message {
	private int playerId;

	public PlayerIdResponseMessage() {
		super();
		playerId = -1;
	}

	@Override
	public String encode() {
		return getMessageId() + " " + Message.PLAYER_ID_RESPONSE_PREFIX + " " + playerId + " ";
	}

	@Override
	public void decode(String message) {
		String[] tokens = message.split(" ");
		setMessageId(Integer.parseInt(tokens[0]));
		setPlayerId(Integer.parseInt(tokens[2]));
	}

	@Override
	public Type getType() {
		return Type.PLAYER_ID_RESPONSE;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
}