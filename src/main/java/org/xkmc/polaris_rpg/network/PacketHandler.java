package org.xkmc.polaris_rpg.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.xkmc.polaris_rpg.init.PolarisRPG;
import org.xkmc.polaris_rpg.network.config.ConfigSyncManager;
import org.xkmc.polaris_rpg.network.packets.EffectToClient;
import org.xkmc.polaris_rpg.network.packets.EmptyRightClickToServer;
import org.xkmc.polaris_rpg.network.packets.SlotClickToServer;
import org.xkmc.polaris_rpg.network.packets.TargetSetPacket;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_CLIENT;
import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_SERVER;

public enum PacketHandler {
	TARGET_SET(TargetSetPacket.class, TargetSetPacket::new, PLAY_TO_SERVER),
	EFFECT_SYNC(EffectToClient.class, EffectToClient::new, PLAY_TO_CLIENT),
	EMPTY_RIGHT_CLICK(EmptyRightClickToServer.class, PLAY_TO_SERVER),
	CONFIG_SYNC(ConfigSyncManager.SyncPacket.class, PLAY_TO_CLIENT),
	SLOT_CLICK_TO_SERVER(SlotClickToServer.class, PLAY_TO_SERVER);

	public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(PolarisRPG.MODID, "main");
	public static final int NETWORK_VERSION = 1;
	public static final String NETWORK_VERSION_STR = String.valueOf(NETWORK_VERSION);
	public static SimpleChannel channel;

	private final LoadedPacket<?> packet;

	<T extends SimplePacketBase> PacketHandler(Class<T> type, Function<PacketBuffer, T> factory,
											   NetworkDirection direction) {
		packet = new LoadedPacket<>(type, factory, direction);
	}

	<T extends SerialPacketBase> PacketHandler(Class<T> type, NetworkDirection direction) {
		this(type, (buf) -> SerialPacketBase.serial(type, buf), direction);
	}

	public static void registerPackets() {
		channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
				.serverAcceptedVersions(NETWORK_VERSION_STR::equals)
				.clientAcceptedVersions(NETWORK_VERSION_STR::equals)
				.networkProtocolVersion(() -> NETWORK_VERSION_STR)
				.simpleChannel();
		for (PacketHandler packet : values())
			packet.packet.register();
	}

	public static void sendToNear(World world, BlockPos pos, int range, Object message) {
		channel.send(PacketDistributor.NEAR
				.with(PacketDistributor.TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), range, world.dimension())), message);
	}

	private static class LoadedPacket<T extends SimplePacketBase> {
		private static int index = 0;

		private final BiConsumer<T, PacketBuffer> encoder;
		private final Function<PacketBuffer, T> decoder;
		private final BiConsumer<T, Supplier<NetworkEvent.Context>> handler;
		private final Class<T> type;
		private final NetworkDirection direction;

		private LoadedPacket(Class<T> type, Function<PacketBuffer, T> factory, NetworkDirection direction) {
			encoder = T::write;
			decoder = factory;
			handler = T::handle;
			this.type = type;
			this.direction = direction;
		}

		private void register() {
			channel.messageBuilder(type, index++, direction)
					.encoder(encoder)
					.decoder(decoder)
					.consumer(handler)
					.add();
		}
	}

}
