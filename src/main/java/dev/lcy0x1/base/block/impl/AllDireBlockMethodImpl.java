package dev.lcy0x1.base.block.impl;

import dev.lcy0x1.base.block.BlockProxy;
import dev.lcy0x1.base.block.mult.CreateBlockStateBlockMethod;
import dev.lcy0x1.base.block.mult.PlacementBlockMethod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;

public class AllDireBlockMethodImpl implements PlacementBlockMethod, CreateBlockStateBlockMethod {

	public AllDireBlockMethodImpl() {
	}

	@Override
	public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockProxy.FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockState def, BlockItemUseContext context) {
		return def.setValue(BlockProxy.FACING, context.getClickedFace().getOpposite());
	}
}
