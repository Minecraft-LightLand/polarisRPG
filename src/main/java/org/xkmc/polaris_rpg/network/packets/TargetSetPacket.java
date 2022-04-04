package org.xkmc.polaris_rpg.network.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.xkmc.polaris_rpg.network.SimplePacketBase;
import org.xkmc.polaris_rpg.util.RayTraceUtil;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Supplier;

public class TargetSetPacket extends SimplePacketBase {

	public UUID player, target;

	public TargetSetPacket(UUID player, @Nullable UUID target) {
		this.player = player;
		this.target = target;
	}

	public TargetSetPacket(PacketBuffer buf) {
		player = buf.readUUID();
		boolean exist = buf.readBoolean();
		if (exist) {
			target = buf.readUUID();
		}
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeUUID(player);
		buffer.writeBoolean(target != null);
		if (target != null) {
			buffer.writeUUID(target);
		}
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> RayTraceUtil.sync(this));
		context.get().setPacketHandled(true);
	}
}
