package org.xkmc.polaris_rpg.event;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.xkmc.polaris_rpg.content.capability.worldstorage.WorldStorageCapability;
import org.xkmc.polaris_rpg.init.PolarisRPG;

public class CapabilityEvents {

	@SubscribeEvent
	public static void onAttachLevelCapabilities(AttachCapabilitiesEvent<World> event) {
		if (event.getObject() instanceof ServerWorld) {
			ServerWorld level = (ServerWorld) event.getObject();
			if (level.dimension() == World.OVERWORLD) {
				event.addCapability(new ResourceLocation(PolarisRPG.MODID, "world_storage"),
						new WorldStorageCapability(level));
			}
		}
	}


}
