package org.xkmc.polaris_rpg.event;

import dev.lcy0x1.core.util.Automator;
import dev.lcy0x1.core.util.ExceptionHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.xkmc.polaris_rpg.content.capability.playerdata.LLPlayerCapability;
import org.xkmc.polaris_rpg.content.capability.playerdata.LLPlayerData;
import org.xkmc.polaris_rpg.content.capability.worldstorage.WorldStorageCapability;
import org.xkmc.polaris_rpg.init.PolarisRPG;
import org.xkmc.polaris_rpg.network.packets.CapToClient;

public class CapabilityEvents {

	@SubscribeEvent
	public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getObject();
			event.addCapability(new ResourceLocation(PolarisRPG.MODID, "player_data"),
					new LLPlayerCapability(player, player.level));
		}
	}

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

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.player.isAlive())
			LLPlayerData.get(event.player).tick();
	}

	@SubscribeEvent
	public static void onServerPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayerEntity e = (ServerPlayerEntity) event.getPlayer();
		if (e != null) {
			new CapToClient(CapToClient.Action.ALL, LLPlayerData.get(e)).toClientPlayer(e);
		}
	}

	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		CompoundNBT tag0 = Automator.toTag(new CompoundNBT(), LLPlayerData.get(event.getOriginal()));
		ExceptionHandler.run(() -> Automator.fromTag(tag0, LLPlayerData.class, LLPlayerData.get(event.getPlayer()), f -> true));
		LLPlayerData.get(event.getPlayer());
		ServerPlayerEntity e = (ServerPlayerEntity) event.getPlayer();
		new CapToClient(CapToClient.Action.CLONE, LLPlayerData.get(e)).toClientPlayer(e);
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onPlayerRespawn(ClientPlayerNetworkEvent.RespawnEvent event) {
		CompoundNBT tag0 = LLPlayerData.getCache(event.getOldPlayer());
		ExceptionHandler.run(() -> Automator.fromTag(tag0, LLPlayerData.class, LLPlayerData.get(event.getNewPlayer()), f -> true));
		LLPlayerData.get(event.getNewPlayer());
	}


}
