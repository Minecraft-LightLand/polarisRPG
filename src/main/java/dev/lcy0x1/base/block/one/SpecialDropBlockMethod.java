package dev.lcy0x1.base.block.one;

import dev.lcy0x1.base.block.type.SingletonBlockMethod;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;

import java.util.List;

public interface SpecialDropBlockMethod extends SingletonBlockMethod {

	List<ItemStack> getDrops(BlockState state, LootContext.Builder builder);

}
