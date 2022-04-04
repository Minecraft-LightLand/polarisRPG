package org.xkmc.polaris_rpg.content.item.weapons;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ToolProficiency {

	private static final UUID BASE_ATTACK_DAMAGE_UUID = UUID.randomUUID();
	private static final UUID BASE_ATTACK_SPEED_UUID = UUID.randomUUID();

	private static final String TAG_TOOL = "tool_proficiency";
	private static final String TAG_WEAPON = "weapon_proficiency";

	private final int max_weapon_proficiency;
	private final int max_tool_proficiency;
	public final int damage;
	public final float speed;

	public ToolProficiency(int max_weapon_proficiency, int max_tool_proficiency, int damage, float speed) {
		this.max_weapon_proficiency = max_weapon_proficiency;
		this.max_tool_proficiency = max_tool_proficiency;
		this.damage = damage;
		this.speed = speed;
	}

	public void addAttribute(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder, ItemStack stack) {
		int prof = stack.getOrCreateTag().getInt(TAG_WEAPON);
		double factor = 0.5 + 0.5 * (prof * 1.0f / max_weapon_proficiency);
		double damage = this.damage * factor;
		double speed = Math.min(0, (this.speed + 4) * factor - 4);
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", damage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", speed, AttributeModifier.Operation.ADDITION));
	}

	public void addWeaponProficiency(ItemStack stack, int amount) {
		CompoundNBT tag = stack.getOrCreateTag();
		tag.putInt(TAG_WEAPON, Math.min(max_weapon_proficiency, tag.getInt(TAG_WEAPON) + amount));
	}

	public void addToolProficiency(ItemStack stack, int amount) {
		CompoundNBT tag = stack.getOrCreateTag();
		tag.putInt(TAG_TOOL, Math.min(max_tool_proficiency, tag.getInt(TAG_TOOL) + amount));
	}

	public float getMiningSpeedFactor(ItemStack stack, float speed) {
		if (speed <= 1.0f + 1e-3) {
			return speed;
		}
		int prof = stack.getOrCreateTag().getInt(TAG_TOOL);
		return 1 + (speed - 1) * (0.5f + 0.5f * (prof * 1.0f / max_tool_proficiency));
	}

	public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {

	}

}
