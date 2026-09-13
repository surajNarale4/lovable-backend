package io.suraj.projects.lovable.service.impl;

import io.suraj.projects.lovable.entity.ChatSession;
import io.suraj.projects.lovable.entity.ChatSessionId;
import io.suraj.projects.lovable.entity.Project;
import io.suraj.projects.lovable.entity.User;
import io.suraj.projects.lovable.error.ResourseNotFoundException;
import io.suraj.projects.lovable.llm.Prompt;
import io.suraj.projects.lovable.repository.ChatSessionRepository;
import io.suraj.projects.lovable.repository.ProjectRepository;
import io.suraj.projects.lovable.repository.UserRepository;
import io.suraj.projects.lovable.security.SecurityExpressions;
import io.suraj.projects.lovable.service.AiGenerationService;
import io.suraj.projects.lovable.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiGenerationServiceImpl implements AiGenerationService {

    private final ChatClient chatClient;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectFileService projectFileService;
    private final ChatSessionRepository chatSessionRepository;
    private final static Pattern FILE_TAG_PATTERN = Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>",Pattern.DOTALL);



    @Override
    @PreAuthorize("@security.hasEditPermission(#projectId)")
    public Flux<String> streamResponse(String message, Long projectId) {

        String userId = SecurityExpressions.getUserId();

        StringBuffer responseBuffer = new StringBuffer();

        createChatSessionIfNotExist(projectId,userId);

        projectId = 1L;
        Map<String,Object> advisorParams= Map.of(
                "userId",userId,
                "projectId",projectId
        );


        Long finalProjectId = projectId;
        return chatClient.prompt()
                .system(Prompt.SYSTEM_PROMPT)
                .user(message)
                .advisors(advisorSpec -> advisorSpec.params(advisorParams))
                .stream()
                .chatResponse()
                .doOnNext(response->{
                    String re= response.getResult().getOutput().getText();
                    responseBuffer.append(re);
                })
                .doOnComplete(()->{
                    Schedulers.boundedElastic().schedule(()->parseAndSaveFile(responseBuffer, finalProjectId));

                })
                .doOnError(error->{
                    log.error("error during streaming project id :{}", finalProjectId);
                })
                .map(response-> Objects.requireNonNull(Objects.requireNonNull(response.getResult()).getOutput().getText()));
    }

    private void parseAndSaveFile(StringBuffer responseBuffer, Long projectId) {

        Matcher matcher = FILE_TAG_PATTERN.matcher(responseBuffer);

        while(matcher.find()){
            String filePath = matcher.group(1);
            String fileContent = matcher.group(2);

            projectFileService.saveFile(projectId,filePath,fileContent);
        }
    }

    private ChatSession createChatSessionIfNotExist(Long projectId, String userId) {

        ChatSessionId chatSessionId = new ChatSessionId(projectId,userId);
        ChatSession chatSession=chatSessionRepository.findById(chatSessionId).orElse(null);

        if(chatSession==null){
            Project project = projectRepository.findById(projectId).orElseThrow(()->new ResourseNotFoundException("project not found with project id "+projectId));
            User user = userRepository.findById(userId).orElseThrow(()->new ResourseNotFoundException("user not found with user id "+userId));

           chatSession  = ChatSession.builder()
                    .user(user)
                    .project(project)
                    .id(chatSessionId)
                    .build();

            chatSessionRepository.save(chatSession);
        }
        return chatSession;

    }
}
