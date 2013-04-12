package com.benlawrencem.game.dungeongarden.net;

import com.benlawrencem.game.dungeongarden.net.message.Message;
import com.benlawrencem.game.dungeongarden.net.message.Message.Type;
import com.benlawrencem.net.nightingale.Packet.CouldNotSendPacketException;
import com.benlawrencem.net.nightingale.Server.CouldNotStartServerException;
import com.benlawrencem.net.nightingale.ServerListener;

public class Server implements ServerListener {
	private com.benlawrencem.net.nightingale.Server server;

	public static void main(String[] args) {
		Server server = new Server();
		server.startServer(9876);
	}

	public Server() {
		server = new com.benlawrencem.net.nightingale.Server(this);
	}

	public void startServer(int port) {
		System.out.println("Starting server on port " + port);
		try {
			server.startServer(port);
			System.out.println("Server (probably) started");
		} catch (CouldNotStartServerException e) {
			System.out.println("Could not start server: " + e.getMessage());
		}
	}

	public void stopServer() {
		System.out.println("Stopping server...");
		server.stopServer();
	}

	public boolean isRunning() {
		return server.isRunning();
	}

	@Override
	public boolean onClientConnected(int clientId, String address, int port) {
		System.out.println("Client " + clientId + " connected");
		return true;
	}

	@Override
	public void onClientDisconnected(int clientId, String reason) {
		System.out.println("Client " + clientId + "disconnected: " + reason);
	}

	@Override
	public void onMessageNotDelivered(int messageId, int resendMessageId, int clientId, String message) {
		System.out.println("Message " + messageId + " not delivered to client " + clientId + ": \"" + message + "\"");
	}

	@Override
	public void onReceive(int clientId, String message) {
		Message messageObject = Message.parse(message);
		System.out.println("Received message from client " + clientId + ": \"" + message + "\"");
		if(messageObject == null)
			System.out.println("(Could not parse message!)");
		else {
			if(messageObject.getType() == Type.PLAYER_ID_REQUEST) {
				System.out.println("Received ID request");
				try {
					String s = Message.createPlayerIdResponseMessage(-1, clientId).encode();
					System.out.println("Sending ID to client " + clientId + ": " + s);
					server.send(clientId, s);
				} catch (CouldNotSendPacketException e) {
					System.out.println("Could not send ID to client " + clientId + ": " + e.getMessage());
				}
			}
			else {
				System.out.println("Broadcasting message too all clients except client " + clientId +": \"" + message + "\"");
				for(int otherClientId : server.getClientIds()) {
					if(otherClientId != clientId) {
						try {
							server.send(otherClientId, message);
						} catch (CouldNotSendPacketException e) {
							System.out.println("Could not broadcast message \"" + message + "\" to client " + otherClientId + ": " + e.getMessage());
						}
					}
				}
			}
		}
	}

	@Override
	public void onServerStopped() {
		System.out.println("Server stopped");
	}
}