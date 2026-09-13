package io.suraj.projects.lovable.controller;


import io.suraj.projects.lovable.dto.chat.ChatRequest;
import io.suraj.projects.lovable.service.AiGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final AiGenerationService aiGeneratorService;

    @PostMapping(value="/stream" ,produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> stream(@RequestBody ChatRequest request){
        return aiGeneratorService.streamResponse(request.message(), request.projectId())
                .map(data->ServerSentEvent.<String>builder()
                        .data(data)
                        .build());
    }
}
