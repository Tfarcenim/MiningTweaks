package de.ellpeck.miningtweaks;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(MiningTweaks.MOD_ID)
public class MiningTweaks {

  public static final String MOD_ID = "miningtweaks";

  public MiningTweaks() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onConfigurationChangedEvent);
    EVENT_BUS.addListener(this::serverstart);
  }

  public static void handleConfig() {
    ConfigHandler.tweak_hardness.get().forEach(s -> {
      String[] split = s.split("@");
      Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(split[0]));
      block.blockHardness = Float.parseFloat(split[1]);
    });

    ConfigHandler.tweak_mining_level.get().forEach(s -> {
      String[] split = s.split("@");
      Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(split[0]));
      Util.setHarvestTool(block, ToolType.get(split[1]));
      Util.setHarvestLevel(block, Integer.parseInt(split[2]));
    });

  }

  public void serverstart(FMLServerStartedEvent e) {
    handleConfig();
  }

  public void onConfigurationChangedEvent(OnConfigChangedEvent event) {
    if (MOD_ID.equals(event.getModID())) {
      handleConfig();
    }
  }

  public static final ConfigHandler SERVER;
  public static final ForgeConfigSpec SERVER_SPEC;

  static {
    final Pair<ConfigHandler, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(ConfigHandler::new);
    SERVER_SPEC = specPair2.getRight();
    SERVER = specPair2.getLeft();
  }

  public static class ConfigHandler {

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> tweak_hardness;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> tweak_mining_level;

    public ConfigHandler(ForgeConfigSpec.Builder builder) {
      builder.push("general");
      tweak_hardness =
              builder
                      .comment("The blocks whose hardness should be modified. This needs to be the registry name of the block followed by an @ followed by the new hardness, so for example: 'minecraft:stone@5'")
                      .defineList("tweak_hardness", Lists.newArrayList(), String.class::isInstance);

      tweak_mining_level =
              builder
                      .comment("The blocks whose mining level should be modified. This needs to be the registry name of the block followed by an @ followed by the tool class ('pickaxe', 'axe' or 'shovel') followed by an @ followed by the harvest level (0 = wood, 1 = stone, 2 = iron, 3 = diamond, custom levels work), so for example: 'minecraft:stone@pickaxe@2'")
                      .defineList("tweak_mining_level", Lists.newArrayList(), String.class::isInstance);
      builder.pop();
    }
  }
}
