package com.modakbul.service.freeboard;

import java.util.List;
import java.util.stream.Collectors;

import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.member.Member;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.modakbul.dto.freeboard.FreeboardCommentDto;
import com.modakbul.entity.freeboard.FreeboardComment;
import com.modakbul.repository.freeboard.FreeboardCommentRepository;
import com.modakbul.repository.freeboard.FreeboardRepository;
import com.modakbul.repository.member.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FreeboardCommentService {
	private final FreeboardCommentRepository freeboardCommentRepository;
	private final FreeboardRepository freeboardRepository;
	private final MemberRepository memberRepository;
//	public FreeboardComment toEntity(Freeboard freeboard, Member member, FreeboardComment parentComment) {
	public void writeComment(FreeboardCommentDto commentDto) {
		Freeboard freeboard = new Freeboard();
		freeboard.setId(commentDto.getFreeboardId());
		Member member = new Member();
		member.setId(commentDto.getMemberId());
		FreeboardComment parent = new FreeboardComment();
		parent.setId(commentDto.getParentId());

	    // 댓글 엔티티 생성
	    FreeboardComment comment = commentDto.toEntity(freeboard,member, parent);
	    
	    // 부모 댓글이 있는 경우
	    if (commentDto.getParentId() != null) {
	        FreeboardComment parentComment = freeboardCommentRepository.findById(commentDto.getParentId())
	            .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
	        comment.setParent(parentComment); // 부모 댓글 설정
	    }else {
	        // 부모 댓글이 없는 경우 (최상위 댓글)
	        comment.setParent(null); // null로 설정 (필수는 아니지만 명시적으로 null 처리)
	    }
	    
	    // 댓글 저장
	    freeboardCommentRepository.save(comment);
	}
	
	public Page<FreeboardCommentDto> findCommentsByfreeBoardId(Long freeboardId, int page, int size) {
		if (page < 1) {
	        page = 1; // 페이지는 1 이상이어야 함
	    }
		log.info("Fetching comments for boardId: {}, page: {}, size: {}", freeboardId, page, size);
		// 페이지에 맞는 limit와 offset 계산
	    int limit = size;
	    int offset = (page - 1) * size;
	    
	    // 댓글 트리 조회
	    List<FreeboardComment> comments = freeboardCommentRepository.findCommentTree(freeboardId, limit, offset);
	    
	    // 전체 댓글 수 조회
	    Long totalRowCount = freeboardCommentRepository.countByFreeboardId(freeboardId);
	    
	    // 댓글을 DTO로 변환
	    List<FreeboardCommentDto> commentDtos = comments.stream()
	            .map(comment -> new FreeboardCommentDto(
	                    comment.getId(),
	                    comment.getParent() != null ? comment.getParent().getId() : null,
	                    null, // 자식 댓글 처리 (예: 나중에 추가 가능)
	                    comment.getFreeboard().getId(),
	                    comment.getMember().getId(),
	                    comment.getContent(),
	                    comment.getCreatedAt(),
	                    comment.getMember().getUserId()
	            ))
	            .collect(Collectors.toList());

	    // 페이징 정보를 포함한 Page 반환
	    return new PageImpl<>(commentDtos, PageRequest.of(page - 1, size), totalRowCount);
	}
	
	
	
	public void deleteComment(long id) {
		freeboardCommentRepository.deleteById(id);
	} 
}
