package com.wd.pydjc.sys.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.wd.pydjc.sys.model.FileInfo;

public interface FileService {

	FileInfo save(MultipartFile file) throws IOException;

	void delete(String id);

}
