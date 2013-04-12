package com.benlawrencem.game.dungeongarden.net;

import java.util.ArrayList;
import java.util.List;

import com.benlawrencem.game.dungeongarden.net.message.Message;
import com.benlawrencem.game.dungeongarden.net.message.Message.Type;
import com.benlawrencem.game.dungeongarden.net.message.PlayerIdResponseMessage;
import com.benlawrencem.net.nightingale.ClientConnection;
import com.benlawrencem.net.nightingale.ClientConnection.CouldNotConnectException;
import com.benlawrencem.net.nightingale.ClientConnectionListener;
import com.benlawrencem.net.nightingale.Packet.CouldNotSendPacketException;

public class Client implements ClientConnectionListener {
	private static Client instance = null;
	private List<ClientListener> listeners;
	private int lastSentMessageId;
	private int playerId;

	public static Client getInstance() {
		if(instance == null)
			instance = new Client();
		return instance;
	}

	private ClientConnection conn;

	public Client() {
		conn = new ClientConnection(this);
		listeners = new ArrayList<ClientListener>();
		lastSentMessageId = -1;
		playerId = -1;
	}

	public void connect(String address, int port) {
		System.out.println("Connecting to " + address + ":" + port);
		try {
			conn.connect(address, port);
		} catch (CouldNotConnectException e) {
			System.out.println("Could not connect to " + address + ": " + port + ": " + e.getMessage());
		}
	}

	public void disconnect() {
		conn.disconnect();
		lastSentMessageId = -1;
	}

	public boolean isConnected() {
		return conn.isConnected();
	}

	@Override
	public void onConnected() {
		System.out.println("Connected");
		lastSentMessageId++;
		try {
			String s = Message.createPlayerIdRequestMessage(lastSentMessageId).encode();
			System.out.println("Sending ID request: " + s);
			conn.send(s);
		} catch (CouldNotSendPacketException e) {
			System.out.println("Could not send ID request: " + e.getMessage());
		}
	}

	@Override
	public void onCouldNotConnect(String reason) {
		System.out.println("Could not connect: " + reason);
		lastSentMessageId = -1;
	}

	@Override
	public void onDisconnected(String reason) {
		System.out.println("Disconnected: " + reason);
		lastSentMessageId = -1;
	}

	@Override
	public void onMessageNotDelivered(int messageId, int resendMessageId, String message) {
		System.out.println("Message not delivered: \"" + message + "\"");
	}

	public void addListener(ClientListener listener) {
		listeners.add(listener);
	}

	public void sendPlayerUpdate(float x, float y, boolean isMovingUp, boolean isMovingDown, boolean isMovingLeft, boolean isMovingRight) {
		lastSentMessageId++;
		Message messageObject = Message.createPlayerUpdateMessage(lastSentMessageId, playerId, x, y, isMovingUp, isMovingDown, isMovingLeft, isMovingRight);
		try {
			String s = messageObject.encode();
			System.out.println("Sending player update: \"" + s + "\"");
			conn.send(s);
		} catch (CouldNotSendPacketException e) {
			System.out.println("Could not send player update: " + e.getMessage());
		}
	}

	@Override
	public void onReceive(String message) {
		System.out.println("Received message: \"" + message + "\"");
		Message messageObject = Message.parse(message);
		if(messageObject == null)
			System.out.println("(Could not parse message!)");
		else {
			if(messageObject.getType() == Type.PLAYER_ID_RESPONSE) {
				System.out.println("Received ID");
				playerId = ((PlayerIdResponseMessage) messageObject).getPlayerId();
				System.out.println("ID is " + playerId);
				System.out.println("Sending out onConnected notification to listeners");
				for(ClientListener listener : listeners)
					listener.onConnected(playerId);
			}
			else {
				System.out.println("Sending out onMessageReceived notification to listeners");
				for(ClientListener listener : listeners)
					listener.onMessageReceived(messageObject);
			}
		}
	}
}