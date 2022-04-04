package dev.lcy0x1.base.block.one;

import dev.lcy0x1.base.block.type.SingletonBlockMethod;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public interface GetBlockItemBlockMethod extends SingletonBlockMethod {

	ItemStack getCloneItemStack(IBlockReader world, BlockPos pos, BlockState state);

}
