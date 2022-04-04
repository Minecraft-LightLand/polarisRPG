package org.xkmc.polaris_rpg.content.profession;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BaseProficiency {

	private final String tag_name;
	private final int max_proficiency;

	public BaseProficiency(String tag_name, int max_proficiency) {
		this.tag_name = tag_name;
		this.max_proficiency = max_proficiency;
	}

	public void addProficiency(ItemStack stack, int amount) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putInt(tag_name, Math.min(max_proficiency, nbt.getInt(tag_name) + amount));
	}

	public float getProficiency(ItemStack stack) {
		return 0.1f * stack.getOrCreateTag().getInt(tag_name) / max_proficiency;
	}

	public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {

	}

}
