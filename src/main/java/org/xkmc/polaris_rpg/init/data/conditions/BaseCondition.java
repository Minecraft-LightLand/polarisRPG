package org.xkmc.polaris_rpg.init.data.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.xkmc.polaris_rpg.init.PolarisRPG;

import java.util.HashMap;
import java.util.Map;

public class BaseCondition implements ILootCondition {

	public static abstract class Config<T extends Config<T>> {

		private final LootConditionType type;

		protected Config(LootConditionType type) {
			this.type = type;
		}

		public BaseCondition get(){
			return new BaseCondition(type, this);
		}

		public abstract boolean test(LootContext context);

	}

	private static final Map<ResourceLocation, LootConditionType> CONDITIONS = new HashMap<>();
	private static final Map<LootConditionType, Codec<Config<?>>> CODECS = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static LootConditionType register(String id, Codec<? extends Config<?>> codec) {
		ResourceLocation rl = new ResourceLocation(PolarisRPG.MODID, id);
		LootConditionType type = new LootConditionType(new Ser(rl));
		CONDITIONS.put(rl, type);
		CODECS.put(type, (Codec<Config<?>>) codec);
		return type;
	}

	private final LootConditionType type;
	private final Config<?> predicate;

	private BaseCondition(LootConditionType type, Config<?> predicate) {
		this.type = type;
		this.predicate = predicate;
	}

	@Override
	public LootConditionType getType() {
		return type;
	}

	@Override
	public boolean test(LootContext lootContext) {
		return predicate.test(lootContext);
	}

	public static void register() {
		CONDITIONS.forEach((k, v) -> Registry.register(Registry.LOOT_CONDITION_TYPE, k, v));
	}

	private static class Ser implements ILootSerializer<BaseCondition> {

		private final ResourceLocation rl;

		private Ser(ResourceLocation rl) {
			this.rl = rl;
		}

		@Override
		public void serialize(JsonObject object, BaseCondition condition, JsonSerializationContext context) {
			object.add("generated", CODECS.get(condition.getType()).encodeStart(JsonOps.INSTANCE, condition.predicate).result().get());
		}

		@Override
		public BaseCondition deserialize(JsonObject object, JsonDeserializationContext context) {
			LootConditionType type = CONDITIONS.get(rl);
			return new BaseCondition(type, CODECS.get(type).decode(JsonOps.INSTANCE, object.get("generated")).result().get().getFirst());
		}
	}

}
