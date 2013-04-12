package com.benlawrencem.game.dungeongarden.net.message;

public class PlayerIdRequestMessage extends Message {
	public PlayerIdRequestMessage() {
		super();
	}

	@Override
	public String encode() {
		return getMessageId() + " " + Message.PLAYER_ID_REQUEST_PREFIX;
	}

	@Override
	public void decode(String message) {
		String[] tokens = message.split(" ");
		setMessageId(Integer.parseInt(tokens[0]));
	}

	@Override
	public Type getType() {
		return Type.PLAYER_ID_REQUEST;
	}
}