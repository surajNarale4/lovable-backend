package io.suraj.projects.lovable.repository;

import io.suraj.projects.lovable.entity.ChatSession;
import io.suraj.projects.lovable.entity.ChatSessionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, ChatSessionId> {
}
