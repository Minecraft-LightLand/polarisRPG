package org.xkmc.polaris_rpg.content.item.weapons;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.xkmc.polaris_rpg.content.profession.ToolProficiency;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class BasePickaxeItem extends PickaxeItem implements IBaseTieredItem {

	private final ToolProficiency proficiency;
	private final BaseItemTier tier;

	public BasePickaxeItem(BaseItemTier tier, ToolProficiency proficiency, Properties properties) {
		super(tier, proficiency.damage, proficiency.speed, properties);
		this.proficiency = proficiency;
		this.tier = tier;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
		tier.extra.hurtEnemy(stack, target, user);
		proficiency.addWeaponProficiency(stack, 1);
		return super.hurtEnemy(stack, target, user);
	}

	@Override
	public boolean mineBlock(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity user) {
		proficiency.addToolProficiency(stack, 1);
		return super.mineBlock(stack, world, state, pos, user);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		proficiency.addToolProficiency(context.getItemInHand(), 1);
		return super.useOn(context);
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
		if (tier.extra.unbreakable) return 0;
		return super.damageItem(stack, amount, entity, onBroken);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		if (slot != EquipmentSlotType.MAINHAND) {
			return super.getAttributeModifiers(slot, stack);
		}
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
		proficiency.addAttribute(builder, stack);
		tier.extra.addAttribute(builder, stack);
		return builder.build();
	}

	@Override
	public final float getDestroySpeed(ItemStack stack, BlockState state) {
		float speed = super.getDestroySpeed(stack, state);
		return proficiency.getMiningSpeedFactor(stack, speed);
	}

	@Override
	public final ToolProficiency getProficiency() {
		return proficiency;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
		proficiency.appendHoverText(stack, world, list, flag);
		super.appendHoverText(stack, world, list, flag);
	}

	@Override
	public void inventoryTick(ItemStack stack, World level, Entity entity, int slot, boolean selected) {
		tier.extra.inventoryTick(stack, level, entity, slot, selected);
	}

}
