package com.modakbul.service.freeboard;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.modakbul.dto.freeboard.FreeboardCommentDto;
import com.modakbul.entity.freeboard.FreeboardComment;
import com.modakbul.repository.freeboard.FreeboardCommentRepository;
import com.modakbul.repository.freeboard.FreeboardRepository;
import com.modakbul.repository.member.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FreeboardCommentService {
	private final FreeboardCommentRepository freeboardCommentRepository;
	private final FreeboardRepository freeboardRepository;
	private final MemberRepository memberRepository;
	
	public void writeComment(FreeboardCommentDto commentDto) {
	    // 댓글 엔티티 생성
	    FreeboardComment comment = commentDto.toEntity(commentDto.getMemberId(), commentDto.getFreeboardId());
	    
	    // 부모 댓글이 있는 경우
	    if (commentDto.getParentId() != null) {
	        FreeboardComment parentComment = freeboardCommentRepository.findById(commentDto.getParentId())
	            .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
	        comment.setParent(parentComment); // 부모 댓글 설정
	    }

	    // 댓글 저장
	    freeboardCommentRepository.save(comment);
	}
	
	public List<FreeboardCommentDto> findCommentsByfreeBoardId(Long freeboardId) {
	    		
	    // FreeboardComment를 FreeboardCommentDto로 변환
	    return freeboardCommentRepository.findByFreeboardId(freeboardId).stream()
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
	}
}
