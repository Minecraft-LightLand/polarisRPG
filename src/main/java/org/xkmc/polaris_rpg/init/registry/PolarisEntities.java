package org.xkmc.polaris_rpg.init.registry;

import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.entity.EntityClassification;
import org.xkmc.polaris_rpg.content.archer.entity.GenericArrowEntity;
import org.xkmc.polaris_rpg.content.archer.entity.GenericArrowRenderer;
import org.xkmc.polaris_rpg.init.PolarisRPG;

public class PolarisEntities {

	public static final EntityEntry<GenericArrowEntity> ET_ARROW;

	static {
		ET_ARROW = PolarisRPG.REGISTRATE
				.<GenericArrowEntity>entity("generic_arrow", GenericArrowEntity::new, EntityClassification.MISC)
				.properties(e -> e.sized(0.5F, 0.5F)
						.clientTrackingRange(4).updateInterval(20)
						.setShouldReceiveVelocityUpdates(true))
				.renderer(() -> GenericArrowRenderer::new)
				.defaultLang().register();
	}

	public static void register(){

	}

}
