package org.xkmc.polaris_rpg.network.packets;

import dev.lcy0x1.core.util.SerialClass;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.xkmc.polaris_rpg.content.capability.playerdata.LLPlayerData;
import org.xkmc.polaris_rpg.network.SerialPacketBase;

import java.util.function.BiConsumer;

@SerialClass
public class CapToServer extends SerialPacketBase {

	public enum Action {
		DEBUG((handler, tag) -> {
			LogManager.getLogger().info("server: " + tag.getCompound("server"));
			LogManager.getLogger().info("client: " + tag.getCompound("client"));
		});

		private final BiConsumer<LLPlayerData, CompoundNBT> cons;

		Action(BiConsumer<LLPlayerData, CompoundNBT> cons) {
			this.cons = cons;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void sendDebugInfo(CompoundNBT s, CompoundNBT c) {
		CompoundNBT tag = new CompoundNBT();
		tag.put("server", s);
		tag.put("client", c);
		new CapToServer(Action.DEBUG, tag).toServer();
	}

	@SerialClass.SerialField
	public Action action;
	@SerialClass.SerialField
	public CompoundNBT tag;

	@Deprecated
	public CapToServer() {

	}

	private CapToServer(Action act, CompoundNBT tag) {
		this.action = act;
		this.tag = tag;
	}

	public void handle(NetworkEvent.Context ctx) {
		ServerPlayerEntity player = ctx.getSender();
		if (player == null || !player.isAlive())
			return;
		LLPlayerData handler = LLPlayerData.get(player);
		action.cons.accept(handler, tag);
	}

}
