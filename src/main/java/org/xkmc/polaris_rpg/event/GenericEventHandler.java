package org.xkmc.polaris_rpg.event;

import dev.lcy0x1.core.util.SpriteManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.xkmc.polaris_rpg.network.config.ConfigSyncManager;
import org.xkmc.polaris_rpg.util.RayTraceUtil;

@SuppressWarnings("unused")
public class GenericEventHandler {

	@SubscribeEvent
	public static void onCommandRegister(RegisterCommandsEvent event) {
	}

	@SubscribeEvent
	public static void onAddReloadListenerEvent(AddReloadListenerEvent event) {
		event.addListener(new BaseJsonReloadListener(map -> {
			SpriteManager.CACHE.clear();
			SpriteManager.CACHE.putAll(map);
		}));
	}

	@SubscribeEvent
	public static void serverTick(TickEvent.ServerTickEvent event) {
		RayTraceUtil.serverTick();
	}


	@SubscribeEvent
	public static void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(ConfigSyncManager.CONFIG);
	}

	@SubscribeEvent
	public static void onDatapackSync(OnDatapackSyncEvent event) {
		ConfigSyncManager.onDatapackSync(event);
	}

}
