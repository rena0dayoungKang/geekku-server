package com.kosta.geekku.util;

import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.UserRepository;

@Component
public class ProfileImage {
	// 업로드 이미지를 byte[]로 변환해서 데이터베이스에 저장

	@Autowired
	private UserRepository userRepository;

	public void saveProfileImage(UUID userId, byte[] byteImage) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다"));

		 byte[] compressedImage = compressImage(byteImage, 100, 100);
		 user.setProfileImage(compressedImage);
		 userRepository.save(user);
	}

	public byte[] compressImage(byte[] imageBytes, int width, int height) {
		try {
			// 원본 이미지를 BufferedImage로 읽기
			ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
			BufferedImage originalImage = ImageIO.read(inputStream);

			// 새 크기의 BufferedImage 생성
			Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			// 새 이미지에 원본 이미지를 복사
			Graphics2D g2d = resizedImage.createGraphics();
			g2d.drawImage(scaledImage, 0, 0, null);
			g2d.dispose();

			// JPEG 포맷으로 압축
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, "jpeg", outputStream);

			return outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to compress image", e);
		}
	}
}
