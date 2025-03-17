package com.robintegg.demo.bfs.utils;

import static com.robintegg.demo.bfs.chat.ChatMessageCreator.*;

import java.util.ArrayList;
import java.util.List;

import com.robintegg.demo.bfs.chat.ChatMessage;
import com.robintegg.demo.bfs.model.BatchMealPlan;
import com.robintegg.demo.bfs.model.CookingSession;

public class Utils {

	public static List<ChatMessage<?>> messagesForBatchMealPlan(
			final BatchMealPlan batchMealPlan ) {

		final List<ChatMessage<?>> list = new ArrayList<>();

		list.add( text( batchMealPlan.intro() ) );

		final CookingSession cookingSession = batchMealPlan.cookingSession();
		list.add( object( "cookingSession", cookingSession ) );
		list.add( list( "recipes", cookingSession.recipes() ) );
		list.add( list( "finalSteps", cookingSession.finalSteps() ) );
		list.add( list( "overview", cookingSession.mealOverview() ) );

		list.add( text( batchMealPlan.summaryMessage() ) );

		return list;

	}

}
