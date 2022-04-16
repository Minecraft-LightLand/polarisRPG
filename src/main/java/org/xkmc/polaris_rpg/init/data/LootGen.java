package org.xkmc.polaris_rpg.init.data;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import org.xkmc.polaris_rpg.init.PolarisRPG;
import org.xkmc.polaris_rpg.init.data.conditions.DamageSourceCondition;
import org.xkmc.polaris_rpg.init.data.conditions.EntityTypeCondition;
import org.xkmc.polaris_rpg.init.data.conditions.ExceedDamageCondition;
import org.xkmc.polaris_rpg.init.registry.SimpleItems;

public class LootGen extends GlobalLootModifierProvider {

	public LootGen(DataGenerator gen) {
		super(gen, PolarisRPG.MODID);
	}

	public static void genLoot(RegistrateLootTableProvider pvd) {
		pvd.addLootAction(RegistrateLootTableProvider.LootType.ENTITY, t -> {

		});
	}

	public static void genModifier(GatherDataEvent event) {
		event.getGenerator().addProvider(new LootGen(event.getGenerator()));
	}

	@Override
	protected void start() {
		add("zombie_brain", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.ZOMBIE_BRAIN.getStack(1), 1),
				new EntityTypeCondition(EntityType.ZOMBIE, EntityType.DROWNED, EntityType.HUSK).get(),
				KilledByPlayer.killedByPlayer().build(),
				new ExceedDamageCondition(10).get()
		));
		add("hard_bone", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.HARD_BONE.getStack(1), 1),
				new EntityTypeCondition(EntityType.SKELETON, EntityType.STRAY).get(),
				KilledByPlayer.killedByPlayer().build(),
				new ExceedDamageCondition(10).get()
		));
		add("wither_bone", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.WITHER_BONE.getStack(1), 1),
				new EntityTypeCondition(EntityType.WITHER_SKELETON).get(),
				KilledByPlayer.killedByPlayer().build(),
				new ExceedDamageCondition(10).get()
		));
		add("hoglin_teeth", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.HOGLIN_TEETH.getStack(1), 1),
				new EntityTypeCondition(EntityType.HOGLIN, EntityType.ZOGLIN).get(),
				KilledByPlayer.killedByPlayer().build(),
				new ExceedDamageCondition(10).get()
		));
		add("piglin_teeth", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.PIGLIN_TEETH.getStack(1), 1),
				new EntityTypeCondition(EntityType.ZOMBIFIED_PIGLIN, EntityType.PIGLIN).get(),
				KilledByPlayer.killedByPlayer().build(),
				new ExceedDamageCondition(10).get()
		));
		add("ender_emerald", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.ENDER_EMERALD.getStack(1), 1),
				new EntityTypeCondition(EntityType.ENDERMAN, EntityType.ENDERMITE).get(),
				KilledByPlayer.killedByPlayer().build(),
				new ExceedDamageCondition(10).get()
		));
		add("deep_ocean_soul", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.DEEP_OCEAN_SOUL.getStack(1), 1),
				new EntityTypeCondition(EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN).get(),
				KilledByPlayer.killedByPlayer().build(),
				new ExceedDamageCondition(10).get()
		));
		add("soul_remnant", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.SOUL_REMNANT.getStack(1), 0.01f),
				KilledByPlayer.killedByPlayer().build(),
				new ExceedDamageCondition(10).get()
		));

		add("rune_gem", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.RUNE_GEM.getStack(1), 1),
				new EntityTypeCondition(EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.EVOKER).get(),
				new DamageSourceCondition(DamageSource.LIGHTNING_BOLT, 1).get()
		));

		add("shulker_heart", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.RUNE_GEM.getStack(1), 1),
				new EntityTypeCondition(EntityType.SHULKER).get(),
				new DamageSourceCondition("indirectMagic", 2).get()
		));

		add("gel", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.GEL.getStack(1), 1),
				new EntityTypeCondition(EntityType.SLIME, EntityType.MAGMA_CUBE).get(),
				new DamageSourceCondition("indirectMagic", 2).get()
		));

		add("ghast_cry", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.GHAST_CRY.getStack(1), 1),
				new EntityTypeCondition(EntityType.GHAST).get(),
				new ExceedDamageCondition(900).get()
		));

		add("end_seed", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.END_SEED.getStack(1), 1),
				new EntityTypeCondition(EntityType.ENDER_DRAGON).get(),
				KilledByPlayer.killedByPlayer().build()
		));

		add("shining_nether_star", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.SHINING_NETHER_STAR.getStack(1), 1),
				new EntityTypeCondition(EntityType.WITHER).get(),
				new ExceedDamageCondition(50).get()
		));

	}
}
