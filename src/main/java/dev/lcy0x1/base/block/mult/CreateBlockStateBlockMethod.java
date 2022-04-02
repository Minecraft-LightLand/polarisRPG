package dev.lcy0x1.base.block.mult;

import dev.lcy0x1.base.block.type.MultipleBlockMethod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;

public interface CreateBlockStateBlockMethod extends MultipleBlockMethod {

	void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder);

}