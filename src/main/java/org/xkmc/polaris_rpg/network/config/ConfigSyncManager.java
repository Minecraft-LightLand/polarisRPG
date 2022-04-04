package org.xkmc.polaris_rpg.network.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.lcy0x1.core.util.SerialClass;
import dev.lcy0x1.core.util.Serializer;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import org.xkmc.polaris_rpg.network.SerialPacketBase;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConfigSyncManager {

	public static HashMap<String, BaseConfig> CONFIGS = new HashMap<>();

	public static final ReloadListener<?> CONFIG = new JsonReloadListener(new Gson(), "polaris_config") {

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, IResourceManager manager, IProfiler profiler) {
			map.forEach((k, v) -> {
				BaseConfig config = Serializer.from(v, BaseConfig.class, null);
				if (config != null)
					CONFIGS.put(k.toString(), config);
			});
		}

	};

	@SerialClass
	public static class SyncPacket extends SerialPacketBase {

		@SerialClass.SerialField
		public HashMap<String, BaseConfig> map = null;

		@Deprecated
		public SyncPacket() {

		}

		public SyncPacket(HashMap<String, BaseConfig> map) {
			this.map = map;
		}

		@Override
		public void handle(NetworkEvent.Context ctx) {
			if (map != null)
				CONFIGS = map;
		}

	}

	@SerialClass
	public static class BaseConfig {

	}

	public static void onDatapackSync(OnDatapackSyncEvent event) {
		SyncPacket packet = new SyncPacket(CONFIGS);
		if (event.getPlayer() == null) packet.toAllClient();
		else packet.toClientPlayer(event.getPlayer());
	}

}
