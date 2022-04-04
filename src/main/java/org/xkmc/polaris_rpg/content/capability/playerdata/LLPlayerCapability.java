package org.xkmc.polaris_rpg.content.capability.playerdata;

import dev.lcy0x1.core.util.SerialClass;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SerialClass
public class LLPlayerCapability implements ICapabilitySerializable<CompoundNBT> {

	public final PlayerEntity player;
	public final World w;
	public LLPlayerData handler = new LLPlayerData();
	public LazyOptional<LLPlayerData> lo = LazyOptional.of(() -> this.handler);

	public LLPlayerCapability(PlayerEntity player, World w) {
		this.player = player;
		this.w = w;
		if (w == null)
			LogManager.getLogger().error("world not present in entity");
		handler.world = w;
		handler.player = player;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
		if (capability == LLPlayerData.CAPABILITY)
			return lo.cast();
		return LazyOptional.empty();
	}

	@Override
	public CompoundNBT serializeNBT() {
		return (CompoundNBT) LLPlayerData.STORAGE.writeNBT(LLPlayerData.CAPABILITY, lo.resolve().get(), null);
	}

	@Override
	public void deserializeNBT(CompoundNBT compoundNBT) {
		LLPlayerData.STORAGE.readNBT(LLPlayerData.CAPABILITY, handler, null, compoundNBT);
		handler.init();
	}

}
