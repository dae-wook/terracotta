package com.daesoo.terracotta.schematic.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jnbt.Tag;
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

	private static Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) {
		return items.get(key);
	}

//	public SchematicDto2 getSchematic(String fileName) {
//		SchematicDto2 schematicDto = null;
//		InputStream fis = fileUtil.downloadSchematicFileToInputStream(fileName);
//		Schematic schematic;
//		try {
//			schematic = SchematicLoader.load(fis);
//			List<CustomPair<SchematicBlockPos, SchematicBlock>> blockList = schematic.blocks()
//			        .map(pair -> new CustomPair<>(pair.left(), pair.right()))
//			        .collect(Collectors.toList());
//			
////			Map<String, List<int[]>> blockMap = new HashMap<>();
////			HashSet<String> nameSet = new HashSet<>();
////			for(int i = 0; i < blockList.size(); i++) {
////				nameSet.add(blockList.get(i).blockData.name);
////			}
////			for(String name : nameSet) {
////				ArrayList<int[]> posList = new ArrayList<>();
////				for(int i = 0; i < blockList.size(); i++) {
////					CustomPair<SchematicBlockPos, SchematicBlock> pair = blockList.get(i);
////					SchematicBlock block = pair.blockData;
////					SchematicBlockPos pos = pair.position;
////					int[] blockPos = new int[]{pos.x, pos.y, pos.z};
////					if(block.name.equals(name)) {
////						posList.add(blockPos);
////					}
////					blockMap.put(block.name.split(":")[1], posList);
////				}
////			}
//			
//			Map<String, List<int[]>> blockMap = new HashMap<>();
//			Set<String> nameSet = new HashSet<>();
//
//			// 1단계: 중복 반복 제거하여 nameSet 구축
//			for (CustomPair<SchematicBlockPos, SchematicBlock> pair : blockList) {
//			    nameSet.add(pair.blockData.name);
//			}
//
//			// 2단계: 각 이름에 해당하는 위치 목록을 구축하여 blockMap에 추가
//			for (String name : nameSet) {
//			    List<int[]> posList = new ArrayList<>();
//			    for (CustomPair<SchematicBlockPos, SchematicBlock> pair : blockList) {
//			        if (pair.blockData.name.equals(name)) {
//			            SchematicBlockPos pos = pair.position;
//			            int[] blockPos = new int[]{pos.x, pos.y, pos.z};
//			            posList.add(blockPos);
//			        }
//			    }
//			    blockMap.put(name.split(":")[1], posList); // 중복 추가 제거
//			}
//			
//			
//			
////			blockList.get(0).blockData.
//			ObjectMapper mapper = new ObjectMapper();
//	        String json = mapper.writeValueAsString(blockMap);
//			
////	        System.out.println(GzipWraper.decompress(GzipWraper.compress(json)));
//			schematicDto = SchematicDto2.builder()
//			.width(schematic.width())
//			.height(schematic.height())
//			.length(schematic.length())
//			.blockData(GzipWraper.compress(json))
////			.blockList(GzipWraper.compress(json))
////			.blockList(blockList)
//			.build();
//			
//		} catch (ParsingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		
//			
//			
//			
//		
//		return schematicDto;
//	}

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
	
}
