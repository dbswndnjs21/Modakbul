package com.modakbul.repository.freeboard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.modakbul.entity.freeboard.FreeboardComment;

public interface FreeboardCommentRepository extends JpaRepository<FreeboardComment, Long> {
	// boardId에 해당하는 댓글 목록을 가져오는 메소드
    List<FreeboardComment> findByFreeboardId(Long freeboardId);
   
    @Query(value = "WITH RECURSIVE comment_tree AS ( " +
            "SELECT id, content, created_at, freeboard_id, member_id, parent_id, 0 AS depth " +
            "FROM freeboard_comment WHERE freeboard_id = :freeboardId AND parent_id IS NULL " +
            "UNION ALL " +
            "SELECT fc.id, fc.content, fc.created_at, fc.freeboard_id, fc.member_id, fc.parent_id, ct.depth + 1 AS depth " +
            "FROM freeboard_comment fc " +
            "JOIN comment_tree ct ON fc.parent_id = ct.id) " +
            "SELECT * FROM comment_tree " +
            "ORDER BY CASE WHEN depth = 0 THEN id ELSE parent_id END, depth, created_at " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<FreeboardComment> findCommentTree(@Param("freeboardId") Long freeboardId, @Param("limit") int limit, @Param("offset") int offset);

    Long countByFreeboardId(Long freeboardId);
    
    
}
