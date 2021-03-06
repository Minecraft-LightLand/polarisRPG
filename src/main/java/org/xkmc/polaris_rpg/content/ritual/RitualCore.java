package org.xkmc.polaris_rpg.content.ritual;

import dev.lcy0x1.base.block.impl.TileEntityBlockMethodImpl;
import dev.lcy0x1.base.block.mult.AnimateTickBlockMethod;
import dev.lcy0x1.base.block.mult.OnClickBlockMethod;
import dev.lcy0x1.base.block.mult.ScheduleTickBlockMethod;
import dev.lcy0x1.base.block.type.BlockMethod;
import dev.lcy0x1.base.recipe.BaseRecipe;
import dev.lcy0x1.core.util.SerialClass;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.xkmc.polaris_rpg.init.registry.PolarisBlocks;
import org.xkmc.polaris_rpg.init.registry.PolarisRecipeTypes;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RitualCore {
	public static final TileEntityBlockMethodImpl TILE_ENTITY_SUPPLIER_BUILDER = new TileEntityBlockMethodImpl(() -> PolarisBlocks.TE_RITUAL_CORE);

	public static class Activate implements ScheduleTickBlockMethod, OnClickBlockMethod, AnimateTickBlockMethod {

		@Override
		public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
			TileEntity te = world.getBlockEntity(pos);
			if (te instanceof TE) {
				((TE) te).activate(null);
			}
		}

		@Override
		public ActionResultType onClick(BlockState bs, World w, BlockPos pos, PlayerEntity pl, Hand h, BlockRayTraceResult r) {
			if (w.isClientSide()) {
				return pl.getMainHandItem().getItem() == Items.STICK ? ActionResultType.SUCCESS : ActionResultType.PASS;
			}
			if (pl.getMainHandItem().getItem() == Items.STICK) {
				TileEntity te = w.getBlockEntity(pos);
				if (te instanceof TE) {
					((TE) te).activate(pl);
				}
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.PASS;
		}

		@Override
		public void animateTick(BlockState state, World world, BlockPos pos, Random r) {
			TileEntity tile = world.getBlockEntity(pos);
			if (!(tile instanceof TE)) return;
			TE te = (TE) tile;
			if (!te.isLocked()) return;
			List<RitualSide.TE> side = te.getSide();
			if (side.size() < 8) return;
			for (RitualSide.TE ste : side) {
				if (ste.isEmpty()) continue;
				BlockPos spos = ste.getBlockPos();
				world.addAlwaysVisibleParticle(ParticleTypes.ENCHANT,
						pos.getX() + 0.5,
						pos.getY() + 2.5,
						pos.getZ() + 0.5,
						spos.getX() - pos.getX() + r.nextFloat() - 0.5,
						spos.getY() - pos.getY() - r.nextFloat() - 0.5,
						spos.getZ() - pos.getZ() + r.nextFloat() - 0.5);
			}
		}
	}

	public static final BlockMethod CLICK = new RitualTE.RitualPlace();
	public static final BlockMethod ACTIVATE = new Activate();
	public static final int[][] POS = {{-2, -2}, {-3, 0}, {-2, 2}, {0, -3}, {0, 3}, {2, -2}, {3, 0}, {2, 2}};

	@SerialClass
	public static class TE extends RitualTE implements ITickableTileEntity {

		public AbstractMagicCraftRecipe<?> recipe = null;

		@SerialClass.SerialField
		public int remainingTime = 0;

		@SerialClass.SerialField
		public int lv = 0;

		public TE(TileEntityType<TE> type) {
			super(type);
		}

		public void activate(@Nullable PlayerEntity player) {
			if (level == null || level.isClientSide()) {
				return;
			}
			List<RitualSide.TE> list = getSide();
			if (list.size() < 8) {
				return;
			}
			//TODO sideness
			Inv inv = new Inv(this, list);
			Optional<AbstractMagicCraftRecipe<?>> r = level.getRecipeManager().getRecipeFor(PolarisRecipeTypes.RT_CRAFT, inv, level);
			r.ifPresent(e -> {
				recipe = e;
				remainingTime = 200;
				setLocked(true, list);
			});

		}

		private void send(@Nullable PlayerEntity player, ITextComponent text) {
			if (player == null) return;
			World world = player.level;
			if (world == null) return;
			MinecraftServer server = world.getServer();
			if (server == null)
				return;
			server.getPlayerList().broadcastMessage(text, ChatType.GAME_INFO, player.getUUID());
		}

		private List<RitualSide.TE> getSide() {
			assert level != null;
			List<RitualSide.TE> list = new ArrayList<>();
			for (int[] dire : POS) {
				TileEntity te = level.getBlockEntity(getBlockPos().offset(dire[0], 0, dire[1]));
				if (te instanceof RitualSide.TE) {
					list.add((RitualSide.TE) te);
				}
			}
			return list;
		}

		@Override
		public void tick() {
			if (remainingTime <= 0 || level == null || level.isClientSide())
				return;
			remainingTime--;
			if (remainingTime % 4 != 0) {
				return;
			}
			List<RitualSide.TE> list = getSide();
			if (list.size() == 8 && recipe == null) {
				Inv inv = new Inv(this, list);
				Optional<AbstractMagicCraftRecipe<?>> r = level.getRecipeManager().getRecipeFor(PolarisRecipeTypes.RT_CRAFT, inv, level);
				if (r.isPresent()) {
					recipe = r.get();
				} else {
					remainingTime = 0;
					lv = 0;
					setLocked(false, list);
					setChanged();
					return;
				}
			}
			if (list.size() < 8 || !match(list)) {
				recipe = null;
				remainingTime = 0;
				lv = 0;
				setLocked(false, list);
				setChanged();
				return;
			}
			if (remainingTime == 0) {
				Inv inv = new Inv(this, list);
				recipe.assemble(inv, lv);
				lv = 0;
				recipe = null;
				setLocked(false, list);
			}
			setChanged(); // mark the tile entity dirty every 4 ticks
		}

		private void setLocked(boolean bool, List<RitualSide.TE> list) {
			setLocked(bool);
			for (RitualSide.TE te : list) {
				te.setLocked(bool);
			}
		}

		private boolean match(List<RitualSide.TE> list) {
			if (level == null || recipe == null || list.size() < 8) {
				return false;
			}
			Inv inv = new Inv(this, list);
			return recipe.matches(inv, level);
		}

	}

	public static class Inv implements BaseRecipe.RecInv<AbstractMagicCraftRecipe<?>> {

		public final TE core;
		public final List<RitualSide.TE> sides;

		private Inv(TE core, List<RitualSide.TE> sides) {
			this.core = core;
			this.sides = sides;
		}

		private SyncedSingleItemTE getSlot(int slot) {
			return slot < 5 ? sides.get(slot) : slot == 5 ? core : sides.get(slot - 1);
		}

		@Override
		public int getContainerSize() {
			return 0;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public ItemStack getItem(int slot) {
			return getSlot(slot).getItem(0);
		}

		@Override
		public ItemStack removeItem(int slot, int count) {
			return getSlot(slot).removeItem(0, count);
		}

		@Override
		public ItemStack removeItemNoUpdate(int slot) {
			return getSlot(slot).removeItemNoUpdate(0);
		}

		@Override
		public void setItem(int slot, ItemStack stack) {
			getSlot(slot).setItem(0, stack);
		}

		@Override
		public void setChanged() {

		}

		@Override
		public boolean stillValid(PlayerEntity player) {
			return true;
		}

		@Override
		public void clearContent() {
			core.clearContent();
			for (RitualSide.TE te : sides)
				te.clearContent();
		}
	}

}
