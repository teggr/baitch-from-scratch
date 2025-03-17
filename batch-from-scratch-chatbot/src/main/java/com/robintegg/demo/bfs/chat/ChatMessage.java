package com.robintegg.demo.bfs.chat;

public class ChatMessage<T> {

	private final String type;
	private final T content;

	public ChatMessage(  String type, T content ) {
		this.type = type;
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public T getContent() {
		return content;
	}

}
