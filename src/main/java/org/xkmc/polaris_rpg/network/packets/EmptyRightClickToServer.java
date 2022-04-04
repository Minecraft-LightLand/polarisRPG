package org.xkmc.polaris_rpg.network.packets;

import dev.lcy0x1.core.util.SerialClass;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import org.xkmc.polaris_rpg.event.ItemUseEventHandler;
import org.xkmc.polaris_rpg.network.SerialPacketBase;

@SerialClass
public class EmptyRightClickToServer extends SerialPacketBase {

	@SerialClass.SerialField
	public boolean hand, right;

	public EmptyRightClickToServer(boolean right, boolean hand) {
		this.right = right;
		this.hand = hand;
	}

	@Deprecated
	public EmptyRightClickToServer() {
		this(true, true);
	}

	public void handle(NetworkEvent.Context ctx) {
		ServerPlayerEntity pl = ctx.getSender();
		if (pl != null) {
			if (right) {
				PlayerInteractEvent.RightClickEmpty event = new PlayerInteractEvent.RightClickEmpty(pl,
						hand ? Hand.MAIN_HAND : Hand.OFF_HAND);
				ItemUseEventHandler.onPlayerRightClickEmpty(event);
			} else {
				PlayerInteractEvent.LeftClickEmpty event = new PlayerInteractEvent.LeftClickEmpty(pl);
				ItemUseEventHandler.onPlayerLeftClickEmpty(event);
			}
		}
	}


}
