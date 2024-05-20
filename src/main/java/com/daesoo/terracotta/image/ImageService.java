package com.daesoo.terracotta.image;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.daesoo.terracotta.common.dto.ErrorMessage;
import com.daesoo.terracotta.common.util.FileUtil;
import com.daesoo.terracotta.post.dto.ImageResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
	
	private final FileUtil fileUtil;

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

}
