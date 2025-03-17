package com.robintegg.demo.bfs.model;

import java.time.LocalDate;

public record BatchMealPlan(LocalDate plannedDate, String intro,
							CookingSession cookingSession,
							String summaryMessage) {}
