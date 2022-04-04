package org.xkmc.polaris_rpg.network.packets;

import dev.lcy0x1.base.Proxy;
import dev.lcy0x1.core.util.Automator;
import dev.lcy0x1.core.util.ExceptionHandler;
import dev.lcy0x1.core.util.SerialClass;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.network.NetworkEvent;
import org.xkmc.polaris_rpg.content.capability.playerdata.CapProxy;
import org.xkmc.polaris_rpg.content.capability.playerdata.LLPlayerData;
import org.xkmc.polaris_rpg.network.SerialPacketBase;

import java.util.function.Consumer;
import java.util.function.Function;

@SerialClass
public class CapToClient extends SerialPacketBase {

	@SerialClass.SerialField
	public Action action;

	@SerialClass.SerialField
	public CompoundNBT tag;

	@Deprecated
	public CapToClient() {

	}

	public CapToClient(Action action, LLPlayerData handler) {
		this.action = action;
		this.tag = action.server.apply(handler);
	}

	public void handle(NetworkEvent.Context context) {
		if (action != Action.ALL && action != Action.CLONE && !Proxy.getClientPlayer().isAlive())
			return;
		action.client.accept(tag);
	}

	public static void reset(ServerPlayerEntity e, LLPlayerData.Reset reset) {
		CapToClient msg = new CapToClient(Action.RESET, null);
		msg.tag.putInt("ordinal", reset.ordinal());
		msg.toClientPlayer(e);
	}

	public enum Action {
		DEBUG((m) -> Automator.toTag(new CompoundNBT(), m), (tag) -> {
			LLPlayerData m = CapProxy.getHandler();
			CompoundNBT comp = ExceptionHandler.get(() -> Automator.toTag(new CompoundNBT(), LLPlayerData.class, m, f -> true));
			CapToServer.sendDebugInfo(tag, comp);
		}),
		ALL((m) -> {
			return Automator.toTag(new CompoundNBT(), m);
		}, tag -> LLPlayerData.cacheSet(tag, false)),
		CLONE((m) -> {
			return Automator.toTag(new CompoundNBT(), m);
		}, tag -> LLPlayerData.cacheSet(tag, true)),
		RESET(m -> new CompoundNBT(), tag -> {
			LLPlayerData h = CapProxy.getHandler();
			h.reset(LLPlayerData.Reset.values()[tag.getInt("ordinal")]);
		});

		public final Function<LLPlayerData, CompoundNBT> server;
		public final Consumer<CompoundNBT> client;


		Action(Function<LLPlayerData, CompoundNBT> server, Consumer<CompoundNBT> client) {
			this.server = server;
			this.client = client;
		}
	}

}
