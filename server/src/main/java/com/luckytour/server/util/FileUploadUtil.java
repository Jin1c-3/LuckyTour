package com.luckytour.server.util;

import com.luckytour.server.common.constant.ConstsPool;
import com.luckytour.server.exception.FileException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	@PostConstruct
	public void init() {
		STATIC_PATH = staticPath;
		AVATAR_PATH = avatarPath;
		FOLDER_FORMAT = new SimpleDateFormat(folderFormat + ConstsPool.FILE_SEPARATOR);
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
	 * 文件上传
	 *
	 * @param request 请求
	 * @param prePath 文件存储的前缀路径
	 * @param file    文件
	 * @return 文件的网络访问地址
	 */
	public static String storeFile(HttpServletRequest request, String prePath, MultipartFile file) {
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
			String httpAddress = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + ConstsPool.FILE_SEPARATOR + webFolderPath + newName;
			log.debug("图片存储成功，真实文件位置: {} 网络文件地址: {}", folder + ConstsPool.FILE_SEPARATOR + newName, httpAddress);
			return httpAddress;
		} catch (IOException e) {
			throw new FileException("文件存储发生异常", e);
		}
	}

	public static String storeStaticFile(HttpServletRequest request, String prePath, MultipartFile file) {
		return storeFile(request, STATIC_PATH + ConstsPool.FILE_SEPARATOR + prePath, file);
	}

	public static String storeAvatar(HttpServletRequest request, String userId, MultipartFile file) {
		return storeStaticFile(request, AVATAR_PATH + ConstsPool.FILE_SEPARATOR + userId, file);
	}
}
