package org.xkmc.polaris_rpg.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GenericItemStack<I extends Item>{

	public final I item;
	public final ItemStack stack;

	public GenericItemStack(I item, ItemStack stack) {
		this.item = item;
		this.stack = stack;
	}

	@SuppressWarnings({"unchecked"})
	public static <T extends Item> GenericItemStack<T> of(ItemStack stack) {
		return new GenericItemStack<>((T) stack.getItem(), stack);
	}

	public static <T extends Item> GenericItemStack<T> from(T item) {
		return new GenericItemStack<>(item, item.getDefaultInstance());
	}
}
