package org.xkmc.polaris_rpg.content.capability.worldstorage;

import dev.lcy0x1.core.util.Automator;
import dev.lcy0x1.core.util.ExceptionHandler;
import dev.lcy0x1.core.util.SerialClass;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SerialClass
public class WorldStorageCapability implements ICapabilitySerializable<CompoundNBT> {

	public final ServerWorld w;
	public final WorldStorage handler;
	public final LazyOptional<WorldStorage> lo;

	public WorldStorageCapability(ServerWorld level) {
		this.w = level;
		if (level == null) LogManager.getLogger().error("world not present");
		handler = new WorldStorage();
		handler.level = level;
		lo = LazyOptional.of(() -> this.handler);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
		if (capability == WorldStorage.CAPABILITY)
			return lo.cast();
		return LazyOptional.empty();
	}

	@Override
	public CompoundNBT serializeNBT() {
		return Automator.toTag(new CompoundNBT(), lo.resolve().get());
	}

	@Override
	public void deserializeNBT(CompoundNBT tag) {
		ExceptionHandler.get(() -> Automator.fromTag(tag, WorldStorage.class, handler, f -> true));
		handler.init();
	}

}
