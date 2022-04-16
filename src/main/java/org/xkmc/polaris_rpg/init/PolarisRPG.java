package org.xkmc.polaris_rpg.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xkmc.polaris_rpg.content.capability.worldstorage.WorldStorage;
import org.xkmc.polaris_rpg.event.*;
import org.xkmc.polaris_rpg.init.data.*;
import org.xkmc.polaris_rpg.init.data.conditions.BaseCondition;
import org.xkmc.polaris_rpg.init.registry.*;
import org.xkmc.polaris_rpg.network.PacketHandler;

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
		PolarisTags.register();
		PolarisLootModifier.register();

		REGISTRATE.addDataGenerator(ProviderType.LANG, LangData::genLang);
		REGISTRATE.addDataGenerator(ProviderType.RECIPE, RecipeGen::genRecipe);
		REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, AdvancementGen::genAdvancement);
		REGISTRATE.addDataGenerator(ProviderType.LOOT, LootGen::genLoot);
		REGISTRATE.<GlobalLootModifierSerializer<?>>addRegisterCallback(GlobalLootModifierSerializer.class, BaseCondition::register);

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::gatherData);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(CapabilityEvents.class);
		MinecraftForge.EVENT_BUS.register(GenericEventHandler.class);
		MinecraftForge.EVENT_BUS.register(PolarisGeneralEventHandler.class);
		MinecraftForge.EVENT_BUS.register(ItemUseEventHandler.class);
		MinecraftForge.EVENT_BUS.register(EffectSyncEvents.class);
		MinecraftForge.EVENT_BUS.register(ClientEntityEffectRenderEvents.class);

	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			PacketHandler.registerPackets();
			EffectSyncEvents.init();
			WorldStorage.register();
		});
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			//
			PolarisClient.registerItemProperties();
		});
	}

	public void gatherData(GatherDataEvent event) {
		LootGen.genModifier(event);
	}

	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event) {
	}

}
