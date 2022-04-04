package dev.lcy0x1.base.block.mult;

import dev.lcy0x1.base.block.type.MultipleBlockMethod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface SetPlacedByBlockMethod extends MultipleBlockMethod {

	void setPlacedBy(World level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack);

}
