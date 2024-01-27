package com.daesoo.terracotta.schematic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.schematic.util.Schematic;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3737, http://localhost:8080")
@CrossOrigin(origins = "*")
public class SchematicController {
	
	private final SchematicService mainService;
	
	
	@GetMapping("/api/scheme")
	public ResponseDto<Schematic> hello(@RequestBody String fileName) {
		System.out.println(fileName);
		return ResponseDto.success(HttpStatus.OK,mainService.getShematic(fileName));
	}
	
}
