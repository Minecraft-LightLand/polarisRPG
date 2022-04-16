package org.xkmc.polaris_rpg.content.ritual;

import dev.lcy0x1.base.block.impl.TileEntityBlockMethodImpl;
import dev.lcy0x1.core.util.SerialClass;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.tileentity.TileEntityType;
import org.xkmc.polaris_rpg.init.registry.PolarisBlocks;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RitualSide {
	public static final TileEntityBlockMethodImpl TILE_ENTITY_SUPPLIER_BUILDER = new TileEntityBlockMethodImpl(() -> PolarisBlocks.TE_RITUAL_SIDE);

	@SerialClass
	public static class TE extends RitualTE {

		public TE(TileEntityType<TE> type) {
			super(type);
		}

	}

}
