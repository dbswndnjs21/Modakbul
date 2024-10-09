package com.modakbul.repository.freeboard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.image.FreeboardImage;

public interface FreeboardImageRepository extends JpaRepository<FreeboardImage, Long> {
	// 특정 게시글 ID에 연결된 이미지를 찾는 메서드
    List<FreeboardImage> findByFreeboardId(Long freeboardId);
    List<FreeboardImage> findByFreeboardIdOrderByImageOrderAsc(Long freeboardId);
    FreeboardImage findBySaveFileName(String saveFileName);
    void deleteByFreeboard(Freeboard freeboard);
    List<FreeboardImage> findByFreeboard(Freeboard freeboard);
}
