package com.cNerds.dailyMoment.core.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Component
public class FileUtil {
	@Value("${spring.file.upload.location}")
	private String uploadImagePath;
	
    public Boolean saveFile (MultipartFile file , int fileSaveType, String fileName) {
    	
    	Boolean success = false;
    	Date date = new Date();
        StringBuilder sb = new StringBuilder();
        // 파일 이름 저장
        sb.append(fileName);

        String fileTypePath = this.fileType(fileSaveType);
        if (!file.isEmpty()) {
    	    File dest = new File(uploadImagePath+ fileTypePath + "\\"+ sb.toString());
          try {
             file.transferTo(dest);
             success = true;
          } catch (IllegalStateException e) {
             e.printStackTrace();           
          } catch (IOException e) {
             e.printStackTrace();
          }
        }
        
        return success;
    }
    
    public Resource loadFile(int fileLoadType, String fileName) {
    	  String fileTypePath = this.fileType(fileLoadType);

    	Resource resource = new FileSystemResource(uploadImagePath+ fileTypePath + "\\" + fileName);
    	return resource;
    }
    
    
    public String fileType(int fileType) {
    	String fileTypePath ="";
    	switch(fileType){
			case 1 : fileTypePath = "user"; break;
			case 2 : fileTypePath = "children";break;
			case 3 : fileTypePath = "photo";break;		
		}
    	return fileTypePath;
    }
    
    public String readFile(File file, String encoding) {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
						new InputStreamReader(
								new FileInputStream(file), encoding));
			String line = null;
			while ((line = reader.readLine()) != null){
				builder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeQuietly(reader);
		}
		return builder.toString();
	}
    
    private void closeQuietly(Closeable c) {
		if (c != null) {
			try {
				c.close();
			} catch (IOException ignored) {}
		}
	}
}
