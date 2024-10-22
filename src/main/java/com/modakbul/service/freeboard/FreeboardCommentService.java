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
	
	
	
	public void deleteComment(long id) {
		freeboardCommentRepository.deleteById(id);
	}
	public List<FreeboardCommentDto> findAllComments() {
		log.info("Fetching all comments");

		// 모든 댓글 조회
		List<FreeboardComment> comments = freeboardCommentRepository.findAll();

		// 댓글을 DTO로 변환
		return comments.stream()
				.map(comment -> new FreeboardCommentDto(
						comment.getId(),
						comment.getParent() != null ? comment.getParent().getId() : null,
						null, // 자식 댓글 처리
						comment.getFreeboard().getId(),
						comment.getMember().getId(),
						comment.getContent(),
						comment.getCreatedAt(),
						comment.getMember().getUserId()
				))
				.collect(Collectors.toList());
	}


}
