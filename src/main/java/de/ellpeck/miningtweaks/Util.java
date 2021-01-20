package de.ellpeck.miningtweaks;

import net.minecraft.block.Block;
import net.minecraftforge.common.ToolType;

import java.lang.reflect.Field;

public class Util {

	public static Field harvestLevel;
	public static Field harvestTool;

	static {
		try {
			harvestLevel = Block.class.getDeclaredField("harvestLevel");
			harvestLevel.setAccessible(true);

			harvestTool = Block.class.getDeclaredField("harvestTool");
			harvestTool.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException("oh no", e);
		}
	}

	public static void setHarvestLevel(Block b, int level) {
		try {
			harvestLevel.setInt(b, level);
		} catch (Exception e) {
			throw new RuntimeException("oh no", e);
		}
	}

	public static void setHarvestTool(Block b, ToolType tool) {
		try {
			harvestTool.set(b, tool);
		} catch (Exception e) {
			throw new RuntimeException("oh no", e);
		}
	}
}
