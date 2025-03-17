package com.robintegg.demo.bfs;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;

@Configuration
public class BatchMealPlanConfiguration {

	@Bean
	McpSyncClient mcpSyncClient() {
		final McpSyncClient mcp = McpClient
				.sync( new HttpClientSseClientTransport( "http://localhost:8081" ) )
				.requestTimeout( Duration.ofSeconds(10)  )
				.build();
		mcp.initialize();
		return mcp;
	}

}
