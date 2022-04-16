package org.xkmc.polaris_rpg.init.data;

import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
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
		add("zombie_brain_drop", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.ZOMBIE_BRAIN.getStack(1), 1),
				new EntityTypeCondition(EntityType.ZOMBIE, EntityType.DROWNED, EntityType.HUSK).get(),
				new ExceedDamageCondition(10).get()
		));
		add("rune_gem_drop", PolarisLootModifier.SER.get(), new PolarisLootModifier(
				new PolarisLootModifier.Config(SimpleItems.RUNE_GEM.getStack(1), 1),
				new EntityTypeCondition(EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.EVOKER).get(),
				new DamageSourceCondition(DamageSource.LIGHTNING_BOLT, 1).get()
		));
	}
}
