package org.xkmc.polaris_rpg.content.archer.entity;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tterrag.registrate.util.nullness.FieldsAreNonnullByDefault;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import org.xkmc.polaris_rpg.content.archer.feature.FeatureList;
import org.xkmc.polaris_rpg.content.archer.feature.types.FlightControlFeature;
import org.xkmc.polaris_rpg.content.archer.item.GenericArrowItem;
import org.xkmc.polaris_rpg.content.archer.item.GenericBowItem;
import org.xkmc.polaris_rpg.init.PolarisRPG;
import org.xkmc.polaris_rpg.init.registry.PolarisEntities;
import org.xkmc.polaris_rpg.init.registry.PolarisItems;
import org.xkmc.polaris_rpg.util.GenericItemStack;
import org.xkmc.polaris_rpg.util.ServerOnly;

import java.util.Objects;

@FieldsAreNonnullByDefault
public class GenericArrowEntity extends AbstractArrowEntity implements IEntityAdditionalSpawnData {

	public static class ArrowEntityData {

		public static final Codec<ArrowEntityData> CODEC = RecordCodecBuilder.create(i -> i.group(
				ItemStack.CODEC.fieldOf("bow").forGetter(e -> e.bow.stack),
				ItemStack.CODEC.fieldOf("arrow").forGetter(e -> e.arrow.stack),
				Codec.BOOL.fieldOf("no_consume").forGetter(e -> e.no_consume),
				Codec.FLOAT.fieldOf("power").forGetter(e -> e.power)
		).apply(i, (bow, arrow, no_consume, power) -> new ArrowEntityData(
				GenericItemStack.of(bow),
				GenericItemStack.of(arrow),
				no_consume, power
		)));

		public static final ArrowEntityData DEFAULT = new ArrowEntityData(
				GenericItemStack.from(PolarisItems.STARTER_BOW.get()),
				GenericItemStack.from(PolarisItems.STARTER_ARROW.get()),
				false, 1);

		public final GenericItemStack<GenericBowItem> bow;
		public final GenericItemStack<GenericArrowItem> arrow;
		public final boolean no_consume;
		public final float power;

		public ArrowEntityData(GenericItemStack<GenericBowItem> bow, GenericItemStack<GenericArrowItem> arrow, boolean no_consume, float power) {
			this.bow = bow;
			this.arrow = arrow;
			this.no_consume = no_consume;
			this.power = power;
		}
	}

	@ServerOnly
	public ArrowEntityData data = ArrowEntityData.DEFAULT;

	@ServerOnly
	public FeatureList features = new FeatureList();

	public GenericArrowEntity(EntityType<GenericArrowEntity> type, World level) {
		super(type, level);
	}

	public GenericArrowEntity(World level, LivingEntity user, ArrowEntityData data, FeatureList features) {
		super(PolarisEntities.ET_ARROW.get(), user, level);
		this.data = data;
		this.features = features;
	}

	@Override
	protected void doPostHurtEffects(LivingEntity target) {
		super.doPostHurtEffects(target);
		features.hit.forEach(e -> e.onHitEntity(this, target));
	}

	@ServerOnly
	@Override
	protected ItemStack getPickupItem() {
		return data.arrow.stack;
	}

	@Override
	public void tick() {
		Vector3d velocity = getDeltaMovement();
		super.tick();
		FlightControlFeature flight = features.getFlightControl();
		flight.tickMotion(this, velocity);
		if (flight.life > 0 && this.tickCount > flight.life) {
			this.remove();
		}
	}

	protected void tickDespawn() {
		++this.life;
		if (this.life >= features.getFlightControl().ground_life) {
			this.remove();
		}

	}

	protected void onHitBlock(BlockRayTraceResult result) {
		super.onHitBlock(result);
		features.hit.forEach(e -> e.onHitBlock(this, result));
	}

	@ServerOnly
	@Override
	public void addAdditionalSaveData(CompoundNBT tag) {
		super.addAdditionalSaveData(tag);
		DataResult<INBT> data_tag = ArrowEntityData.CODEC.encodeStart(NBTDynamicOps.INSTANCE, data);
		if (data_tag.error().isPresent()) {
			PolarisRPG.LOGGER.error(data_tag.error().get());
		} else if (data_tag.get().left().isPresent()) {
			tag.put("archery", data_tag.get().left().get());
		}
	}

	@ServerOnly
	@Override
	public void readAdditionalSaveData(CompoundNBT tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("archery")) {
			CompoundNBT data_tag = tag.getCompound("archery");
			DataResult<Pair<ArrowEntityData, INBT>> result = ArrowEntityData.CODEC.decode(NBTDynamicOps.INSTANCE, data_tag);
			result.get().left().ifPresent(e -> this.data = e.getFirst());
		}
		features = Objects.requireNonNull(FeatureList.merge(data.bow.item.config.feature, data.arrow.item.config.feature.get()));
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		DataResult<INBT> data_tag = ArrowEntityData.CODEC.encodeStart(NBTDynamicOps.INSTANCE, data);
		if (data_tag.error().isPresent()) {
			PolarisRPG.LOGGER.error(data_tag.error().get());
		} else if (data_tag.get().left().isPresent()) {
			buffer.writeNbt((CompoundNBT) data_tag.get().left().get());
		}

	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		CompoundNBT data_tag = additionalData.readAnySizeNbt();
		DataResult<Pair<ArrowEntityData, INBT>> result = ArrowEntityData.CODEC.decode(NBTDynamicOps.INSTANCE, data_tag);
		result.get().left().ifPresent(e -> this.data = e.getFirst());
		features = Objects.requireNonNull(FeatureList.merge(data.bow.item.config.feature, data.arrow.item.config.feature.get()));
		features.shot.forEach(e -> e.onClientShoot(this));
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
