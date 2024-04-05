package com.daesoo.terracotta.schematic.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.daesoo.terracotta.common.util.FileUtil;
import com.daesoo.terracotta.common.util.GzipWraper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import net.sandrohc.schematic4j.SchematicLoader;
import net.sandrohc.schematic4j.exception.ParsingException;
import net.sandrohc.schematic4j.schematic.Schematic;
import net.sandrohc.schematic4j.schematic.types.Pair;
import net.sandrohc.schematic4j.schematic.types.SchematicBlock;
import net.sandrohc.schematic4j.schematic.types.SchematicBlockPos;

@Component
@RequiredArgsConstructor
public class SchemeParser {
	
	private final FileUtil fileUtil;

	public SchematicDto getSchematic(String fileName) {
	    SchematicDto schematicDto = null;
	    try (InputStream fis = fileUtil.downloadSchematicFileToInputStream(fileName)) {
	        Schematic schematic = SchematicLoader.load(fis);

	        Map<String, List<int[]>> blockMap = new HashMap<>();
	        
	        // 블록맵 구축
	        for (SchematicBlockPos pos : schematic.blocks().map(Pair::left).collect(Collectors.toList())) {
	            SchematicBlock block = schematic.block(pos);
	            if(block.name.equals("minecraft:air")) continue;
	            String name = block.name.split(":")[1];
	            blockMap.computeIfAbsent(name, k -> new ArrayList<>())
	                    .add(new int[]{pos.x, pos.y, pos.z});
	        }

	        ObjectMapper mapper = new ObjectMapper();
	        String json = mapper.writeValueAsString(blockMap);

	        schematicDto = SchematicDto.builder()
	                .width(schematic.width())
	                .height(schematic.height())
	                .length(schematic.length())
	                .blockData(GzipWraper.compress(json))
//	                .blockData(json)
	                .build();

	    } catch (ParsingException | IOException e) {
	        e.printStackTrace();
	    }

	    return schematicDto;
	}

	public SchematicDto convertFileToSchematicJson(byte[] bytes) {
		SchematicDto schematicDto = new SchematicDto();
	    try (InputStream fis = new ByteArrayInputStream(bytes)) {
	        Schematic schematic = SchematicLoader.load(fis);

	        Map<String, List<int[]>> blockMap = new HashMap<>();
	        
	        int blockCount = 0;
	        
	        // 블록맵 구축
	        for (Pair<SchematicBlockPos, SchematicBlock> pair : schematic.blocks().toList()) {
	        	SchematicBlockPos pos = pair.left;
	            SchematicBlock block = pair.right;
	            if(block.name.equals("minecraft:air")) {
	            	continue;
	            }
	            String name = block.name.split(":")[1];
	            blockCount++;
	            blockMap.computeIfAbsent(name, k -> new ArrayList<>())
	                    .add(new int[]{convertPositive(pos.x), convertPositive(pos.y), convertPositive(pos.z)});
	        }
	        if (blockMap.size() < 1) {
	        	throw new IllegalArgumentException("dd");
	        }

	        ObjectMapper mapper = new ObjectMapper();
	        String json = mapper.writeValueAsString(blockMap);
	        
	        schematicDto = SchematicDto.builder()
	                .width(schematic.width())
	                .height(schematic.height())
	                .length(schematic.length())
	                .blockData(GzipWraper.compress(json))
	                .size(blockCount)
	                .build();
	        

//	        schematicDto = mapper.writeValueAsString(schematicDto);

	    } catch (ParsingException | IOException e) {
	        e.printStackTrace();
	    }

	    return schematicDto;
	}
	
	private int convertPositive(int position) {
		if (position < 0) {
			return position * -1;
		}
		return position;
	}
	
}
