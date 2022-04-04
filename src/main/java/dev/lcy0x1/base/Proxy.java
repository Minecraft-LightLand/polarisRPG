package dev.lcy0x1.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Optional;

public class Proxy {

	@OnlyIn(Dist.CLIENT)
	public static ClientPlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	public static PlayerEntity getPlayer() {
		return DistExecutor.unsafeRunForDist(() -> Proxy::getClientPlayer, () -> () -> null);
	}

	public static World getWorld() {
		return DistExecutor.unsafeRunForDist(() -> Proxy::getClientWorld, () -> () -> Proxy.getServer().map(MinecraftServer::overworld).orElse(null));
	}

	@OnlyIn(Dist.CLIENT)
	public static ClientWorld getClientWorld() {
		return Minecraft.getInstance().level;
	}

	public static Optional<MinecraftServer> getServer() {
		return Optional.ofNullable(ServerLifecycleHooks.getCurrentServer());
	}

}
