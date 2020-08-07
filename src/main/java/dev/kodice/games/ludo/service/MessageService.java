package dev.kodice.games.ludo.service;

public class MessageService {

	public Long getGameIdFromMessage(String message) {
		return Long.parseLong(message.substring(5));
	}
	
}
