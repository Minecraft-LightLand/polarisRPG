package org.xkmc.polaris_rpg.util;

import com.google.common.collect.Maps;
import dev.lcy0x1.base.Proxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.xkmc.polaris_rpg.event.ClientEntityEffectRenderEvents;
import org.xkmc.polaris_rpg.network.packets.TargetSetPacket;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

public class RayTraceUtil {

	public static final int CLIENT_TIMEOUT = 100;
	public static final int SERVER_TIMEOUT = 200;

	public static final ClientEntityEffectRenderEvents.EntityTarget TARGET = new EnderEntityTarget();
	public static final ConcurrentMap<UUID, ServerTarget> TARGET_MAP = Maps.newConcurrentMap();


	public static BlockRayTraceResult rayTraceBlock(World worldIn, PlayerEntity player, double reach) {
		float xRot = player.xRot;
		float yRot = player.yRot;
		Vector3d Vector3d = new Vector3d(player.getX(), player.getEyeY(), player.getZ());
		Vector3d Vector3d1 = getRayTerm(Vector3d, xRot, yRot, reach);
		return worldIn.clip(new RayTraceContext(Vector3d, Vector3d1, RayTraceContext.BlockMode.OUTLINE,
				RayTraceContext.FluidMode.NONE, player));
	}

	public static EntityRayTraceResult rayTraceEntity(PlayerEntity player, double reach, Predicate<Entity> pred) {
		World world = player.level;
		Vector3d pos = new Vector3d(player.getX(), player.getEyeY(), player.getZ());
		Vector3d end = getRayTerm(pos, player.xRot, player.yRot, reach);
		AxisAlignedBB box = new AxisAlignedBB(pos, end).inflate(1);
		double d0 = reach * reach;
		Entity entity = null;
		Vector3d Vector3d = null;
		for (Entity e : world.getEntities(player, box)) {
			if (!pred.test(e))
				continue;
			AxisAlignedBB aabb = e.getBoundingBox().inflate(e.getPickRadius());
			Optional<Vector3d> optional = aabb.clip(pos, end);
			if (aabb.contains(pos)) {
				if (d0 >= 0.0D) {
					entity = e;
					Vector3d = optional.orElse(pos);
					d0 = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vector3d Vector3d1 = optional.get();
				double d1 = pos.distanceToSqr(Vector3d1);
				if (d1 < d0 || d0 == 0.0D) {
					if (e.getRootVehicle() == player.getRootVehicle()) {
						if (d0 == 0.0D) {
							entity = e;
							Vector3d = Vector3d1;
						}
					} else {
						entity = e;
						Vector3d = Vector3d1;
						d0 = d1;
					}
				}
			}
		}
		return entity == null ? null : new EntityRayTraceResult(entity, Vector3d);
	}

	public static Vector3d getRayTerm(Vector3d pos, float xRot, float yRot, double reach) {
		float f2 = MathHelper.cos(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f3 = MathHelper.sin(-yRot * ((float) Math.PI / 180F) - (float) Math.PI);
		float f4 = -MathHelper.cos(-xRot * ((float) Math.PI / 180F));
		float f5 = MathHelper.sin(-xRot * ((float) Math.PI / 180F));
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		return pos.add(f6 * reach, f5 * reach, f7 * reach);
	}

	public static void serverTick() {
		TARGET_MAP.entrySet().removeIf(e -> {
			Optional<ServerPlayerEntity> player = Proxy.getServer().map(MinecraftServer::getPlayerList).map(x -> x.getPlayer(e.getKey()));
			if (!player.isPresent()) return true;
			ServerTarget target = e.getValue();
			Entity entity = ((ServerWorld) (player.get().level)).getEntity(target.target);
			if (entity == null || !entity.isAlive()) {
				return true;
			}
			target.time++;
			return target.time >= SERVER_TIMEOUT;
		});
	}

	public static void sync(TargetSetPacket packet) {
		if (packet.target == null) TARGET_MAP.remove(packet.player);
		else if (TARGET_MAP.containsKey(packet.player)) {
			ServerTarget target = TARGET_MAP.get(packet.player);
			target.target = packet.target;
			target.time = 0;
		} else TARGET_MAP.put(packet.player, new ServerTarget(packet.target));
	}

	public static class EnderEntityTarget extends ClientEntityEffectRenderEvents.EntityTarget {

		private int timeout = 0;

		public EnderEntityTarget() {
			super(3, Math.PI / 180 * 5, 10);
		}

		@Override
		public void onChange(Entity entity) {
			UUID eid = entity == null ? null : entity.getUUID();
			new TargetSetPacket(Proxy.getClientPlayer().getUUID(), eid).toServer();
			timeout = 0;
		}

		@Override
		public void tickRender() {
			super.tickRender();
			if (target != null) {
				timeout++;
				if (timeout > CLIENT_TIMEOUT) {
					onChange(target);
				}
			}
		}
	}

	public static class ServerTarget {

		public UUID target;
		public int time;

		public ServerTarget(UUID target) {
			this.target = target;
			time = 0;
		}
	}

	public static void clientUpdateTarget(PlayerEntity player, double range) {
		if (player.level.isClientSide()) {
			Vector3d vec3 = player.getEyePosition(1f);
			Vector3d vec31 = player.getViewVector(1.0F).scale(range);
			Vector3d vec32 = vec3.add(vec31);
			AxisAlignedBB aabb = player.getBoundingBox().expandTowards(vec31).inflate(1.0D);
			double sq = range * range;
			Predicate<Entity> predicate = (e) -> (e instanceof LivingEntity) && !e.isSpectator();
			EntityRayTraceResult result = ProjectileHelper.getEntityHitResult(player, vec3, vec32, aabb, predicate, sq);
			if (result != null && vec3.distanceToSqr(result.getLocation()) < sq) {
				RayTraceUtil.TARGET.updateTarget(result.getEntity());
			}
		}
	}

	public static LivingEntity serverGetTarget(PlayerEntity player) {
		if (player.level.isClientSide()) {
			return (LivingEntity) TARGET.target;
		}
		UUID id = player.getUUID();
		if (!RayTraceUtil.TARGET_MAP.containsKey(id))
			return null;
		UUID tid = RayTraceUtil.TARGET_MAP.get(id).target;
		if (tid == null)
			return null;
		return (LivingEntity) (((ServerWorld) player.level).getEntity(tid));
	}
}
