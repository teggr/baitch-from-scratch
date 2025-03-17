package com.robintegg.demo.bfs.profiles;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class FoodPreferences {

	/*
	 * set user preference
	 * get user preference ....
	 */

	@Tool(description = "Get foods that the user does not like")
	public List<String> getFoodsThatTheUserDoesNotLike() {
		System.out.println( "Get foods that the user does not like" );
		return List.of( "chicken" );
	}

}
