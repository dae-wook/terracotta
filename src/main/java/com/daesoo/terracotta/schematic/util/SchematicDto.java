package com.daesoo.terracotta.schematic.util;

import java.util.List;
import java.util.Map;

import org.jnbt.Tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.sandrohc.schematic4j.schematic.types.Pair;
import net.sandrohc.schematic4j.schematic.types.SchematicBlock;
import net.sandrohc.schematic4j.schematic.types.SchematicBlockPos;

@Getter

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchematicDto {
	
	private int width;
	private int height;
	private int length;
	byte[] blockData;
//	String blockData;
//	List<CustomPair<SchematicBlockPos, SchematicBlock>> blockList;
}
