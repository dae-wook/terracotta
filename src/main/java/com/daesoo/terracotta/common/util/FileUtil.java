package com.daesoo.terracotta.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.Instant;

import org.apache.commons.io.FileUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FileUtil {

	@Value("${GCP_CREDENTIALS_LOCATION}")
	private String gcpConfigFile;

	@Value("${GCP_BUCKET}")
	private String gcpBucketId;
	
	@Value("${GCP_BUCKET_IMAGE}")
	private String gcpBucketImageId;

	@Value("${GCP_PROJECT_ID}")
	private String gcpProjectId;
	
	@Value("${GCP_DIR_NAME}")
	private String directory;


	public String uploadFile(MultipartFile multipartFile, String contentType) {

		try{

			String originalFileName = multipartFile.getOriginalFilename();
			String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
			log.debug("업로드 시작");
			byte[] fileData = multipartFile.getBytes();

			InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();

			StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
					.setCredentials(GoogleCredentials.fromStream(inputStream)).build();

			Storage storage = options.getService();
			Bucket bucket = storage.get(gcpBucketId,Storage.BucketGetOption.fields());

			Instant instant = Instant.now();
			long currentTimeMillis = instant.toEpochMilli();
			String saveFileName = fileName + "-" + currentTimeMillis + checkFileExtension(originalFileName);
			Blob blob = bucket.create(directory + "/" + saveFileName, fileData, contentType);

			if(blob != null){
				log.debug("업로드 성공");
				return saveFileName;
			}

		}catch (Exception e){
			throw new IllegalArgumentException("GCS에 저장 중 에러 발생");
		}
		throw new IllegalArgumentException("GCS에 저장 중 에러 발생");
	}
	
	public String uploadImage(MultipartFile multipartFile, String contentType) {

		try{

			String originalFileName = multipartFile.getOriginalFilename();
			String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
			log.debug("업로드 시작");
			byte[] fileData = multipartFile.getBytes();

			InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();

			StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
					.setCredentials(GoogleCredentials.fromStream(inputStream)).build();

			Storage storage = options.getService();
			Bucket bucket = storage.get(gcpBucketImageId,Storage.BucketGetOption.fields());		

			Instant instant = Instant.now();
			long currentTimeMillis = instant.toEpochMilli();
			String saveFileName = fileName + "-" + currentTimeMillis + checkImageFileExtension(originalFileName);
			Blob blob = bucket.create("schematic/thumbs/" + saveFileName, fileData, contentType);

			if(blob != null){
				log.debug("업로드 성공");
				return saveFileName;
			}

		}catch (Exception e){
			throw new IllegalArgumentException("이미지 저장 중 에러 발생");
		}
		throw new IllegalArgumentException("이미지 저장 중 에러 발생");
	}

	private File convertFile(MultipartFile file) {

		try{
			if(file.getOriginalFilename() == null){
				throw new BadRequestException("파일 이름이 비어있음");
			}
			File convertedFile = new File(file.getOriginalFilename());

			FileOutputStream outputStream = new FileOutputStream(convertedFile);
			outputStream.write(file.getBytes());
			outputStream.close();
			log.debug("변환된 파일 : {}", convertedFile);
			return convertedFile;
		}catch (Exception e){
			throw new IllegalArgumentException("변환 중 에러 발생");
		}
	}

	private String checkFileExtension(String fileName) {
		if(fileName != null && fileName.contains(".")){
			String[] extensionList = {".schem", ".litematic"};

			for(String extension: extensionList) {
				if (fileName.endsWith(extension)) {
					log.debug("허용된 파일 확장자 : {}", extension);
					return extension;
				}
			}
		}
		log.error("허용되지 않은 파일 형식");
		throw new IllegalArgumentException("허용되지 않은 파일 형식");
	}
	
	private String checkImageFileExtension(String fileName) {
		if(fileName != null && fileName.contains(".")){
			String[] extensionList = {".png", ".jpg", ".webp"};

			for(String extension: extensionList) {
				if (fileName.endsWith(extension)) {
					log.debug("허용된 파일 확장자 : {}", extension);
					return extension;
				}
			}
		}
		log.error("허용되지 않은 파일 형식");
		throw new IllegalArgumentException("허용되지 않은 파일 형식");
	}

	
	public InputStream downloadSchematicFileToInputStream(String fileName) {
		try {
			InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();
			
			
			Storage storage = StorageOptions.newBuilder().setProjectId(gcpProjectId).setCredentials(GoogleCredentials.fromStream(inputStream)).build().getService();
			byte[] content = storage.readAllBytes(gcpBucketId, "schematics/" + fileName);
			
			
			return new ByteArrayInputStream(content);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}