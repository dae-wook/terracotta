package com.daesoo.terracotta.schematic;

import org.springframework.stereotype.Service;

import com.daesoo.terracotta.schematic.util.Schematic;
import com.daesoo.terracotta.schematic.util.SchemeParser;

@Service
public class SchematicService {
	
	public Schematic getShematic(int number) {
		
		Schematic schematic = SchemeParser.getSchematic(number);
		
		return schematic;
	}
	
}
