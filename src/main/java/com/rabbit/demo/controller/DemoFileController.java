package com.rabbit.demo.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rabbit.common.util.file.FileUtils;
import com.rabbit.framework.web.domain.AjaxResult;
import com.rabbit.system.constant.ConfigConstants;
import com.rabbit.system.service.ISysConfigService;

@RestController
@RequestMapping("/demo/file")
public class DemoFileController {
	@Autowired
	ISysConfigService configService;

	private static final Map<String, String> fileDataBase = new HashMap<String, String>();

	@PostMapping
	public AjaxResult fileUpload(MultipartFile file) {
		// 文件存储的根目录
		String basepath = (String) configService.selectByConfigKeyFromCache(ConfigConstants.KEY_OF_FILE_SAVE_DIR);
		// 原始文件名
		String rawFileName = file.getOriginalFilename();
		// 获得原始文件的后缀
		String suffix = rawFileName.substring(rawFileName.lastIndexOf("."));
		// 生成服务器磁盘存储的文件名
		String diskFileName = UUID.randomUUID().toString().replace("-", "") + suffix;
		// 拼凑全路径
		String fullFilePathOnDisk = String
				.valueOf(new StringBuffer(String.valueOf(basepath)).append(File.separator).append(diskFileName));

		// 目标存储文件
		File destFile = new File(fullFilePathOnDisk);

		try {
			// 保存文件
			file.transferTo(destFile);

			fileDataBase.put(diskFileName, fullFilePathOnDisk);
			// 构造返回参数
			Map<String, String> data = new HashMap<String, String>();
			data.put("rawFileName", rawFileName);
			data.put("diskFileName", diskFileName);
			return AjaxResult.success("文件上传成功", data);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("文件处理异常");
		}

	}

	@GetMapping("/download/{filename}")
	public void fileDownload(@PathVariable String filename, HttpServletResponse response) {
		if (fileDataBase.containsKey(filename)) {
			String fullFilePathOnDisk = fileDataBase.get(filename);
			FileUtils.streamDownload(response, filename, fullFilePathOnDisk);
		}
	}

	@DeleteMapping("/{filename}")
	public AjaxResult deleteFile(@PathVariable String filename) {
		if (!fileDataBase.containsKey(filename)) {
			return AjaxResult.error("文件不存在");
		}
		String fullFilePathOnDisk = fileDataBase.get(filename);
		File file = new File(fullFilePathOnDisk);
		file.delete();
		return AjaxResult.success();
	}

}
