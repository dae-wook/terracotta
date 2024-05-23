package com.daesoo.terracotta.image;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.entity.Image;
import com.daesoo.terracotta.common.repository.ImageRepository;
import com.daesoo.terracotta.common.util.FileUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
	
	private final FileUtil fileUtil;
	private final ImageRepository imageRepository;

	public UploadedImageResponseDto uploadImages(MultipartFile[] images) {
		// TODO Auto-generated method stub
		if(images.length <= 0) {
			throw new IllegalArgumentException(ErrorMessage.IMAGE_NOT_FOUND.getMessage());
		}
		return UploadedImageResponseDto.of(fileUtil.uploadImages(images));
	}

	public Boolean deleteImages(String[] imageNames) {
		// TODO Auto-generated method stub
		
		return fileUtil.deleteImagesByImageName(imageNames);
	}

	public int deleteFilesExcept() {
		// TODO Auto-generated method stub
		List<Image> images = imageRepository.findAll();
		List<String> imageNames = new ArrayList<>();
		for(Image image : images) {
			imageNames.add("schematic/thumbs/" + image.getPath());
		}
		fileUtil.deleteFilesExcept(imageNames);
		return 0;
	}

}
