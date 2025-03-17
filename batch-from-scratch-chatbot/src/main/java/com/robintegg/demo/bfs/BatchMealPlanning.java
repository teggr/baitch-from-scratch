package com.robintegg.demo.bfs;

import static com.robintegg.demo.bfs.chat.ChatMessageCreator.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import io.modelcontextprotocol.client.McpSyncClient;

import com.robintegg.demo.bfs.chat.ChatMessage;
import com.robintegg.demo.bfs.chat.ChatStream;
import com.robintegg.demo.bfs.model.BatchMealPlan;
import com.robintegg.demo.bfs.model.Ingredient;
import com.robintegg.demo.bfs.model.OrderConfirmation;
import com.robintegg.demo.bfs.profiles.FoodPreferences;
import com.robintegg.demo.bfs.profiles.Profile;
import com.robintegg.demo.bfs.utils.Utils;

@Component
public class BatchMealPlanning {

	enum State {
		starting,
		planning,
		shopping,
		batching
	}

	private final ChatClient.Builder chatClientBuilder;
	private final FoodPreferences foodPreferences;
	private final McpSyncClient mcpSyncClient;

	private State state = State.starting;
	private ChatClient chatClient;

	public BatchMealPlanning( ChatClient.Builder chatClientBuilder, FoodPreferences foodPreferences, McpSyncClient mcpSyncClient ) {
		this.chatClientBuilder = chatClientBuilder.defaultAdvisors( new SimpleLoggerAdvisor() )
				.defaultTools( new LocalTools() )
				.defaultSystem( """
						You are an AI assistant that knows all about batch cooking.
						Your tone embodies the fun side traits of Joe Swash and 'The Batch Lady' Suzanne Mulholland from the popular channel 4 program, ‘Batch from Scratch’.
						You will help with time-saving tips and easy-to-follow recipes, to show how batch cooking can turn mealtime mayhem into a dinnertime dream.
						""" );
		this.foodPreferences = foodPreferences;
		this.mcpSyncClient = mcpSyncClient;
	}

	public List<ChatStream> onMessage( Profile user, final String message ) {

		if ( state == State.starting ) {

			chatClient = chatClientBuilder
					.defaultAdvisors( new MessageChatMemoryAdvisor( new InMemoryChatMemory() ) )
					.build();

			final String welcome = chatClient.prompt().user( u -> u.text( """
					In a couple of sentences, welcome our user called {name} to this new Meal Planner.
					Ask the user when they will have some free time to do some batch cooking.
					Add a suggestion how they might respond to the question of free time.
					""" ).param( "name", user.getName() ) ).call().content();

			final List<ChatMessage<?>> chatMessages = List.of( text( welcome ), suggestion(
					"Try entering something like on Sunday I've got an hour free to make some evening meals for 4 people" ) );

			state = State.planning;

			return List.of( ChatStream.of( Profile.joe(), chatMessages ) );

		} else if ( state == State.planning ) {

			final ChatClient.ChatClientRequestSpec prompt = chatClient.prompt();

			final BatchMealPlan content = chatClient.prompt()
					.user( u -> u.text( """
							Given the time constraints provided by the user.
							Provide a list of recipes that the user can cook within their suggested time window.
							You should suggest as many different types of meal as possible to cook with their suggested time window.
							Include in the response all the ingredients required, the number of portions that the recipe will make and freezing instructions.
							Plan out the cooking session for the specified date using the user's current date and time.
							Avoid meal suggestions that include food that the user doesn't like. Check all the ingredients in the recipes.
							Include a friendly summary message that the user will read after browsing the recipes.
							###
							{userMessage}
							""" ).param( "userMessage", message ) )
					.tools( foodPreferences )
					.call()
					.entity( BatchMealPlan.class );

			List<ChatMessage<?>> chatMessages = Utils.messagesForBatchMealPlan( content );

			chatMessages.add( text( "What would you like to do with your plan?" ) );
			chatMessages.add( text( "We could create a shopping list for you?" ) );
			chatMessages.add( reminder( content.plannedDate() ) );

			chatMessages.add(
					suggestion( "Try entering something like please create me a shopping list." ) );

			state = State.shopping;

			return List.of( ChatStream.of( Profile.suzanne(), chatMessages ) );

		} else if ( state == State.shopping ) {

			final ShoppingIntent createShoppingList = chatClient.prompt().user( u -> u.text( """
					Tell me if the user is asking for a list of ingredients to be created? Respond with a simple yes or no.
					Tell me if the user is asking for the shopping to be ordered on their behalf from a grocers or online service? Respond with a simple yes or no.
					The values are mutually exclusive. Only one can be chosen.
					###
					{userMessage}
					""" ).param( "userMessage", message ) ).call().entity( ShoppingIntent.class );

			if ( createShoppingList.isCreateShoppingList() ) {

				final List<Ingredient> shoppingList = chatClient.prompt()
						.user( u -> u.text( """
								Given the recipes that we have already put together in the cooking session. Think about this step by step.
								Get the list of all the ingredients in all the cooking session recipes.
								The list should include total quantities of each ingredient by name so there are no duplicates.
								Each entry in the list should contain a name and quantity.
								Any further instructions should be incorporated.
								###
								{userMessage}
								""" ).param( "userMessage", message ) )
						.call()
						.entity( new ParameterizedTypeReference<List<Ingredient>>() {} );

				List<ChatMessage<?>> chatMessages = new ArrayList<>();

				chatMessages.add( list( "shoppingList", shoppingList ) );

				chatMessages.add( text( "What would you like to do with your shopping list?" ) );
				chatMessages.add( text( "Shall we get the shopping list ordered for you?" ) );

				chatMessages.add( suggestion(
						"Try entering something like please place my order for delivery." ) );

				return List.of( ChatStream.of( Profile.suzanne(), chatMessages ) );

			} else if ( createShoppingList.isOrderShopping() ) {

				final OrderConfirmation orderConfirmation = chatClient.prompt()
						.user( u -> u.text( """
								Get the ingredient list that we already created for the cooking session.
								Place an order for the shopping.
								The order must be delivered before the planned cooking session.
								Confirm the order can be delivered and what the delivery details are.
								Take into account any further instructions from the following user message.
								Add a jovial confirmation message to the response.
								###
								{userMessage}
								""" ).param( "userMessage", message ) )
						.tools( new SyncMcpToolCallbackProvider( mcpSyncClient ) )
						.call()
						.entity( OrderConfirmation.class );

				List<ChatMessage<?>> chatMessages = new ArrayList<>();

				chatMessages.add( object( "orderConfirmation", orderConfirmation ) );

				chatMessages.add( text( "I think we're done with this demo now!" ) );
				chatMessages.add( text( "Time for some questions..." ) );

				return List.of( ChatStream.of( Profile.joe(), chatMessages ) );

			} else {

				List<ChatMessage<?>> chatMessages = List.of(
						text( "I can't do much about that right now :(" ) );

				return List.of( ChatStream.of( Profile.joe(), chatMessages ) );

			}

		} else {

			List<ChatMessage<?>> chatMessages = List.of(
					text( "I can't do much about that right now :(" ) );

			return List.of( ChatStream.of( Profile.joe(), chatMessages ) );

		}

	}

	public void reset() {

		state = State.starting;

	}

	static class LocalTools {

		@Tool(description = "Get the current date and time in the user's timezone")
		String getCurrentDateTime() {
			System.out.println( "getCurrentDateTime" );
			return LocalDateTime.now()
					.atZone( LocaleContextHolder.getTimeZone().toZoneId() )
					.toString();
		}

	}

	public static class ShoppingIntent {

		private boolean createShoppingList = false;
		private boolean orderShopping = false;

		public void setCreateShoppingList( final boolean createShoppingList ) {
			this.createShoppingList = createShoppingList;
		}

		public boolean isCreateShoppingList() {
			return createShoppingList;
		}

		public void setOrderShopping( final boolean orderShopping ) {
			this.orderShopping = orderShopping;
		}

		public boolean isOrderShopping() {
			return orderShopping;
		}

	}

}
