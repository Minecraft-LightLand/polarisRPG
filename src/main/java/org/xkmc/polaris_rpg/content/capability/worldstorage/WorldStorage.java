package org.xkmc.polaris_rpg.content.capability.worldstorage;

import dev.lcy0x1.core.util.SerialClass;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@SerialClass
public class WorldStorage {

	public static Capability<WorldStorage> CAPABILITY;

	public static WorldStorage get(ServerWorld level) {
		return level.getServer().overworld().getCapability(CAPABILITY).resolve().get();
	}


	public final ServerWorld level;

	@SerialClass.SerialField
	private final HashMap<String, CompoundNBT> storage = new HashMap<>();

	private final HashMap<UUID, StorageContainer[]> cache = new HashMap<>();

	public WorldStorage(ServerWorld level) {
		this.level = level;
	}

	public Optional<StorageContainer> getOrCreateStorage(UUID id, int color, long password) {
		if (cache.containsKey(id)) {
			StorageContainer storage = cache.get(id)[color];
			if (storage != null) {
				if (storage.password == password)
					return Optional.of(storage);
				return Optional.empty();
			}
		}
		CompoundNBT col = getCol(id, color, password);
		if (col.getLong("password") != password)
			return Optional.empty();
		StorageContainer storage = new StorageContainer(id, col);
		putStorage(id, color, storage);
		return Optional.of(storage);
	}

	public StorageContainer changePassword(UUID id, int color, long password) {
		cache.remove(id);
		CompoundNBT col = getCol(id, color, password);
		col.putLong("password", password);
		StorageContainer storage = new StorageContainer(id, col);
		putStorage(id, color, storage);
		return storage;
	}

	private void putStorage(UUID id, int color, StorageContainer storage) {
		StorageContainer[] arr;
		if (cache.containsKey(id))
			arr = cache.get(id);
		else cache.put(id, arr = new StorageContainer[16]);
		arr[color] = storage;
	}

	private CompoundNBT getCol(UUID id, int color, long password) {
		CompoundNBT ans;
		String sid = id.toString();
		if (!storage.containsKey(sid)) {
			storage.put(sid, ans = new CompoundNBT());
			ans.putUUID("owner_id", id);
		} else ans = storage.get(sid);
		CompoundNBT col;
		if (ans.contains("color_" + color)) {
			col = ans.getCompound("color_" + color);
		} else {
			col = new CompoundNBT();
			col.putLong("password", password);
			ans.put("color_" + color, col);
		}
		return col;
	}

	public void init() {

	}

}
