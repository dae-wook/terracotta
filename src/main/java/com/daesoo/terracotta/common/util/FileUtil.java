package com.daesoo.terracotta.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

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

	public String[] uploadFile(MultipartFile schematic, MultipartFile image) {

		try{

			String originalSchematicFileName = schematic.getOriginalFilename();
			String fileName = originalSchematicFileName.substring(0, originalSchematicFileName.lastIndexOf('.'));

			String originalImageFileName = image.getOriginalFilename();
			String imageFileName = originalImageFileName.substring(0, originalImageFileName.lastIndexOf('.'));

			log.debug("업로드 시작");
			byte[] schematicFileData = schematic.getBytes();
			byte[] imageFileData = image.getBytes();

			InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();

			StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
					.setCredentials(GoogleCredentials.fromStream(inputStream)).build();

			Storage storage = options.getService();
			Bucket schematicBucket = storage.get(gcpBucketId,Storage.BucketGetOption.fields());
			Bucket imageBucket = storage.get(gcpBucketImageId,Storage.BucketGetOption.fields());

			Instant instant = Instant.now();
			long currentTimeMillis = instant.toEpochMilli();

			String saveSchematicFileName = fileName + "-s" + currentTimeMillis + checkFileExtension(originalSchematicFileName);
			String saveImageFileName = imageFileName + "-" + currentTimeMillis + checkImageFileExtension(originalImageFileName);

			Blob schematicBlob = schematicBucket.create(directory + "/" + saveSchematicFileName, schematicFileData, schematic.getContentType());
			Blob imageBlob = imageBucket.create("schematic/thumbs/" + saveImageFileName, imageFileData, image.getContentType());

			if(schematicBlob != null && imageBlob != null){
				log.debug("업로드 성공");
				return new String[]{saveSchematicFileName, saveImageFileName};
			}

		}catch (Exception e){
			e.printStackTrace();
			throw new IllegalArgumentException("GCS에 저장 중 에러 발생");
		}
		throw new IllegalArgumentException("GCS에 저장 중 에러 발생");
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
			String[] extensionList = {".png", ".jpg", ".webp", ".jpeg"};

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
	
	public File downloadSchematicFileToFile(String fileName) {
	    try {
	        InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();

	        Storage storage = StorageOptions.newBuilder().setProjectId(gcpProjectId).setCredentials(GoogleCredentials.fromStream(inputStream)).build().getService();
	        byte[] content = storage.readAllBytes(gcpBucketId, "schematics/" + fileName);

	        // Create a temporary file to write the content
	        Path tempFilePath = Files.createTempFile("temp-", ".schematic");
	        File tempFile = tempFilePath.toFile();

	        // Write content to the temporary file
	        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
	            fos.write(content);
	        }

	        return tempFile;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}


}