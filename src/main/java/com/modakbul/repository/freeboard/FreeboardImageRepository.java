package com.modakbul.repository.freeboard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.image.FreeboardImage;

public interface FreeboardImageRepository extends JpaRepository<FreeboardImage, Long> {
    // 특정 게시글 ID에 연결된 이미지를 찾는 메서드
    List<FreeboardImage> findByFreeboardId(Long freeboardId);

    // 특정 게시글 ID에 연결된 이미지를 이미지 순서에 따라 정렬하여 찾는 메서드
    List<FreeboardImage> findByFreeboardIdOrderByImageOrderAsc(Long freeboardId);

    // 저장된 파일 이름으로 이미지 찾기
    FreeboardImage findBySaveFileName(String saveFileName);

    // 특정 게시글에 연결된 모든 이미지를 삭제하는 메서드
    void deleteByFreeboard(Freeboard freeboard);

    // 특정 게시글에 연결된 이미지를 찾는 메서드
    List<FreeboardImage> findByFreeboard(Freeboard freeboard);

    // 파일 이름과 게시글 ID로 이미지를 삭제하는 메서드
    void deleteBySaveFileNameAndFreeboardId(String saveFileName, Long freeboardId);
}
