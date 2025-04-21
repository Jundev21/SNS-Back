package com.sns.sns.service.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.sns.sns.service.common.exception.BasicException;
import com.sns.sns.service.common.exception.ErrorCode;

@Component
public class GoogleDriveConfig {
	private final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private final String GOOGLE_KEY_PATH = getGoogleKeyPath();
	@Value("${google.folder-id}")
	private String GOOGLE_FOLDER_ID;
	@Value("${google.application-name}")
	private String APPLICATION_NAME;

	public String uploadImageToGoogleDrive(MultipartFile imageRequest) {
		if(imageRequest == null || imageRequest.isEmpty()) return "";
		try {
			java.io.File tempFile = java.io.File.createTempFile("tempFile", ".png");
			imageRequest.transferTo(tempFile);
			Drive drive = connectGoogleDrive();
			com.google.api.services.drive.model.File fileData = new com.google.api.services.drive.model.File();
			fileData.setName(tempFile.getName());
			fileData.setParents(Collections.singletonList(GOOGLE_FOLDER_ID));
			FileContent mediaContent = new FileContent("image/jpeg", tempFile);

			com.google.api.services.drive.model.File uploadFile = drive.files().create(fileData, mediaContent)
				.setFields("id").execute();
			String imageUrl = loadImageFromGoogleDrive(uploadFile.getId());
			tempFile.delete();
			return imageUrl;
		} catch (Exception e) {
			throw new BasicException(ErrorCode.FAILED_GOOGLE_IMAGE, e.toString());
		}
	}

	public String loadImageFromGoogleDrive(String fileId) {
		try {
			return "https://drive.google.com/thumbnail?id="+ fileId + "&sz=w1000";
		} catch (Exception e) {
			throw new BasicException(ErrorCode.FAILED_GOOGLE_IMAGE, ErrorCode.FAILED_GOOGLE_IMAGE.getMsg());
		}
	}

	public void deleteImageFromGoogleDrive(String fileId) {
		try {
			Drive drive = connectGoogleDrive();
			drive.files().delete(fileId).execute();
		} catch (Exception e) {
			throw new BasicException(ErrorCode.FAILED_GOOGLE_IMAGE, ErrorCode.FAILED_GOOGLE_IMAGE.getMsg());
		}
	}

	public void upDateImageFromGoogleDrive(String fileId, MultipartFile imageRequest) {
		try {
			java.io.File tempFile = java.io.File.createTempFile("tempFile", ".png");
			imageRequest.transferTo(tempFile);
			Drive drive = connectGoogleDrive();
			com.google.api.services.drive.model.File fileData = new com.google.api.services.drive.model.File();
			fileData.setName(tempFile.getName());
			fileData.setParents(Collections.singletonList(GOOGLE_FOLDER_ID));
			drive.files().update(fileId,fileData).execute();
		} catch (Exception e) {
			throw new BasicException(ErrorCode.FAILED_GOOGLE_IMAGE, ErrorCode.FAILED_GOOGLE_IMAGE.getMsg());
		}
	}

	private String getGoogleKeyPath() {
		String currentDirectory = System.getProperty("user.dir");
		Path filePath = Paths.get(currentDirectory, "/src/main/resources/googleCredential.json");
		return filePath.toString();
	}

	private Drive connectGoogleDrive() {
		try {
			InputStream credentialsStream = getClass().getClassLoader().getResourceAsStream("googleCredential.json");
			if (credentialsStream == null) {
				throw new IOException("Resource not found: googleCredential.json");
			}

			GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
				.createScoped(Collections.singleton(DriveScopes.DRIVE));

			return new Drive.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY,
				new HttpCredentialsAdapter(credentials))
				.setApplicationName(APPLICATION_NAME)
				.build();
		} catch (Exception e) {
			throw new BasicException(ErrorCode.FAILED_GOOGLE_AUTHENTICATION,
				ErrorCode.FAILED_GOOGLE_AUTHENTICATION.getMsg());
		}
	}

	private void checkIsEmptyFile(MultipartFile imageRequest) {
		if (imageRequest.isEmpty()) {
			throw new BasicException(ErrorCode.EMPTY_FILE, ErrorCode.EMPTY_FILE.getMsg());
		}
	}
}
