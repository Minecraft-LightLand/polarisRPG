package org.xkmc.polaris_rpg.content.backpack;

import dev.lcy0x1.base.BaseTileEntity;
import dev.lcy0x1.base.block.NameSetable;
import dev.lcy0x1.core.util.SerialClass;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.xkmc.polaris_rpg.content.capability.worldstorage.StorageContainer;
import org.xkmc.polaris_rpg.content.capability.worldstorage.WorldStorage;
import org.xkmc.polaris_rpg.init.data.LangData;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

@SerialClass
public class WorldChestBlockEntity extends BaseTileEntity implements INamedContainerProvider, NameSetable {

	@SerialClass.SerialField
	public UUID owner_id;
	@SerialClass.SerialField(toClient = true)
	public String owner_name;
	@SerialClass.SerialField
	long password;
	@SerialClass.SerialField(toClient = true)
	private int color;

	private ITextComponent name;

	private LazyOptional<IItemHandler> handler;

	public WorldChestBlockEntity(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (level != null && !level.isClientSide() && !this.remove &&
				cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (handler == null) {
				Optional<StorageContainer> storage = WorldStorage.get((ServerWorld) level).getOrCreateStorage(owner_id, color, password);
				handler = !storage.isPresent() ? LazyOptional.empty() : LazyOptional.of(() -> new InvWrapper(storage.get().container));
			}
			return this.handler.cast();
		}
		return super.getCapability(cap, side);
	}

	public void setColor(int color) {
		if (this.color == color)
			return;
		handler = null;
		this.color = color;
		this.password = color;
		this.setChanged();
	}

	@Override
	public ITextComponent getName() {
		return name == null ? LangData.STORAGE_OWNER.get(owner_name) : name;
	}

	@Override
	public ITextComponent getDisplayName() {
		return getName();
	}

	@Override
	public WorldChestContainer createMenu(int wid, PlayerInventory inventory, PlayerEntity player) {
		if (level == null || owner_id == null) return null;
		Optional<StorageContainer> storage = WorldStorage.get((ServerWorld) level).getOrCreateStorage(owner_id, color, password);
		if (!storage.isPresent()) return null;
		return new WorldChestContainer(wid, inventory, storage.get().container, storage.get());
	}

	@Override
	public void setCustomName(ITextComponent component) {
		name = component;
	}

}
