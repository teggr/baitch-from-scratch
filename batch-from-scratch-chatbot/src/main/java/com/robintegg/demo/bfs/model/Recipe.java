package com.robintegg.demo.bfs.model;

import java.time.Duration;
import java.util.List;

public record Recipe(String name, Duration preparationTime, List<String> ingredients, List<String> steps,
					 int servings) {}
