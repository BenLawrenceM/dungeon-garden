package com.benlawrencem.game.dungeongarden.net.message;

public abstract class Message {
	public static final int NO_MESSAGE_ID = -1;
	public static final String PLAYER_UPDATE_PREFIX = "pos=";
	public static final String PLAYER_ID_REQUEST_PREFIX = "id?";
	public static final String PLAYER_ID_RESPONSE_PREFIX = "id=";
	public static enum Type { PLAYER_UPDATE, PLAYER_ID_REQUEST, PLAYER_ID_RESPONSE };

	private int messageId;

	public Message() {
		this.messageId = Message.NO_MESSAGE_ID;
	}

	public Message(int messageId) {
		this.messageId = messageId;
	}

	public static Message parse(String message) {
		String[] tokens = message.split(" ");

		if(Message.PLAYER_UPDATE_PREFIX.equals(tokens[1])) {
			Message messageObject = new PlayerUpdateMessage();
			messageObject.decode(message);
			return messageObject;
		}
		else if(Message.PLAYER_ID_REQUEST_PREFIX.equals(tokens[1])) {
			Message messageObject = new PlayerIdRequestMessage();
			messageObject.decode(message);
			return messageObject;
		}
		else if(Message.PLAYER_ID_RESPONSE_PREFIX.equals(tokens[1])) {
			Message messageObject = new PlayerIdResponseMessage();
			messageObject.decode(message);
			return messageObject;
		}

		return null;
	}

	public static PlayerUpdateMessage createPlayerUpdateMessage(int messageId, int playerId, float x, float y, boolean isMovingUp, boolean isMovingDown, boolean isMovingLeft, boolean isMovingRight) {
		PlayerUpdateMessage messageObject = new PlayerUpdateMessage();
		messageObject.setMessageId(messageId);
		messageObject.setPlayerId(playerId);
		messageObject.setX(x);
		messageObject.setY(y);
		messageObject.setIsMovingUp(isMovingUp);
		messageObject.setIsMovingDown(isMovingDown);
		messageObject.setIsMovingLeft(isMovingLeft);
		messageObject.setIsMovingRight(isMovingRight);
		return messageObject;
	}

	public static PlayerIdRequestMessage createPlayerIdRequestMessage(int messageId) {
		PlayerIdRequestMessage messageObject = new PlayerIdRequestMessage();
		messageObject.setMessageId(messageId);
		return messageObject;
	}

	public static PlayerIdResponseMessage createPlayerIdResponseMessage(int messageId, int playerId) {
		PlayerIdResponseMessage messageObject = new PlayerIdResponseMessage();
		messageObject.setMessageId(messageId);
		messageObject.setPlayerId(playerId);
		return messageObject;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public abstract String encode();

	public abstract void decode(String message);

	public abstract Type getType();
}