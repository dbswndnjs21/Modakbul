package com.modakbul.service.freeboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modakbul.entity.freeboard.Freeboard;
import com.modakbul.entity.image.FreeboardImage;
import com.modakbul.repository.freeboard.FreeboardImageRepository;
import com.modakbul.repository.freeboard.FreeboardRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FreeboardService {
	@Autowired FreeboardRepository freeboardRepository;
	@Autowired FreeboardImageRepository freeboardImageRepository;
	
		
	public void saveFreeboard(Freeboard freeboard, List<FreeboardImage> images) {
		freeboardRepository.save(freeboard);
		
		for(FreeboardImage image : images) {
			image.setFreeboard(freeboard);
			freeboardImageRepository.save(image);
		}
	}
}
