package org.xkmc.polaris_rpg.content.backpack;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import org.xkmc.polaris_rpg.content.capability.worldstorage.StorageContainer;
import org.xkmc.polaris_rpg.content.capability.worldstorage.WorldStorage;
import org.xkmc.polaris_rpg.init.data.LangData;
import org.xkmc.polaris_rpg.init.registry.PolarisBlocks;
import org.xkmc.polaris_rpg.util.ServerOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WorldChestItem extends BlockItem {

	public static class MenuPvd implements INamedContainerProvider {

		public final ServerPlayerEntity player;
		public final ItemStack stack;
		public final WorldChestItem item;

		public MenuPvd(ServerPlayerEntity player, ItemStack stack, WorldChestItem item) {
			this.player = player;
			this.stack = stack;
			this.item = item;
		}

		@Override
		public ITextComponent getDisplayName() {
			return stack.getDisplayName();
		}

		@ServerOnly
		@Override
		public WorldChestContainer createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
			StorageContainer container = getContainer((ServerWorld) player.level).get();
			return new WorldChestContainer(id, inventory, container.container, container);
		}

		@ServerOnly
		private Optional<StorageContainer> getContainer(ServerWorld level) {
			CompoundNBT tag = stack.getOrCreateTag();
			UUID id = tag.getUUID("owner_id");
			long pwd = tag.getLong("password");
			return WorldStorage.get(level).getOrCreateStorage(id, item.color.getId(), pwd);
		}

		@ServerOnly
		public void open() {
			item.refresh(stack, player);
			if (player.level.isClientSide() || !getContainer((ServerWorld) player.level).isPresent())
				return;
			NetworkHooks.openGui(player, this);
		}

	}

	public final DyeColor color;

	public WorldChestItem(DyeColor color, Properties props) {
		super(PolarisBlocks.WORLD_CHEST.get(), props);
		this.color = color;
	}

	void refresh(ItemStack stack, PlayerEntity player) {
		if (!stack.getOrCreateTag().contains("owner_id")) {
			stack.getOrCreateTag().putUUID("owner_id", player.getUUID());
			stack.getOrCreateTag().putString("owner_name", player.getName().getString());
			stack.getOrCreateTag().putLong("password", color.getId());
		}
	}

	public void fillItemCategory(ItemGroup tab, NonNullList<ItemStack> list) {
		if (this.allowdedIn(tab)) {
			list.add(new ItemStack(this));
		}
	}

	@Override
	public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			new MenuPvd((ServerPlayerEntity) player, stack, this).open();
		} else {
			player.playSound(SoundEvents.ENDER_CHEST_OPEN, 1, 1);
		}
		return ActionResult.consume(stack);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		if (!context.getLevel().isClientSide() && context.getPlayer() != null)
			refresh(context.getItemInHand(), context.getPlayer());
		if (context.getPlayer() != null && !context.getPlayer().isCrouching()) {
			ItemStack stack = context.getItemInHand();
			if (!context.getLevel().isClientSide()) {
				new MenuPvd((ServerPlayerEntity) context.getPlayer(), stack, this).open();
			} else {
				context.getPlayer().playSound(SoundEvents.ENDER_CHEST_OPEN, 1, 1);
			}
			return ActionResultType.SUCCESS;
		}
		if (!context.getItemInHand().getOrCreateTag().contains("owner_id"))
			return ActionResultType.FAIL;
		return super.useOn(context);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> list, ITooltipFlag flag) {
		CompoundNBT tag = stack.getTag();
		if (tag == null) return;
		if (tag.contains("owner_name")) {
			String name = tag.getString("owner_name");
			list.add(LangData.STORAGE_OWNER.get(name));
		}
	}

	public String getDescriptionId() {
		return this.getOrCreateDescriptionId();
	}

}
