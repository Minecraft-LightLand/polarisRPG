package dev.lcy0x1.base.block.impl;

import dev.lcy0x1.base.block.mult.OnClickBlockMethod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ContainerBlockMethodImpl<T extends Container> implements OnClickBlockMethod {

	private final ITextComponent title;
	private final RemoteContainerFactory<T> factory;

	public ContainerBlockMethodImpl(String title, RemoteContainerFactory<T> factory) {
		this.title = new TranslationTextComponent(title);
		this.factory = factory;
	}

	public ActionResultType onClick(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
		if (world.isClientSide) {
			return ActionResultType.SUCCESS;
		} else {
			player.openMenu(getMenuProvider(state, world, pos));
			player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
			return ActionResultType.CONSUME;
		}
	}

	public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
		return new SimpleNamedContainerProvider((wid, inventory, player) -> factory.create(wid, inventory, IWorldPosCallable.create(world, pos)), title);
	}

	public interface RemoteContainerFactory<T extends Container> {

		T create(int wid, PlayerInventory inv, IWorldPosCallable pos);

	}

}
