# B(ai)tch from Scratch

A Spring Boot AI demo for introducing concepts using Channel 4's Batch from Scratch as a theme.

![](/docs/batch-from-scratch.jpg)

There are two services.


## batch-from-scratch-chat-bot

This contains the main chat client and UI.

You will need to add your define the `spring.ai.openai.api-key` property which can be done via environment variables.

```shell
SPRING_AI_OPENAI_API_KEY=
```

Run `BatchMealPlanApplication` on http://localhost:8080

## grocery-order-service

This is an online grocery store.

Run `GroceryStoreApplication` on http://localhost:8081

