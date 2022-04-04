package org.xkmc.polaris_rpg.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xkmc.polaris_rpg.event.PolarisGeneralEventHandler;
import org.xkmc.polaris_rpg.init.data.AdvancementGen;
import org.xkmc.polaris_rpg.init.data.LangData;
import org.xkmc.polaris_rpg.init.data.RecipeGen;
import org.xkmc.polaris_rpg.init.registry.*;

@Mod(PolarisRPG.MODID)
public class PolarisRPG {

	public static final String MODID = "polaris_rpg";
	public static final Logger LOGGER = LogManager.getLogger();
	public static Registrate REGISTRATE;

	public PolarisRPG() {
		REGISTRATE = Registrate.create(MODID);

		PolarisItems.register();
		PolarisBlocks.register();
		PolarisEntities.register();
		PolarisContainers.register();
		PolarisRecipeTypes.register();
		PolarisMagic.register();

		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, AdvancementGen::genAdvancement);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::gatherData);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(PolarisGeneralEventHandler.class);

	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
		});
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			//
			PolarisClient.registerItemProperties();
		});
	}

	public void gatherData(GatherDataEvent event) {
	}

	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
	}

}
