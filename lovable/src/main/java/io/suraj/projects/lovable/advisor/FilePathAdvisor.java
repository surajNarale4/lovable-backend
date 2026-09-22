package io.suraj.projects.lovable.advisor;

import io.suraj.projects.lovable.dto.project.FileNode;
import io.suraj.projects.lovable.service.ProjectFileService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class FilePathAdvisor implements StreamAdvisor {

    private final ProjectFileService projectFileService;

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {


        Long projectId = (Long) chatClientRequest.context().getOrDefault("projectId",1);
        String userId = (String) chatClientRequest.context().getOrDefault("userId",1);

        String systemContext = chatClientRequest.prompt().getSystemMessage().getText();


        List<FileNode> fileNodes =projectFileService.getFileTree(projectId);

        String fileSection = fileNodes.stream()
                .map(node->"\n\n "+node.path()+"\n\n")
                .collect(Collectors.joining());

        String newSystemPrompt = systemContext + "\n"+"------File Section-------"+"\n"+fileSection;

        chatClientRequest.prompt().getSystemMessage().mutate().text(newSystemPrompt);

        return streamAdvisorChain.nextStream(chatClientRequest);
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
