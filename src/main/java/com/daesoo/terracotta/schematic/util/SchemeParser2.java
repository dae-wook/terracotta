package com.daesoo.terracotta.schematic.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.daesoo.terracotta.common.util.FileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.ByteArrayTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchemeParser2 {
	
	private final FileUtil fileUtil;

	private static Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) {
		return items.get(key);
	}

	public Schematic getSchematic(String fileName) {
		Schematic schematic = null;
		try {
			NamedTag namedTag = new NBTDeserializer().fromStream(fileUtil.downloadSchematicFileToInputStream(fileName));
			CompoundTag rootTag = (CompoundTag) namedTag.getTag();
			CompoundTag palletTag = (CompoundTag) rootTag.get("Palette");
			Map<String, Integer> paletteResponseMap = new HashMap<>();
			for(String key : palletTag.keySet()) {
				
				paletteResponseMap.put(key, Integer.parseInt(palletTag.get(key).valueToString()));
			}
			
			short length = Short.parseShort(rootTag.get("Length").valueToString());
			short width = Short.parseShort(rootTag.get("Width").valueToString());
			short height = Short.parseShort(rootTag.get("Height").valueToString());
			log.info("l {} w {} h {}", length, width, height);
			ByteArrayTag blockDataTag = (ByteArrayTag) rootTag.get("BlockData");
			byte[] blockData = blockDataTag.getValue();

//			System.out.println(blockData[14359]);
			System.out.println(rootTag.get("Palette").getClass());
			
			for(String key : rootTag.keySet()) {
				if(!(key.equals("BlockData") || key.equals("Palette"))) {
					
					System.out.println(key + ":" + rootTag.get(key));
				}
			}
			
			log.info("blockCount : {}", blockData.length);
			schematic = Schematic.builder()
					.blockData(blockData)
					.width(width)
					.height(height)
					.length(length)
					.palette(paletteResponseMap)
					.build();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return schematic;
	}

}
