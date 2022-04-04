package org.xkmc.polaris_rpg.content.capability.playerdata;

import dev.lcy0x1.base.Proxy;
import dev.lcy0x1.core.util.Automator;
import dev.lcy0x1.core.util.ExceptionHandler;
import dev.lcy0x1.core.util.SerialClass;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@SerialClass
public class LLPlayerData {

	public static final Storage STORAGE = new Storage();

	@CapabilityInject(LLPlayerData.class)
	public static Capability<LLPlayerData> CAPABILITY = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(LLPlayerData.class, STORAGE, LLPlayerData::new);
	}

	public static LLPlayerData get(PlayerEntity e) {
		return e.getCapability(CAPABILITY).resolve().get().check();
	}

	public static boolean isProper(PlayerEntity player) {
		return player.getCapability(CAPABILITY).isPresent();
	}

	private static CompoundNBT revive_cache;

	@OnlyIn(Dist.CLIENT)
	public static void cacheSet(CompoundNBT tag, boolean force) {
		ClientPlayerEntity pl = Proxy.getClientPlayer();
		if (!force && pl != null && pl.getCapability(CAPABILITY).cast().resolve().isPresent()) {
			LLPlayerData m = LLPlayerData.get(pl);
			m.reset(Reset.FOR_INJECT);
			ExceptionHandler.run(() -> Automator.fromTag(tag, LLPlayerData.class, m, f -> true));
			m.init();
		} else revive_cache = tag;
	}

	@OnlyIn(Dist.CLIENT)
	public static CompoundNBT getCache(PlayerEntity pl) {
		CompoundNBT tag = revive_cache;
		revive_cache = null;
		if (tag == null)
			tag = Automator.toTag(new CompoundNBT(), get(pl));
		return tag;
	}

	@SerialClass.SerialField
	public State state = State.PREINJECT;
	public PlayerEntity player;
	public World world;

	public void tick() {
	}

	public void reset(Reset reset) {
		reset.cons.accept(this);
	}

	protected void init() {
		if (state == null) {
			reset(Reset.FOR_INJECT);
		}
		if (state != State.ACTIVE) {
			state = State.ACTIVE;
		}
	}

	public void reInit() {
		state = State.PREINIT;
		check();
	}

	private LLPlayerData check() {
		if (state != State.ACTIVE)
			init();
		return this;
	}

	@SerialClass.OnInject
	public void onInject() {
		if (state == State.PREINJECT || state == State.ACTIVE)
			state = State.PREINIT;
	}

	public enum State {
		PREINJECT, PREINIT, ACTIVE
	}

	public enum Reset {
		ALL((h) -> {
		}), FOR_INJECT((h) -> {
			h.state = State.PREINJECT;
		});

		final Consumer<LLPlayerData> cons;

		Reset(Consumer<LLPlayerData> cons) {
			this.cons = cons;
		}
	}

	public static class Storage implements Capability.IStorage<LLPlayerData> {

		@Nullable
		@Override
		public INBT writeNBT(Capability<LLPlayerData> capability, LLPlayerData obj, Direction direction) {
			return Automator.toTag(new CompoundNBT(), obj);
		}

		@Override
		public void readNBT(Capability<LLPlayerData> capability, LLPlayerData obj, Direction direction, INBT inbt) {
			ExceptionHandler.get(() -> Automator.fromTag((CompoundNBT) inbt, LLPlayerData.class, obj, f -> true));
		}

	}

}
