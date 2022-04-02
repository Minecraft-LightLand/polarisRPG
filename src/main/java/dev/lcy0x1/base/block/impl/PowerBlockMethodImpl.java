package dev.lcy0x1.base.block.impl;

import dev.lcy0x1.base.block.mult.CreateBlockStateBlockMethod;
import dev.lcy0x1.base.block.mult.DefaultStateBlockMethod;
import dev.lcy0x1.base.block.one.BlockPowerBlockMethod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class PowerBlockMethodImpl implements CreateBlockStateBlockMethod, BlockPowerBlockMethod, DefaultStateBlockMethod {

	public PowerBlockMethodImpl() {
	}

	@Override
	public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.POWER);
	}

	@Override
	public int getSignal(BlockState bs, IBlockReader r, BlockPos pos, Direction d) {
		return bs.getValue(BlockStateProperties.POWER);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(BlockStateProperties.POWER, 0);
	}

}
