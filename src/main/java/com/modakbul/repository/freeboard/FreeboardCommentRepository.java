package com.modakbul.repository.freeboard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modakbul.entity.freeboard.FreeboardComment;

public interface FreeboardCommentRepository extends JpaRepository<FreeboardComment, Long> {
	// boardId에 해당하는 댓글 목록을 가져오는 메소드
    List<FreeboardComment> findByFreeboardId(Long freeboardId);
}
