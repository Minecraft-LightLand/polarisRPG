package org.xkmc.polaris_rpg.content.backpack;

import dev.lcy0x1.base.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.xkmc.polaris_rpg.init.data.LangData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class BackpackItem extends Item {

	public static ListNBT getListTag(ItemStack stack) {
		if (stack.getOrCreateTag().contains("Items")) {
			return stack.getOrCreateTag().getList("Items", 10);
		} else {
			return new ListNBT();
		}
	}

	public static void setListTag(ItemStack stack, ListNBT list) {
		stack.getOrCreateTag().put("Items", list);
	}

	@OnlyIn(Dist.CLIENT)
	public static float isOpened(ItemStack stack, ClientWorld level, LivingEntity entity) {
		if (entity != Proxy.getClientPlayer()) return 0;
		Screen screen = Minecraft.getInstance().screen;
		if (screen instanceof BackpackScreen) {
			BackpackScreen gui = (BackpackScreen) screen;
			return gui.getMenu().getStack() == stack ? 1 : 0;
		}
		return 0;
	}

	public static final class MenuPvd implements INamedContainerProvider {

		private final ServerPlayerEntity player;
		private final int slot;
		private final ItemStack stack;

		public MenuPvd(ServerPlayerEntity player, Hand hand, ItemStack stack) {
			this.player = player;
			slot = hand == Hand.MAIN_HAND ? player.inventory.selected : 40;
			this.stack = stack;
		}

		public MenuPvd(ServerPlayerEntity player, int slot, ItemStack stack) {
			this.player = player;
			this.slot = slot;
			this.stack = stack;
		}

		@Override
		public ITextComponent getDisplayName() {
			return stack.getDisplayName();
		}

		@Override
		public BackpackContainer createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
			CompoundNBT tag = stack.getOrCreateTag();
			UUID uuid = tag.getUUID("container_id");
			return new BackpackContainer(id, inventory, slot, uuid, tag.getInt("rows"));
		}

		public void writeBuffer(PacketBuffer buf) {
			CompoundNBT tag = stack.getOrCreateTag();
			UUID id = tag.getUUID("container_id");
			buf.writeInt(slot);
			buf.writeUUID(id);
			buf.writeInt(tag.getInt("rows"));
		}

		public void open() {
			CompoundNBT tag = stack.getOrCreateTag();
			if (!tag.getBoolean("init")) {
				tag.putBoolean("init", true);
				tag.putUUID("container_id", UUID.randomUUID());
				tag.putInt("rows", 1);
			}
			NetworkHooks.openGui(player, this, this::writeBuffer);
		}

	}

	public final DyeColor color;

	public BackpackItem(DyeColor color, Properties props) {
		super(props);
		this.color = color;
	}

	@Override
	public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			new MenuPvd((ServerPlayerEntity) player, hand, stack).open();
		} else {
			player.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 1, 1);
		}
		return ActionResult.success(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> list, ITooltipFlag flag) {
		list.add(LangData.BACKPACK_SLOT.get(Math.max(1, stack.getOrCreateTag().getInt("rows")), 6));
	}

}
