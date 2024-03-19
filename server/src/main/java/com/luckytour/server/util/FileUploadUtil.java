package com.luckytour.server.util;

import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.exception.FileException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author qing
 * @date Created in 2024/1/13 14:41
 */
@Slf4j
@Component
public class FileUploadUtil {

	@Value("${storage.static.path}")
	private String staticPath;

	@Value("${storage.static.avatar.path}")
	private String avatarPath;

	@Value("${storage.folder.default-format}")
	private String folderFormat;

	@Value("${storage.static.blog.pic.path}")
	private String blogPicPath;

	@Value("${storage.static.blog.path}")
	private String blogPath;

	@PostConstruct
	public void init() {
		STATIC_PATH = staticPath;
		AVATAR_PATH = avatarPath;
		FOLDER_FORMAT = new SimpleDateFormat(folderFormat + ConstsPool.FILE_SEPARATOR);
		BLOG_PIC_PATH = blogPicPath;
		BLOG_PATH = blogPath;
	}

	/**
	 * 文件上传的本地存储路径
	 */
	private static String STATIC_PATH;

	/**
	 * 头像上传的本地存储路径
	 */
	private static String AVATAR_PATH;

	/**
	 * 文件默认按照时间作为文件夹存储
	 */
	private static Format FOLDER_FORMAT;

	/**
	 * 博客图片存储路径
	 */
	private static String BLOG_PIC_PATH;

	/**
	 * 博客存储路径
	 */
	private static String BLOG_PATH;

	private static String generateFolderPath(String prePath) {
		String innerFolder = FOLDER_FORMAT.format(new Date());
		return StringUtils.isBlank(prePath) ? prePath : (prePath + ConstsPool.FILE_SEPARATOR) + innerFolder;
	}

	private static String generateFileName(String originalName) {
		return UUID.randomUUID() + originalName.substring(originalName.lastIndexOf("."));
	}

	private static void createFolderIfNotExists(String folderPath) {
		File folder = new File(folderPath);
		if (!folder.isDirectory()) {
			log.debug("新建文件夹{}", folder);
			if (!folder.mkdirs()) {
				throw new FileException("文件夹创建失败", folder);
			}
		}
	}

	private static String storeFile(String prePath, byte[] fileBytes, String fileName) {
		String folderPath = generateFolderPath(prePath);
		createFolderIfNotExists(folderPath);

		Path destinationFile = Paths.get(folderPath, fileName);
		try {
			Files.write(destinationFile, fileBytes);
			String httpAddress = /*request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +*/ConstsPool.FILE_SEPARATOR + folderPath + fileName;
			log.debug("图片存储成功，真实文件位置: {} 网络文件地址: {}", folderPath + fileName, httpAddress);
			return httpAddress;
		} catch (IOException e) {
			throw new FileException("文件存储发生异常", e);
		}
	}

	private static String storeMultipartFile(String prePath, MultipartFile file) {
		try {
			byte[] fileBytes = file.getBytes();
			assert file.getOriginalFilename() != null;
			String fileName = generateFileName(file.getOriginalFilename());
			return storeFile(prePath, fileBytes, fileName);
		} catch (IOException e) {
			throw new FileException("文件读取发生异常", e);
		}
	}

	/**
	 * 文件上传
	 *
	 * @param prePath 文件存储的前缀路径
	 * @param file    文件
	 * @return 文件的网络访问地址
	 */
	/*private static String storeMultipartFile(String prePath, MultipartFile file) {
		// 在 uploadPath 文件夹中通过日期对上传的文件归类保存
		// 比如：/2019/06/06/test.png
		String innerFolder = FOLDER_FORMAT.format(new Date());
		String webFolderPath = StringUtils.isBlank(prePath) ? prePath : (prePath + ConstsPool.FILE_SEPARATOR) + innerFolder;
		String trueFolderPath = System.getProperty("user.dir") + ConstsPool.FILE_SEPARATOR + webFolderPath;
		File folder = new File(trueFolderPath);
		if (!folder.isDirectory()) {
			log.debug("新建文件夹{}", folder);
			if (!folder.mkdirs()) {
				throw new FileException("文件夹创建失败", folder);
			}
		}
		// 对上传的文件重命名，避免文件重名
		String oldName = file.getOriginalFilename();
		assert oldName != null;
		String newName = UUID.randomUUID() + oldName.substring(oldName.lastIndexOf("."));
		try {
			// 文件保存
			file.transferTo(new File(folder, newName));
			// 返回上传文件的访问路径
			String httpAddress = *//*request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +*//*ConstsPool.FILE_SEPARATOR + webFolderPath + newName;
			log.debug("图片存储成功，真实文件位置: {} 网络文件地址: {}", folder + ConstsPool.FILE_SEPARATOR + newName, httpAddress);
			return httpAddress;
		} catch (IOException e) {
			throw new FileException("文件存储发生异常", e);
		}
	}*/
	/*private static String storeBase64JpgFile(String prePath, String base64String) {
		// 解码Base64字符串
		byte[] decodedBytes = Base64.getDecoder().decode(base64String);

		// 创建新的文件名和文件路径
		String innerFolder = FOLDER_FORMAT.format(new Date());
		String webFolderPath = StringUtils.isBlank(prePath) ? prePath : (prePath + ConstsPool.FILE_SEPARATOR) + innerFolder;
		String trueFolderPath = System.getProperty("user.dir") + ConstsPool.FILE_SEPARATOR + webFolderPath;
		File folder = new File(trueFolderPath);
		if (!folder.isDirectory()) {
			log.debug("新建文件夹{}", folder);
			if (!folder.mkdirs()) {
				throw new FileException("文件夹创建失败", folder);
			}
		}
		String newName = UUID.randomUUID().toString() + ".png";

		// 将字节数组写入到新的文件中
		Path destinationFile = Paths.get(trueFolderPath, newName);
		try {
			Files.write(destinationFile, decodedBytes);
			String httpAddress = *//*request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +*//*ConstsPool.FILE_SEPARATOR + webFolderPath + newName;
			log.debug("图片存储成功，真实文件位置: {} 网络文件地址: {}", folder + ConstsPool.FILE_SEPARATOR + newName, httpAddress);
			return httpAddress;
		} catch (IOException e) {
			throw new FileException("文件存储发生异常", e);
		}
	}*/

	private static String storeBase64PngFile(String prePath, String base64String) {
		byte[] decodedBytes = Base64.getDecoder().decode(base64String);
		String fileName = generateFileName(".png");
		return storeFile(prePath, decodedBytes, fileName);
	}

	public static String storeStaticMultipartFile(String prePath, MultipartFile file) {
		return storeMultipartFile(STATIC_PATH + ConstsPool.FILE_SEPARATOR + prePath, file);
	}

	public static List<String> saveBlogPicsByUid(String uid, List<String> files) {
		return files.stream().map(file -> storeBase64PngFile(STATIC_PATH + ConstsPool.FILE_SEPARATOR + BLOG_PATH + ConstsPool.FILE_SEPARATOR + BLOG_PIC_PATH + ConstsPool.FILE_SEPARATOR + uid, file)).toList();
	}

	public static String storeAvatarByUid(String userId, MultipartFile file) {
		return storeStaticMultipartFile(AVATAR_PATH + ConstsPool.FILE_SEPARATOR + userId, file);
	}

}
