package dev.lcy0x1.base.block.mult;

import dev.lcy0x1.base.block.type.MultipleBlockMethod;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public interface RandomTickBlockMethod extends MultipleBlockMethod {

	void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

}
