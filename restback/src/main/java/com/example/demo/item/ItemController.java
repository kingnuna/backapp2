package com.example.demo.item;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ItemController {
	@Autowired
	private ItemService service;

	@Value("${spring.servlet.multipart.location}")
	private String path;

	@PostMapping("/item")
	public Map add(ItemDto dto) {
		boolean flag = false;
		String fname = dto.getF().getOriginalFilename();
		File f = new File(path + fname);
		try {
			dto.getF().transferTo(f);// 업로드 파일 복사
			dto.setPath(fname);
			System.out.println("add:" + dto);
			service.save(dto);
			flag = true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map map = new HashMap();
		map.put("flag", flag);
		return map;
	}

	@GetMapping("/item")
	public Map list() {
		Map map = new HashMap();
		map.put("list", service.getAll());
		return map;
	}

	@GetMapping("/read-img/{fname}")
	public ResponseEntity<byte[]> read_img(@PathVariable("fname") String fname) {
		System.out.println("read-img/fname:"+fname);
		ResponseEntity<byte[]> result = null;
		File f = new File(path + fname);
		System.out.println("read-img/path:"+path + fname);
		System.out.println("read-img/isexist?:"+f.exists());
		HttpHeaders header = new HttpHeaders(); // import org.springframework.http.HttpHeaders;
		try {
			header.add("Content-Type", Files.probeContentType(f.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(f), header, HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
