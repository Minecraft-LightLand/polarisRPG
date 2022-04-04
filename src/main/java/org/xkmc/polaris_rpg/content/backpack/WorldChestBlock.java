package org.xkmc.polaris_rpg.content.backpack;

import com.google.common.collect.Lists;
import dev.lcy0x1.base.block.impl.TileEntityBlockMethodImpl;
import dev.lcy0x1.base.block.mult.*;
import dev.lcy0x1.base.block.one.GetBlockItemBlockMethod;
import dev.lcy0x1.base.block.one.SpecialDropBlockMethod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.xkmc.polaris_rpg.init.registry.PolarisBlocks;
import org.xkmc.polaris_rpg.init.registry.PolarisItems;

import java.util.List;
import java.util.UUID;

public class WorldChestBlock implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, PlacementBlockMethod,
		OnClickBlockMethod, GetBlockItemBlockMethod, SpecialDropBlockMethod, SetPlacedByBlockMethod {

	public static final WorldChestBlock INSTANCE = new WorldChestBlock();

	public static final TileEntityBlockMethodImpl TILE_ENTITY_SUPPLIER_BUILDER =
			new TileEntityBlockMethodImpl(() -> PolarisBlocks.TE_WORLD_CHEST);

	public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

	@Override
	public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(COLOR);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(COLOR, DyeColor.WHITE);
	}

	@Override
	public ActionResultType onClick(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
		ItemStack stack = player.getItemInHand(hand);
		TileEntity be = level.getBlockEntity(pos);
		if (stack.getItem() instanceof DyeItem && be instanceof WorldChestBlockEntity) {
			DyeItem dye = (DyeItem) stack.getItem();
			WorldChestBlockEntity chest = (WorldChestBlockEntity) be;
			if (!level.isClientSide()) {
				level.setBlockAndUpdate(pos, state.setValue(COLOR, dye.getDyeColor()));
				chest.setColor(dye.getDyeColor().getId());
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public ItemStack getCloneItemStack(IBlockReader world, BlockPos pos, BlockState state) {
		TileEntity be = world.getBlockEntity(pos);
		if (be instanceof WorldChestBlockEntity) {
			return buildStack(state, (WorldChestBlockEntity) be);
		}
		return PolarisItems.DIMENSIONAL_STORAGE[state.getValue(COLOR).getId()].asStack();
	}

	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		TileEntity blockentity = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
		if (blockentity instanceof WorldChestBlockEntity) {
			return Lists.newArrayList(buildStack(state, (WorldChestBlockEntity) blockentity));
		}
		return Lists.newArrayList(PolarisItems.DIMENSIONAL_STORAGE[state.getValue(COLOR).getId()].asStack());
	}

	private ItemStack buildStack(BlockState state, WorldChestBlockEntity chest) {
		ItemStack stack = PolarisItems.DIMENSIONAL_STORAGE[state.getValue(COLOR).getId()].asStack();
		stack.getOrCreateTag().putUUID("owner_id", chest.owner_id);
		stack.getOrCreateTag().putString("owner_name", chest.owner_name);
		stack.getOrCreateTag().putLong("password", chest.password);
		return stack;
	}

	@Override
	public BlockState getStateForPlacement(BlockState def, BlockItemUseContext context) {
		return def.setValue(COLOR, ((WorldChestItem) context.getItemInHand().getItem()).color);
	}

	@Override
	public void setPlacedBy(World level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		TileEntity blockentity = level.getBlockEntity(pos);
		UUID id = stack.getOrCreateTag().getUUID("owner_id");
		String name = stack.getOrCreateTag().getString("owner_name");
		long pwd = stack.getOrCreateTag().getLong("password");
		if (blockentity instanceof WorldChestBlockEntity) {
			WorldChestBlockEntity chest = (WorldChestBlockEntity) blockentity;
			chest.owner_id = id;
			chest.owner_name = name;
			chest.password = pwd;
			chest.setColor(state.getValue(COLOR).getId());
		}
	}
}
