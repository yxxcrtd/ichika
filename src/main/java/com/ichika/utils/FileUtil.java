package com.ichika.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

public class FileUtil {

    public static String uploadFile(MultipartFile file, String root) throws Exception {
        String originalName = file.getOriginalFilename();
        StringBuffer fileName = new StringBuffer(String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL" + originalName.substring(originalName.lastIndexOf(".")).toLowerCase(), new Date()));
        String dest = new StringBuffer(root).append(fileName).toString();
        File destFile = new File(dest);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        file.transferTo(destFile);
        return fileName.toString();
    }

}
