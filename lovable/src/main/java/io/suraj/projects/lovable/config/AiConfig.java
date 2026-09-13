package io.suraj.projects.lovable.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(@Qualifier("ollamaChatModel") ChatModel ollamaChatModel){
        return ChatClient.builder(ollamaChatModel).build();
    }
}
