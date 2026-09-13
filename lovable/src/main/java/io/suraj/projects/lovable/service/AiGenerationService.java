package io.suraj.projects.lovable.service;

import io.suraj.projects.lovable.mapper.MemberMapper;
import reactor.core.publisher.Flux;

public interface AiGenerationService {
    Flux<String> streamResponse(String message, Long aLong);
}
