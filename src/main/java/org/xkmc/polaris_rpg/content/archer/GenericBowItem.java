package org.xkmc.polaris_rpg.content.archer;

import dev.lcy0x1.base.Proxy;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import org.xkmc.polaris_rpg.content.archer.feature.FeatureList;
import org.xkmc.polaris_rpg.content.archer.bow.WindBowFeature;
import org.xkmc.polaris_rpg.content.item.FastItem;
import org.xkmc.polaris_rpg.init.PolarisClient;
import org.xkmc.polaris_rpg.init.registry.PolarisMagic;
import org.xkmc.polaris_rpg.util.GenericItemStack;

import java.util.function.Predicate;

public class GenericBowItem extends BowItem implements FastItem {

	public static class BowConfig {
		public final float damage;
		public final int punch;
		public final int pull_time;
		public final float speed;
		public final int fov_time;
		public final float fov;
		public final FeatureList feature;

		public BowConfig(float damage, int punch, int pull_time, float speed, int fov_time, float fov, FeatureList feature) {
			this.damage = damage;
			this.punch = punch;
			this.pull_time = pull_time;
			this.speed = speed;
			this.fov_time = fov_time;
			this.fov = fov;
			this.feature = feature;
		}
	}

	public final BowConfig config;

	public GenericBowItem(Properties properties, BowConfig config) {
		super(properties);
		this.config = config;
		PolarisClient.BOW_LIKE.add(this);
	}

	/**
	 * on release bow
	 */
	public void releaseUsing(ItemStack bow, World level, LivingEntity user, int remaining_pull_time) {
		if (user instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) user;
			BowFeatureController.stopUsing(player, new GenericItemStack<>(this, bow));
			boolean has_inf = player.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) > 0;
			ItemStack arrow = player.getProjectile(bow);
			int pull_time = this.getUseDuration(bow) - remaining_pull_time;
			pull_time = ForgeEventFactory.onArrowLoose(bow, level, player, pull_time, !arrow.isEmpty() || has_inf);
			if (pull_time < 0) return;
			if (arrow.isEmpty() && !has_inf) {
				return;
			}
			if (arrow.isEmpty()) { // no arrow: use default arrow
				arrow = new ItemStack(Items.ARROW);
			}
			float power = getPowerForTime(user, pull_time);
			if (((double) power < 0.1D)) { // not enough power: cancel
				return;
			}
			boolean no_consume = player.abilities.instabuild || (arrow.getItem() instanceof ArrowItem && ((ArrowItem) arrow.getItem()).isInfinite(arrow, bow, player));
			if (!level.isClientSide) {
				if (!shootArrowOnServer(player, level, bow, arrow, power, no_consume))
					return;
			}

			float pitch = 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F;
			level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, pitch);
			if (!no_consume && !player.abilities.instabuild) {
				arrow.shrink(1);
				if (arrow.isEmpty()) {
					player.inventory.removeItem(arrow);
				}
			}
			player.awardStat(Stats.ITEM_USED.get(this));
		}
	}

	/**
	 * create arrow entity and add to world
	 */
	private boolean shootArrowOnServer(PlayerEntity player, World level, ItemStack bow, ItemStack arrow, float power, boolean no_consume) {
		AbstractArrowEntity abstractarrow;
		if (arrow.getItem() instanceof GenericArrowItem) {
			GenericArrowItem genericArrow = (GenericArrowItem) arrow.getItem();
			abstractarrow = ArrowFeatureController.createArrowEntity(
					new ArrowFeatureController.BowArrowUseContext(level, player, no_consume, power),
					new GenericItemStack<>(this, bow),
					new GenericItemStack<>(genericArrow, arrow));
		} else {
			ArrowItem arrowitem = (ArrowItem) (arrow.getItem() instanceof ArrowItem ? arrow.getItem() : Items.ARROW);
			abstractarrow = arrowitem.createArrow(level, arrow, player);
			abstractarrow = customArrow(abstractarrow);
			abstractarrow.shootFromRotation(player, player.xRot, player.yRot, 0.0F, power * 3f, 1.0F);
			if (power == 1.0F) {
				abstractarrow.setCritArrow(true);
			}

			int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bow);
			if (j > 0) {
				abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double) j * 0.5D + 0.5D);
			}

			int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bow);
			if (k > 0) {
				abstractarrow.setKnockback(k);
			}

			if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bow) > 0) {
				abstractarrow.setSecondsOnFire(100);
			}

			if (no_consume || player.abilities.instabuild && (arrow.getItem() == Items.SPECTRAL_ARROW || arrow.getItem() == Items.TIPPED_ARROW)) {
				abstractarrow.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
			}
		}
		if (abstractarrow == null) {
			return false;
		}
		bow.hurtAndBreak(1, player, (pl) -> pl.broadcastBreakEvent(player.getUsedItemHand()));
		level.addFreshEntity(abstractarrow);
		return true;
	}

	public float getPullForTime(LivingEntity entity, float time) {
		float f = time / config.pull_time;
		EffectInstance ins =  entity.getEffect(PolarisMagic.QUICK_PULL.get());
		if (ins != null) {
			f *= (1.5 + 0.5 * ins.getAmplifier());
		}
		return Math.min(1, f);
	}

	/**
	 * power of arrow, range 0~1
	 * Formula: (t*(t+2))/3
	 * Full in 1 second
	 */
	public float getPowerForTime(LivingEntity entity, float time) {
		float f = getPullForTime(entity, time);
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > 1.0F) {
			f = 1.0F;
		}
		return Math.min(1, f);
	}

	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity user, int count) {
		if (user instanceof PlayerEntity)
			BowFeatureController.usingTick((PlayerEntity) user, new GenericItemStack<>(this, stack));
	}

	/**
	 * On start pulling
	 */
	public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		boolean flag = !player.getProjectile(itemstack).isEmpty();
		ActionResult<ItemStack> ret = ForgeEventFactory.onArrowNock(itemstack, level, player, hand, flag);
		if (ret != null) return ret;

		if (!player.abilities.instabuild && !flag) {
			return ActionResult.fail(itemstack);
		} else {
			player.startUsingItem(hand);
			BowFeatureController.startUsing(player, new GenericItemStack<>(this, itemstack));
			return ActionResult.consume(itemstack);
		}
	}

	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return (stack) -> {
			if (ItemTags.ARROWS.contains(stack.getItem())) {
				return true;
			}
			if (stack.getItem() instanceof GenericArrowItem) {
				return ArrowFeatureController.canBowUseArrow(this, new GenericItemStack<>((GenericArrowItem) stack.getItem(), stack));
			}
			return false;
		};
	}

	/**
	 * return custom arrow entity
	 */
	public AbstractArrowEntity customArrow(AbstractArrowEntity arrow) {
		return arrow;
	}

	/**
	 * For mobs
	 */
	public int getDefaultProjectileRange() {
		return 15;
	}

	@Override
	public boolean isFast(ItemStack stack) {
		if (Proxy.getPlayer().hasEffect(PolarisMagic.RUN_BOW.get())) return true;
		return config.feature.pull.stream().anyMatch(e -> e instanceof WindBowFeature);
	}

}
