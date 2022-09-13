package com.cNerds.dailyMoment.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cNerds.dailyMoment.core.util.FileUtil;

@RestController
@RequestMapping("/image/")
public class FileController {
	
	@Autowired
	private FileUtil fileService;
	
	
	@GetMapping(value="user/{imageName}")
	public ResponseEntity<Resource> displayUser(@PathVariable("imageName") String imageName) {
		int fileLoadType = 1;
		Resource resource = fileService.loadFile(fileLoadType, imageName);
		if(!resource.exists()) 
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		HttpHeaders header = new HttpHeaders();
		Path filePath = null;
		try{
			filePath = Paths.get(resource.getFile().getAbsolutePath());
			header.add("Content-type", Files.probeContentType(filePath));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	
	@GetMapping(value="children/{imageName}")
	public ResponseEntity<Resource> displayChildren(@PathVariable("imageName") String imageName) {
		int fileLoadType = 2;
		Resource resource = fileService.loadFile(fileLoadType, imageName);
		if(!resource.exists()) 
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		HttpHeaders header = new HttpHeaders();
		Path filePath = null;
		try{
			filePath = Paths.get(resource.getFile().getAbsolutePath());
			header.add("Content-type", Files.probeContentType(filePath));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	
	@GetMapping(value="photo/{imageName}")
	public ResponseEntity<Resource> displayPhoto(@PathVariable("imageName") String imageName) {
		int fileLoadType = 3;
		Resource resource = fileService.loadFile(fileLoadType, imageName);
		if(!resource.exists()) 
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		HttpHeaders header = new HttpHeaders();
		Path filePath = null;
		try{
			filePath = Paths.get(resource.getFile().getAbsolutePath());
			header.add("Content-type", Files.probeContentType(filePath));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}
	
}
