package org.xkmc.polaris_rpg.init.data.conditions;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.xkmc.polaris_rpg.init.data.PolarisLootModifier;

import java.util.List;
import java.util.stream.Collectors;

public class EntityTypeCondition extends BaseCondition.Config<EntityTypeCondition> {

	public static Codec<EntityTypeCondition> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.list(ResourceLocation.CODEC).fieldOf("type").forGetter(e ->
					e.type.stream().map(ForgeRegistryEntry::getRegistryName).collect(Collectors.toList()))
	).apply(i, EntityTypeCondition::new));

	public final List<EntityType<?>> type;

	private EntityTypeCondition(List<ResourceLocation> type) {
		this(type.stream().map(ForgeRegistries.ENTITIES::getValue).toArray(EntityType[]::new));
	}

	public EntityTypeCondition(EntityType<?>... type) {
		super(PolarisLootModifier.ENTITY_TYPE);
		this.type = ImmutableList.copyOf(type);
	}

	@Override
	public boolean test(LootContext context) {
		Entity self = context.getParamOrNull(LootParameters.THIS_ENTITY);
		return self != null && type.contains(self.getType());
	}

}
