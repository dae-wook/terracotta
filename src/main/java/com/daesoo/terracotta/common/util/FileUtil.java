package com.daesoo.terracotta.common.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.daesoo.terracotta.common.entity.Image;
import com.daesoo.terracotta.post.dto.FileNameDto;
import com.daesoo.terracotta.schematic.util.SchematicDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	@Value("${GCP_IMAGE_DIR_NAME}")
	private String imageDirectory;
	
	private final ObjectMapper mapper = new ObjectMapper();

    public FileNameDto uploadFile(SchematicDto schematicDto, MultipartFile schematic) {
        try (InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream()) {

            String schematicJson = mapper.writeValueAsString(schematicDto);

            String originalSchematicFileName = schematic.getOriginalFilename();
            String fileName = originalSchematicFileName.substring(0, originalSchematicFileName.lastIndexOf('.'));

            log.debug("업로드 시작");

            StorageOptions options = StorageOptions.newBuilder()
                    .setProjectId(gcpProjectId)
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            Storage storage = options.getService();
            Bucket schematicBucket = storage.get(gcpBucketId, Storage.BucketGetOption.fields());

            Instant instant = Instant.now();
            long currentTimeMillis = instant.toEpochMilli();

            String saveSchematicFileName = fileName + "-s" + currentTimeMillis + checkFileExtension(originalSchematicFileName);

            Blob schematicBlob = schematicBucket.create(directory + "/" + saveSchematicFileName, schematicJson.getBytes(), schematic.getContentType());

            if (schematicBlob != null) {
                log.debug("업로드 성공");
                return new FileNameDto(saveSchematicFileName, null);
            }

        } catch (Exception e) {
            log.error("GCS에 저장 중 에러 발생", e);
            throw new IllegalArgumentException("GCS에 저장 중 에러 발생");
        }

        throw new IllegalArgumentException("GCS에 저장 중 에러 발생");
    }
	
	public ArrayList<String> uploadImages(MultipartFile[] images) {

		try{
			log.debug("업로드 시작");

			InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();
			
			StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
					.setCredentials(GoogleCredentials.fromStream(inputStream)).build();

			Storage storage = options.getService();
			Bucket imageBucket = storage.get(gcpBucketImageId,Storage.BucketGetOption.fields());
			
			Instant instant = Instant.now();
			long currentTimeMillis = instant.toEpochMilli();
			
			ArrayList<String> imageNames = new ArrayList<>();
			for(MultipartFile image : images) {
				String originalImageFileName = image.getOriginalFilename();
				String imageFileName = originalImageFileName.substring(0, originalImageFileName.lastIndexOf('.'));
				

				byte[] imageFileData = image.getBytes();

				String saveImageFileName = imageFileName + "-" + currentTimeMillis + checkImageFileExtension(originalImageFileName);
				imageNames.add(saveImageFileName);

				Blob imageBlob = imageBucket.create(imageDirectory + "/" + saveImageFileName, imageFileData, image.getContentType());
			}



			if(imageNames.size() > 0){
				log.debug("업로드 성공");
				return imageNames;
			}

		}catch (Exception e){
			e.printStackTrace();
			throw new IllegalArgumentException("GCS에 저장 중 에러 발생");
		}
		throw new IllegalArgumentException("GCS에 저장 중 에러 발생");
	}
	
	public String updateSchematic(SchematicDto schematicDto, MultipartFile schematic) {

		try{
			ObjectMapper mapper = new ObjectMapper();
			String schematicJson = mapper.writeValueAsString(schematicDto);

			String originalSchematicFileName = schematic.getOriginalFilename();
			String fileName = originalSchematicFileName.substring(0, originalSchematicFileName.lastIndexOf('.'));


			log.debug("업로드 시작");
			byte[] schematicFileData = schematicJson.getBytes();

			InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();

			StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
					.setCredentials(GoogleCredentials.fromStream(inputStream)).build();

			Storage storage = options.getService();
			Bucket schematicBucket = storage.get(gcpBucketId,Storage.BucketGetOption.fields());

			Instant instant = Instant.now();
			long currentTimeMillis = instant.toEpochMilli();

			String saveSchematicFileName = fileName + "-s" + currentTimeMillis + checkFileExtension(originalSchematicFileName);
			

			Blob schematicBlob = schematicBucket.create(directory + "/" + saveSchematicFileName, schematicFileData, schematic.getContentType());
			

			if(schematicBlob != null){
				log.debug("업로드 성공");
				return saveSchematicFileName;
			}

		}catch (Exception e){
			e.printStackTrace();
			throw new IllegalArgumentException("GCS에 저장 중 에러 발생");
		}
		throw new IllegalArgumentException("GCS에 저장 중 에러 발생");
	}
	
	private void deleteImages(List<Image> fileNames) {
	    try {
	        InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();

	        StorageOptions options = StorageOptions.newBuilder()
	            .setProjectId(gcpProjectId)
	            .setCredentials(GoogleCredentials.fromStream(inputStream))
	            .build();

	        Storage storage = options.getService();
	        Bucket imageBucket = storage.get(gcpBucketImageId, Storage.BucketGetOption.fields());

	        for (Image fileName : fileNames) {
	            Blob imageBlob = imageBucket.get(imageDirectory + "/" + fileName.getPath());
	            if (imageBlob != null) {
	                imageBlob.delete();
	                log.debug("이미지 {} 삭제 성공", fileName);
	            } else {
	                log.warn("이미지 {} 찾을 수 없음", fileName);
	            }
	        }
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new IllegalArgumentException("Error occurred while deleting files from GCS");
	    }
	}
	
	public void deleteFilesExcept(List<String> excludeFileNames) {
        try (InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream()) {
            StorageOptions options = StorageOptions.newBuilder()
                    .setProjectId(gcpProjectId)
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            Storage storage = options.getService();
            Bucket bucket = storage.get(gcpBucketImageId, Storage.BucketGetOption.fields());

            Set<String> excludeFileNameSet = excludeFileNames.stream().collect(Collectors.toSet());

            Iterable<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(imageDirectory)).iterateAll();
            List<Blob> filesToDelete = StreamSupport.stream(blobs.spliterator(), false)
                    .filter(blob -> !excludeFileNameSet.contains(blob.getName()))
                    .collect(Collectors.toList());

            for (Blob blob : filesToDelete) {
                blob.delete();
                log.info("파일 삭제: " + blob.getName());
            }

        } catch (Exception e) {
            log.error("GCS 파일 삭제 중 에러 발생", e);
            throw new IllegalArgumentException("GCS 파일 삭제 중 에러 발생");
        }
    }
	
	public void deleteSchematicExcept(List<String> excludeFileNames) {
        try (InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream()) {
            StorageOptions options = StorageOptions.newBuilder()
                    .setProjectId(gcpProjectId)
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            Storage storage = options.getService();
            Bucket bucket = storage.get(gcpBucketId, Storage.BucketGetOption.fields());

            Set<String> excludeFileNameSet = excludeFileNames.stream().collect(Collectors.toSet());

            Iterable<Blob> blobs = bucket.list(Storage.BlobListOption.prefix(directory)).iterateAll();
            List<Blob> filesToDelete = StreamSupport.stream(blobs.spliterator(), false)
                    .filter(blob -> !excludeFileNameSet.contains(blob.getName()))
                    .collect(Collectors.toList());

            for (Blob blob : filesToDelete) {
                blob.delete();
                log.info("파일 삭제: " + blob.getName());
            }

        } catch (Exception e) {
            log.error("GCS 파일 삭제 중 에러 발생", e);
            throw new IllegalArgumentException("GCS 파일 삭제 중 에러 발생");
        }
    }
	
	public Boolean deleteImagesByImageName(String[] fileNames) {
	    try {
	        InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();

	        StorageOptions options = StorageOptions.newBuilder()
	            .setProjectId(gcpProjectId)
	            .setCredentials(GoogleCredentials.fromStream(inputStream))
	            .build();

	        Storage storage = options.getService();
	        Bucket imageBucket = storage.get(gcpBucketImageId, Storage.BucketGetOption.fields());

	        for (String fileName : fileNames) {
	            Blob imageBlob = imageBucket.get(imageDirectory + "/" + fileName);
	            if (imageBlob != null) {
	                imageBlob.delete();
	                log.debug("이미지 {} 삭제 성공", fileName);
	            } else {
	                log.warn("이미지 {} 찾을 수 없음", fileName);
	            }
	        }
	        return true;
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
//	        throw new IllegalArgumentException("Error occurred while deleting files from GCS");
	    }
	}
	
	public ArrayList<String> updateImage(MultipartFile[] images, List<Image> oldImageName) {

		try{
			
			log.debug("업로드 시작");

			InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();

			StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
					.setCredentials(GoogleCredentials.fromStream(inputStream)).build();

			Storage storage = options.getService();
			Bucket imageBucket = storage.get(gcpBucketImageId,Storage.BucketGetOption.fields());

			Instant instant = Instant.now();
			long currentTimeMillis = instant.toEpochMilli();

			List<Blob> imageBlob = new ArrayList<>();
			ArrayList<String> imageNames = new ArrayList<>();
			
			for(MultipartFile image : images) {
				String originalImageFileName = image.getOriginalFilename();
				String imageFileName = originalImageFileName.substring(0, originalImageFileName.lastIndexOf('.'));

				byte[] imageFileData = image.getBytes();

				String saveImageFileName = imageFileName + "-" + currentTimeMillis + checkImageFileExtension(originalImageFileName);

				imageBlob.add(imageBucket.create(imageDirectory + "/" + saveImageFileName, imageFileData, image.getContentType()));
				imageNames.add(saveImageFileName);
			}
			
			if(imageBlob.size() > 0){

				
				deleteImages(oldImageName);
				log.debug("업로드 성공");
				return imageNames;
			}

		}catch (Exception e){
			e.printStackTrace();
			throw new IllegalArgumentException("GCS에 저장 중 에러 발생");
		}
		throw new IllegalArgumentException("GCS에 저장 중 에러 발생");
	}


	private String checkFileExtension(String fileName) {
		if(fileName != null && fileName.contains(".")){
			String[] extensionList = {".schem", ".litematic", ".schematic"};

			for(String extension: extensionList) {
				if (fileName.endsWith(extension)) {
					log.debug("허용된 파일 확장자 : {}", extension);
					return ".json";
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
	
	public SchematicDto downloadSchematicFileToSchematicDto(String fileName) {
		try {
			InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();


			Storage storage = StorageOptions.newBuilder().setProjectId(gcpProjectId).setCredentials(GoogleCredentials.fromStream(inputStream)).build().getService();
			byte[] content = storage.readAllBytes(gcpBucketId, "schematics/" + fileName);
			
			
			ObjectMapper mapper = new ObjectMapper();
            SchematicDto schematicDto = mapper.readValue(new String(content, StandardCharsets.UTF_8), SchematicDto.class);
            
			return schematicDto;

		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}