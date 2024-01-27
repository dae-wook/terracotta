package com.daesoo.terracotta.schematic;

import org.springframework.stereotype.Service;

import com.daesoo.terracotta.schematic.util.Schematic;
import com.daesoo.terracotta.schematic.util.SchemeParser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchematicService {
	
	private final SchemeParser schemeParser;
	
	public Schematic getShematic(String fileName) {
		
		Schematic schematic = schemeParser.getSchematic(fileName);
		
		return schematic;
	}
	
}
