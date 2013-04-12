package com.benlawrencem.game.dungeongarden.net;

import com.benlawrencem.game.dungeongarden.net.message.Message;

public interface ClientListener {
	void onConnected(int playerId);
	void onMessageReceived(Message message);
}