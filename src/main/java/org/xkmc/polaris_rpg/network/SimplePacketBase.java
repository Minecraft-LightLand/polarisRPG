package org.xkmc.polaris_rpg.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public abstract class SimplePacketBase {

	public abstract void write(PacketBuffer buffer);

	public abstract void handle(Supplier<NetworkEvent.Context> context);

	public void toServer() {
		PacketHandler.channel.sendToServer(this);
	}

	public void toTrackingPlayers(Entity e) {
		PacketHandler.channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> e), this);
	}

	public void toClientPlayer(ServerPlayerEntity e) {
		PacketHandler.channel.sendTo(this, e.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
	}

	public void toAllClient() {
		PacketHandler.channel.send(PacketDistributor.ALL.noArg(), this);
	}

}
