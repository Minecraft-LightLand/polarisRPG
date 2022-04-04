package org.xkmc.polaris_rpg.content.backpack;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.lcy0x1.base.menu.BaseContainerScreen;
import dev.lcy0x1.core.util.SpriteManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class WorldChestScreen extends BaseContainerScreen<WorldChestContainer> {

	public WorldChestScreen(WorldChestContainer cont, PlayerInventory plInv, ITextComponent title) {
		super(cont, plInv, title);
	}

	@Override
	protected void renderBg(MatrixStack stack, float pt, int mx, int my) {
		SpriteManager sm = menu.sprite;
		SpriteManager.ScreenRenderer sr = sm.getRenderer(this);
		sr.start(stack);
	}

}
