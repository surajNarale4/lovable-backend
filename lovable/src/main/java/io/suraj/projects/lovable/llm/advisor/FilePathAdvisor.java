package io.suraj.projects.lovable.llm.advisor;

import io.suraj.projects.lovable.dto.project.FileNode;
import io.suraj.projects.lovable.repository.ChatSessionRepository;
import io.suraj.projects.lovable.service.ProjectFileService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import  org.springframework.ai.chat.messages.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class FilePathAdvisor implements StreamAdvisor {

    private final ProjectFileService projectFileService;
    private final ChatSessionRepository chatSessionRepository;

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {


        Long projectId = (Long) chatClientRequest.context().getOrDefault("projectId",1);
        String userId = (String) chatClientRequest.context().getOrDefault("userId",1);

        String systemContext = chatClientRequest.prompt().getSystemMessage().getText();


        List<FileNode> fileNodes =projectFileService.getFileTree(projectId);

        String fileSection = fileNodes.stream()
                .map(node->" "+node.path()+"\n\n")
                .collect(Collectors.joining());

        String newSystemPrompt = systemContext + "\n"+"------File Section-------"+"\n\n"+fileSection;
        ChatClientRequest newChaClientRequest = changeSystemPrompt(chatClientRequest,newSystemPrompt);

        return streamAdvisorChain.nextStream(newChaClientRequest);

    }

    private ChatClientRequest changeSystemPrompt(ChatClientRequest chatClientRequest, String newSystemPrompt) {
        Prompt oldPrompt = chatClientRequest.prompt();
        List<Message> messages = new ArrayList<>();
        SystemMessage systemMessage = new SystemMessage(newSystemPrompt);
        messages.add(systemMessage);

        for(Message message : messages){

            if(!(message instanceof SystemMessage)){
                messages.add(message);
            }
        }
        Prompt newPrompt = new Prompt(messages,oldPrompt.getOptions());
        return chatClientRequest.builder().prompt(newPrompt).build();
    }

    @Override
    public String getName() {
        return "FilePathAdvisor";
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
