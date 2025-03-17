package com.robintegg.demo.bfs.model;

import java.time.Duration;
import java.util.List;

public record CookingSession(Duration totalTime, List<Recipe> recipes,
							 List<String> finalSteps, List<String> mealOverview) {}
