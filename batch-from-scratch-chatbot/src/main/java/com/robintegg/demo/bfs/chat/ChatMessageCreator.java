package com.robintegg.demo.bfs.chat;

import java.time.LocalDate;
import java.util.List;

public class ChatMessageCreator {

	public static ChatMessage<String> text( String message ) {
		return new ChatMessage<>( "text", message );
	}

	public static ChatMessage<String> suggestion( String suggestion ) {
		return new ChatMessage<>( "suggestion", suggestion );
	}

	public static ChatMessage<?> reminder( LocalDate localDate ) {
		return new ChatMessage<>( "reminder", localDate );
	}

	public static ChatMessage<?> list( String type, List<?> list ) {
		return new ChatMessage<>( type, list );
	}

	public static ChatMessage<?> object( String type, Object o ) {
		return new ChatMessage<>( type, o );
	}

}
