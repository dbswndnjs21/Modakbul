package com.modakbul.repository.freeboard;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modakbul.entity.image.FreeboardImage;

public interface FreeboardImageRepository extends JpaRepository<FreeboardImage, Long> {

}
