package com.robintegg.demo.groceries;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class GroceryStoreApplication {

	public static void main( String[] args ) {
		SpringApplication.run( GroceryStoreApplication.class, args );
	}

	@Bean
	ToolCallbackProvider toolCallbackProvider( GroceryOrders tools ) {
		return MethodToolCallbackProvider.builder().toolObjects( tools ).build();
	}

}

@Component
class GroceryOrders {

	@Tool(description = "Place an order for a list of ingredients. The will be before a certain date. The ingredient list contains names and quantities. Will confirm order, price and delivery date.")
	public OrderConfirmation placeOrder(
			@ToolParam(description = "The delivery date for the shopping.") LocalDate deliveryDate,
			@ToolParam(description = "The ingredients list that needs to be ordered and delivered. Contains names and quantities") List<Ingredient> ingredients ) {

		System.out.println( "order for " + deliveryDate + " of " + ingredients );

		return new OrderConfirmation( true, deliveryDate.minusDays( 1 ), BigDecimal.TEN );

	}

	record OrderConfirmation(boolean confirmed, LocalDate deliveryDate, BigDecimal price) {}

	// Ingredient record to hold ingredient details
	public record Ingredient(String name, String quantity) {}

}

