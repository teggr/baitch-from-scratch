package com.robintegg.demo.bfs.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderConfirmation(String confirmationMessage, LocalDate deliveryDate,
								BigDecimal totalPrice, String summary) {}
