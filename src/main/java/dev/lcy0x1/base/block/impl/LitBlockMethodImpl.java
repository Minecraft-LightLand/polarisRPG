package dev.lcy0x1.base.block.impl;

import dev.lcy0x1.base.block.mult.CreateBlockStateBlockMethod;
import dev.lcy0x1.base.block.mult.DefaultStateBlockMethod;
import dev.lcy0x1.base.block.one.LightBlockMethod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class LitBlockMethodImpl implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, LightBlockMethod {

	private final int light;

	public LitBlockMethodImpl(int light) {
		this.light = light;
	}

	@Override
	public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.LIT);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(BlockStateProperties.LIT, false);
	}

	@Override
	public int getLightValue(BlockState bs, IBlockReader w, BlockPos pos) {
		return bs.getValue(BlockStateProperties.LIT) ? light : 0;
	}
}
