package com.daesoo.terracotta.schematic.util;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schematic {
	
	private byte[] blockData;
	private short width;
	private short length;
	private short height;
	private Map<String, Integer> palette;
	
}
