package dev.lcy0x1.core.util;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Capable of handing primitive types, array, BlockPos, ItemStack, inheritance
 * <br>
 * Not capable of handing collections
 */
public class Automator {

	private static final Map<Class<?>, ClassHandler<?, ?>> MAP = new HashMap<>();

	static {
		new ClassHandler<>(Long.class, LongNBT::getAsLong, LongNBT::valueOf, long.class);
		new ClassHandler<>(Integer.class, IntNBT::getAsInt, IntNBT::valueOf, int.class);
		new ClassHandler<>(Short.class, ShortNBT::getAsShort, ShortNBT::valueOf, short.class);
		new ClassHandler<>(Byte.class, ByteNBT::getAsByte, ByteNBT::valueOf, byte.class);
		new ClassHandler<ByteNBT, Boolean>(Boolean.class, tag -> tag.getAsByte() != 0, ByteNBT::valueOf, boolean.class);
		new ClassHandler<>(Float.class, FloatNBT::getAsFloat, FloatNBT::valueOf, float.class);
		new ClassHandler<>(Double.class, DoubleNBT::getAsDouble, DoubleNBT::valueOf, double.class);
		new ClassHandler<>(long[].class, LongArrayNBT::getAsLongArray, LongArrayNBT::new);
		new ClassHandler<>(int[].class, IntArrayNBT::getAsIntArray, IntArrayNBT::new);
		new ClassHandler<>(byte[].class, ByteArrayNBT::getAsByteArray, ByteArrayNBT::new);
		new ClassHandler<StringNBT, String>(String.class, INBT::getAsString, StringNBT::valueOf);
		new ClassHandler<>(ItemStack.class, ItemStack::of, is -> is.save(new CompoundNBT()));
		new ClassHandler<CompoundNBT, BlockPos>(BlockPos.class,
				tag -> new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z")),
				obj -> {
					CompoundNBT tag = new CompoundNBT();
					tag.putInt("x", obj.getX());
					tag.putInt("y", obj.getY());
					tag.putInt("z", obj.getZ());
					return tag;
				});
		new ClassHandler<CompoundNBT, Vector3d>(Vector3d.class,
				tag -> new Vector3d(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z")),
				obj -> {
					CompoundNBT tag = new CompoundNBT();
					tag.putDouble("x", obj.x());
					tag.putDouble("y", obj.y());
					tag.putDouble("z", obj.z());
					return tag;
				});

		new ClassHandler<StringNBT, UUID>(UUID.class,
				tag -> UUID.fromString(tag.getAsString()),
				id -> StringNBT.valueOf(id.toString())
		);
		new ClassHandler<CompoundNBT, CompoundNBT>(CompoundNBT.class, e -> e, e -> e);
		new ClassHandler<ListNBT, ListNBT>(ListNBT.class, e -> e, e -> e);
		new ClassHandler<StringNBT, ResourceLocation>(ResourceLocation.class, tag -> new ResourceLocation(tag.getAsString()), rl -> StringNBT.valueOf(rl.toString()));
		new RegistryClassHandler<>(Block.class, () -> ForgeRegistries.BLOCKS);
		new RegistryClassHandler<>(Item.class, () -> ForgeRegistries.ITEMS);
		new RegistryClassHandler<>(Enchantment.class, () -> ForgeRegistries.ENCHANTMENTS);
		new RegistryClassHandler<>(Effect.class, () -> ForgeRegistries.POTIONS);
	}

	public static Object fromTag(CompoundNBT tag, Class<?> cls, Object obj, Predicate<SerialClass.SerialField> pred)
			throws Exception {
		if (tag.contains("_class"))
			cls = Class.forName(tag.getString("_class"));
		if (obj == null)
			obj = cls.getConstructor().newInstance();
		Class<?> mcls = cls;
		while (cls.getAnnotation(SerialClass.class) != null) {
			for (Field f : cls.getDeclaredFields()) {
				SerialClass.SerialField sf = f.getAnnotation(SerialClass.SerialField.class);
				if (sf == null || !pred.test(sf))
					continue;
				INBT itag = tag.get(f.getName());
				f.setAccessible(true);
				if (itag != null)
					f.set(obj, fromTagRaw(itag, TypeInfo.of(f), f.get(obj), pred));
			}
			cls = cls.getSuperclass();
		}
		cls = mcls;
		while (cls.getAnnotation(SerialClass.class) != null) {
			Method m0 = null;
			for (Method m : cls.getDeclaredMethods()) {
				if (m.getAnnotation(SerialClass.OnInject.class) != null) {
					m0 = m;
				}
			}
			if (m0 != null) {
				m0.invoke(obj);
				break;
			}
			cls = cls.getSuperclass();
		}
		return obj;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static Object fromTagRaw(INBT tag, TypeInfo cls, Object def, Predicate<SerialClass.SerialField> pred) throws Exception {
		if (tag == null)
			if (cls.getAsClass() == ItemStack.class)
				return ItemStack.EMPTY;
			else
				return null;
		if (MAP.containsKey(cls.getAsClass()))
			return MAP.get(cls.getAsClass()).fromTag.apply(tag);
		if (cls.isArray()) {
			ListNBT list = (ListNBT) tag;
			int n = list.size();
			TypeInfo com = cls.getComponentType();
			Object ans = Array.newInstance(com.getAsClass(), n);
			for (int i = 0; i < n; i++) {
				Array.set(ans, i, fromTagRaw(list.get(i), com, null, pred));
			}
			return ans;
		}
		if (List.class.isAssignableFrom(cls.getAsClass())) {
			ListNBT list = (ListNBT) tag;
			TypeInfo com = cls.getGenericType(0);
			if (def == null)
				def = cls.newInstance();
			List ans = (List<?>) def;
			ans.clear();
			for (INBT iTag : list) {
				ans.add(fromTagRaw(iTag, com, null, pred));
			}
			return ans;
		}
		if (Map.class.isAssignableFrom(cls.getAsClass())) {
			if (def == null)
				def = cls.newInstance();
			TypeInfo key = cls.getGenericType(0);
			TypeInfo val = cls.getGenericType(1);
			CompoundNBT ctag = (CompoundNBT) tag;
			Map map = (Map) def;
			map.clear();
			for (String str : ctag.getAllKeys()) {
				Object mkey = key.getAsClass() == String.class ? str :
						MAP.get(key.getAsClass()).fromTag.apply(StringNBT.valueOf(str));
				map.put(mkey, fromTagRaw(ctag.get(str), val, null, pred));
			}
			return map;
		}
		if (cls.getAsClass().isEnum()) {
			return Enum.valueOf((Class) cls.getAsClass(), tag.getAsString());
		}
		if (cls.getAsClass().getAnnotation(SerialClass.class) != null)
			return fromTag((CompoundNBT) tag, cls.getAsClass(), def, pred);
		throw new Exception("unsupported class " + cls);
	}

	public static CompoundNBT toTag(CompoundNBT tag, Class<?> cls, Object obj, Predicate<SerialClass.SerialField> pred)
			throws Exception {
		if (obj == null)
			return tag;
		if (obj.getClass() != cls) {
			tag.putString("_class", obj.getClass().getName());
			cls = obj.getClass();
		}
		while (cls.getAnnotation(SerialClass.class) != null) {
			for (Field f : cls.getDeclaredFields()) {
				SerialClass.SerialField sf = f.getAnnotation(SerialClass.SerialField.class);
				if (sf == null || !pred.test(sf))
					continue;
				f.setAccessible(true);
				if (f.get(obj) != null)
					tag.put(f.getName(), toTagRaw(TypeInfo.of(f), f.get(obj), pred));
			}
			cls = cls.getSuperclass();
		}
		return tag;
	}

	public static CompoundNBT toTag(CompoundNBT tag, Object obj) {
		return ExceptionHandler.get(() -> toTag(tag, obj.getClass(), obj, f -> true));
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromTag(CompoundNBT tag, Class<?> cls) {
		return (T) ExceptionHandler.get(() -> fromTag(tag, cls, null, f -> true));
	}

	public static INBT toTagRaw(TypeInfo cls, Object obj, Predicate<SerialClass.SerialField> pred) throws Exception {
		if (MAP.containsKey(cls.getAsClass()))
			return MAP.get(cls.getAsClass()).toTag.apply(obj);
		if (cls.isArray()) {
			ListNBT list = new ListNBT();
			int n = Array.getLength(obj);
			TypeInfo com = cls.getComponentType();
			for (int i = 0; i < n; i++) {
				list.add(toTagRaw(com, Array.get(obj, i), pred));
			}
			return list;
		}
		if (List.class.isAssignableFrom(cls.getAsClass())) {
			ListNBT list = new ListNBT();
			int n = ((List<?>) obj).size();
			TypeInfo com = cls.getGenericType(0);
			for (int i = 0; i < n; i++) {
				list.add(toTagRaw(com, ((List<?>) obj).get(i), pred));
			}
			return list;
		}
		if (Map.class.isAssignableFrom(cls.getAsClass())) {
			TypeInfo key = cls.getGenericType(0);
			TypeInfo val = cls.getGenericType(1);
			CompoundNBT ctag = new CompoundNBT();
			Map<?, ?> map = (Map<?, ?>) obj;
			for (Object str : map.keySet()) {
				String mkey = key.getAsClass() == String.class ? (String) str :
						toTagRaw(key, str, pred).getAsString();
				ctag.put(mkey, toTagRaw(val, map.get(str), pred));
			}
			return ctag;
		}
		if (cls.getAsClass().isEnum())
			return StringNBT.valueOf(((Enum<?>) obj).name());
		if (cls.getAsClass().getAnnotation(SerialClass.class) != null)
			return toTag(new CompoundNBT(), cls.getAsClass(), obj, pred);
		throw new Exception("unsupported class " + cls);
	}

	public static class ClassHandler<R extends INBT, T> {

		private final Function<INBT, ?> fromTag;
		private final Function<Object, INBT> toTag;

		@SuppressWarnings("unchecked")
		public ClassHandler(Class<T> cls, Function<R, T> ft, Function<T, INBT> tt, Class<?>... alt) {
			fromTag = (Function<INBT, ?>) ft;
			toTag = (Function<Object, INBT>) tt;
			MAP.put(cls, this);
			for (Class<?> c : alt)
				MAP.put(c, this);
		}

	}

	public static class RegistryClassHandler<T extends IForgeRegistryEntry<T>> extends ClassHandler<StringNBT, T> {

		public RegistryClassHandler(Class<T> cls, Supplier<IForgeRegistry<T>> sup) {
			super(cls, s -> s.getAsString().length() == 0 ? null : sup.get().getValue(new ResourceLocation(s.getAsString())),
					t -> t == null ? StringNBT.valueOf("") : StringNBT.valueOf(t.getRegistryName().toString()));
		}
	}

}