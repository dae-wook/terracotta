package com.daesoo.terracotta.schematic.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.jnbt.ByteArrayTag;
import org.jnbt.CompoundTag;
import org.jnbt.LongArrayTag;
import org.jnbt.NBTInputStream;
import org.jnbt.ShortTag;
import org.jnbt.Tag;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.daesoo.terracotta.common.util.FileUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SchemeParser {
	
	private final FileUtil fileUtil;

	private static Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) {
		return items.get(key);
	}

	public Schematic getSchematic(String fileName) {
		Schematic schematic = null;
		ClassPathResource resource = new ClassPathResource("static/schematics/" + fileName);
		try {
			InputStream fis = fileUtil.downloadObjectToInputStream(fileName);
			NBTInputStream nbt = new NBTInputStream(fis);

			CompoundTag backuptag = (CompoundTag) nbt.readTag();
			Map<String, Tag> tagCollection = backuptag.getValue();
			String extension = fileName.substring(fileName.lastIndexOf('.'));
			nbt.close();
			fis.close();
			if (extension.equals(".schem")) {

				short width = (Short) getChildTag(tagCollection, "Width", ShortTag.class).getValue();
				short height = (Short) getChildTag(tagCollection, "Height", ShortTag.class).getValue();
				short length = (Short) getChildTag(tagCollection, "Length", ShortTag.class).getValue();
				byte[] blockData = (byte[])getChildTag(tagCollection, "BlockData", ByteArrayTag.class).getValue();
				
				CompoundTag paletteCompound = (CompoundTag)tagCollection.get("Palette");
				Map<String, Tag> paletteMap = paletteCompound.getValue();
				Map<String, Integer> paletteResponseMap = new HashMap<>();

				//팔레트
				for(String key : paletteMap.keySet()) {
					paletteResponseMap.put(key, (int)paletteMap.get(key).getValue());
				}

				schematic = Schematic.builder()
						.blockData(blockData)
						.width(width)
						.height(height)
						.length(length)
						.palette(paletteResponseMap)
						.build();
			} else if(extension.equals(".litematic")) {


				for(String key : tagCollection.keySet()) {
					System.out.println(key + ": " + tagCollection.get(key).getValue());
				}
				CompoundTag regionTag = (CompoundTag)getChildTag(tagCollection, "Regions", CompoundTag.class);
				Map<String, Tag> regionCollection = regionTag.getValue();
				CompoundTag mainTag = (CompoundTag)getChildTag(regionCollection, "pumpfarm", CompoundTag.class);
				Map<String, Tag> mainCollection = mainTag.getValue();
				
				
				
				long[] blockData = (long[]) getChildTag(mainCollection, "BlockStates", LongArrayTag.class).getValue();
				for(long block : blockData) {
					System.out.println(block);
				}
				

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return schematic;
	}

}
