package org.xkmc.polaris_rpg.network.packets;

import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.xkmc.polaris_rpg.event.EffectSyncEvents;
import org.xkmc.polaris_rpg.network.SimplePacketBase;

import java.util.UUID;
import java.util.function.Supplier;

public class EffectToClient extends SimplePacketBase {

	public UUID entity;
	public Effect effect;
	public boolean exist;
	public int level;

	public EffectToClient(UUID entity, Effect effect, boolean exist, int level) {
		this.entity = entity;
		this.effect = effect;
		this.exist = exist;
		this.level = level;
	}

	public EffectToClient(PacketBuffer buf) {
		entity = buf.readUUID();
		effect = ForgeRegistries.POTIONS.getValue(buf.readResourceLocation());
		exist = buf.readBoolean();
		if (exist) {
			level = buf.readInt();
		}
	}

	@Override
	public void write(PacketBuffer buffer) {
		buffer.writeUUID(entity);
		buffer.writeResourceLocation(effect.getRegistryName());
		buffer.writeBoolean(exist);
		if (exist) {
			buffer.writeInt(level);
		}
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> EffectSyncEvents.sync(this));
		context.get().setPacketHandled(true);
	}
}
