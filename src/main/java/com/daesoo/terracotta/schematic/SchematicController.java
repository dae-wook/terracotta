package com.daesoo.terracotta.schematic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daesoo.terracotta.common.dto.ResponseDto;
import com.daesoo.terracotta.schematic.util.Schematic;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3737, http://localhost:8080")
@CrossOrigin(origins = "*")
public class SchematicController {
	
	private final SchematicService mainService;
	
	
	@GetMapping("/api/scheme")
	@ResponseBody
	public ResponseDto<Schematic> hello(@RequestParam("number") int number) {
//		System.out.println("수진왔다 " + number);
		return ResponseDto.success(mainService.getShematic(number));
	}
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/test/{number}")
	public String test(Model model, @PathVariable("number") long number) {
		model.addAttribute("schematic", mainService.getShematic(Integer.parseInt(""+number)));
		return "test";
	}

}
