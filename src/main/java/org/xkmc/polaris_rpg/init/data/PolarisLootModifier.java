package org.xkmc.polaris_rpg.init.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.xkmc.polaris_rpg.init.data.conditions.BaseCondition;
import org.xkmc.polaris_rpg.init.data.conditions.DamageSourceCondition;
import org.xkmc.polaris_rpg.init.data.conditions.EntityTypeCondition;
import org.xkmc.polaris_rpg.init.data.conditions.ExceedDamageCondition;

import javax.annotation.Nonnull;
import java.util.List;

import static org.xkmc.polaris_rpg.init.PolarisRPG.REGISTRATE;

public class PolarisLootModifier extends LootModifier {

	public static final RegistryEntry<Ser> SER = REGISTRATE.simple("main", GlobalLootModifierSerializer.class, Ser::new);
	public static final LootConditionType DAMAGE_SOURCE = BaseCondition.register("damage_source", DamageSourceCondition.CODEC);
	public static final LootConditionType EXCEED_DAMAGE = BaseCondition.register("exceed_damage", ExceedDamageCondition.CODEC);
	public static final LootConditionType ENTITY_TYPE = BaseCondition.register("entity_type", EntityTypeCondition.CODEC);

	public static void register() {
	}

	public static class Config {

		public static Codec<Config> CODEC = RecordCodecBuilder.create(i -> i.group(
				ItemStack.CODEC.fieldOf("stack").forGetter(e -> e.stack),
				Codec.FLOAT.fieldOf("chance").forGetter(e -> e.chance)
		).apply(i, Config::new));

		public final ItemStack stack;
		public final float chance;

		public Config(ItemStack stack, float chance) {
			this.stack = stack;
			this.chance = chance;
		}
	}

	public final Config config;

	protected PolarisLootModifier(Config config, ILootCondition... conditions) {
		super(conditions);
		this.config = config;
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> loot, LootContext context) {
		if (context.getRandom().nextFloat() < config.chance)
			loot.add(config.stack.copy());
		return loot;
	}

	public static class Ser extends GlobalLootModifierSerializer<PolarisLootModifier> {

		@Override
		public PolarisLootModifier read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
			return new PolarisLootModifier(Config.CODEC.decode(JsonOps.INSTANCE, object).result().get().getFirst(), conditionsIn);
		}

		@Override
		public JsonObject write(PolarisLootModifier instance) {
			JsonElement conditions = this.makeConditions(instance.conditions).get("conditions");
			JsonObject ans = Config.CODEC.encodeStart(JsonOps.INSTANCE, instance.config).result().get().getAsJsonObject();
			ans.add("conditions", conditions);
			return ans;
		}
	}

}
