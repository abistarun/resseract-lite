package abistech.resseract.controller;

import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.Constants;
import abistech.resseract.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Service
class FileService {
	private final String rootLocation = createTempDirectory();

	public String store(MultipartFile file) throws ResseractException {
		try {
			String fileName = Objects.requireNonNull(file.getOriginalFilename()).split(Constants.CSV)[0];
			File newFile = new File(String.join(File.separator, rootLocation, fileName));
			int cnt = 0;
			while (newFile.exists()) {
				newFile = new File(String.join(File.separator, rootLocation, fileName + "-" + cnt));
				cnt++;
			}
			Path target = newFile.toPath();
			Files.copy(file.getInputStream(), target);
			return target.toString();
		} catch (Exception e) {
			throw new ResseractException(CustomErrorReports.FILE_UPLOAD_ERROR, e);
		}
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	private String createTempDirectory() {
		File tempDirectory = new File(Objects.requireNonNull(Util.getTempFolderLocation()));
		tempDirectory.mkdirs();
		return tempDirectory.getAbsolutePath();
	}
}