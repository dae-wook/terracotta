package com.daesoo.terracotta.schematic.util;

import java.io.File;
import java.io.FileInputStream;
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


public class SchemeParser {

	private static Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) {
		return items.get(key);
	}

	public static Schematic getSchematic(int number) {
		Schematic schematic = null;
		String fileName = null;
		if(number == 1) {
			fileName = "test.schem";
		}else if(number == 2) {
			fileName = "tower.schem";
		}else if(number == 3) {
			fileName = "taj.schem";
		}else if(number == 4) {
			fileName = "nicecastle.schem";
		}else if(number == 5) {
			fileName = "bastion.schem";
		}else if(number == 6) {
			fileName = "cathedral.schem";
		}else if(number == 7) {
			fileName = "test3.schem";
		}else if(number == 8) {
			fileName = "test2.schem";
		}else if(number == 9) {
			fileName = "test4.schem";
		}else if(number == 10) {
			fileName = "test5.schem";
		}else if(number == 11) {
			fileName = "test6.schem";
		}else if(number == 12) {
			fileName = "half.schem";
		}else if(number == 13) {
			fileName = "sp.schem";
		}else if(number == 14) {
			fileName = "test7.schem";
		}else if(number == 15) {
			fileName = "test8.schem";
		}else if(number == 17) {
			fileName = "smallcastle.schem";
		}else if(number == 18) {
			fileName = "castle182.schem";
		}else if(number == 19) {
			fileName = "house.schem";
		}else if(number == 20) {
			fileName = "testhouse.schem";
		}else if(number == 21) {
			fileName = "smallcastle2.schem";
		}else if(number == 22) {
			fileName = "smallcastletest1.schem";
		}else if(number == 23) {
			fileName = "smallcastletest2.schem";
		}else if(number == 24) {
			fileName = "test9.schem";
		}else if(number == 25) {
			fileName = "test10.schem";
		}else if(number == 26) {
			fileName = "test11.schem";
		}else if(number == 27) {
			fileName = "halfsmallcastletest2-1.schem";
		}else if(number == 28) {
			fileName = "halfsmallcastletest2-2.schem";
		}else if(number == 29) {
			fileName = "halfsmallcastletest2-3.schem";
		}else if(number == 30) {
			fileName = "halfsmallcastletest2-4.schem";
		}else if(number == 31) {
			fileName = "test12.schem";
		}else if(number == 32) {
			fileName = "stairs-test.schem";
		}else if(number == 60) {
			fileName = "pumpfarm.litematic";
		}
		ClassPathResource resource = new ClassPathResource("static/schematics/" + fileName);
		try(InputStream fis = resource.getInputStream()) {
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
