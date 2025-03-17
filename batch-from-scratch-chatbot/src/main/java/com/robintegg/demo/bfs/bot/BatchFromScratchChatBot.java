package com.robintegg.demo.bfs.bot;

import static com.robintegg.demo.bfs.chat.ChatMessageCreator.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;

import com.robintegg.demo.bfs.BatchMealPlanning;
import com.robintegg.demo.bfs.chat.ChatMessage;
import com.robintegg.demo.bfs.chat.ChatMessageCreator;
import com.robintegg.demo.bfs.chat.ChatStream;
import com.robintegg.demo.bfs.profiles.Profile;

@Controller
@RequestMapping("/")
public class BatchFromScratchChatBot {

	private final BatchMealPlanning batchMealPlanning;

	public BatchFromScratchChatBot(
			BatchMealPlanning batchMealPlanning ) {this.batchMealPlanning = batchMealPlanning;}

	@ModelAttribute("userProfile")
	public Profile userProfile() {
		return Profile.user();
	}

	@GetMapping
	public String bot( Model model ) {
		model.addAttribute( "chatStreams", batchMealPlanning.onMessage( userProfile(), "" ) );
		return "index";
	}

	@HxRequest
	@PostMapping("/message")
	public String chat( @RequestParam String message, Model model ) {

		List<ChatStream> chatStreams = new ArrayList<>();

		final String trimmedMessage = message.trim();

		chatStreams.add( ChatStream.of( userProfile(), List.of( text( trimmedMessage ) ) ) );
		chatStreams.addAll( batchMealPlanning.onMessage( userProfile(), trimmedMessage ) );

		model.addAttribute( "chatStreams", chatStreams );

		return "messages :: chat-streams";

	}

	@PostMapping("reset")
	public String reset( Model model ) {
		batchMealPlanning.reset();
		return "redirect:/";
	}

}
