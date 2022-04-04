package org.xkmc.polaris_rpg.network;

import dev.lcy0x1.core.util.Automator;
import dev.lcy0x1.core.util.SerialClass;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

@SerialClass
public abstract class SerialPacketBase extends SimplePacketBase {

	public static <T extends SerialPacketBase> T serial(Class<T> cls, PacketBuffer buf) {
		CompoundNBT tag = buf.readAnySizeNbt();
		return Automator.fromTag(tag, cls);
	}

	@Override
	public final void write(PacketBuffer buffer) {
		CompoundNBT tag = Automator.toTag(new CompoundNBT(), this);
		buffer.writeNbt(tag);
	}

	@Override
	public final void handle(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> handle(context.get()));
		context.get().setPacketHandled(true);
	}

	public abstract void handle(NetworkEvent.Context ctx);

}
