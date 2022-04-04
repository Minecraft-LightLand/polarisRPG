package dev.lcy0x1.base.block;

import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;

public interface NameSetable extends INameable {

	void setCustomName(ITextComponent component);

}
