package dev.lcy0x1.base.menu;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.lcy0x1.base.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public abstract class BaseContainerScreen<T extends BaseContainerMenu<T>> extends ContainerScreen<T> {

	public BaseContainerScreen(T cont, PlayerInventory plInv, ITextComponent title) {
		super(cont, plInv, title);
		this.imageHeight = menu.sprite.getHeight();
		this.inventoryLabelY = menu.sprite.getPlInvY() - 11;
	}

	@Override
	public void render(MatrixStack stack, int mx, int my, float partial) {
		super.render(stack, mx, my, partial);
		renderTooltip(stack, mx, my);
	}

	protected boolean click(int btn) {
		if (menu.clickMenuButton(Proxy.getClientPlayer(), btn)) {
			Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, btn);
			return true;
		}
		return false;
	}

}
