package com.robintegg.demo.bfs.chat;

import java.util.List;

import com.robintegg.demo.bfs.profiles.Profile;

public class ChatStream {

	private final Profile profile;
	private final List<ChatMessage<?>> messages;

	private ChatStream( Profile profile, List<ChatMessage<?>> messages ) {
		this.profile = profile;
		this.messages = messages;
	}

	public List<ChatMessage<?>> getMessages() {
		return messages;
	}

	public Profile getProfile() {
		return profile;
	}

	public static ChatStream of( Profile profile, List<ChatMessage<?>> messages ) {
		return new ChatStream( profile, messages );
	}

}
