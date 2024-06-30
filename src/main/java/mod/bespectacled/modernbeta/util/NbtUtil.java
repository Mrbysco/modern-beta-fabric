package mod.bespectacled.modernbeta.util;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class NbtUtil {
	/*
	 * Helper methods for reading primitive values from NbtCompound objects
	 */

	public static String readStringOrThrow(String key, CompoundTag tag) {
		if (tag.contains(key))
			return tag.getString(key);

		throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
	}

	public static String readString(String key, CompoundTag tag, String alternate) {
		if (tag.contains(key))
			return tag.getString(key);

		return alternate;
	}

	public static int readIntOrThrow(String key, CompoundTag tag) {
		if (tag.contains(key))
			return tag.getInt(key);

		throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
	}

	public static int readInt(String key, CompoundTag tag, int alternate) {
		if (tag.contains(key))
			return tag.getInt(key);

		return alternate;
	}

	public static float readFloatOrThrow(String key, CompoundTag tag) {
		if (tag.contains(key))
			return tag.getFloat(key);

		throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
	}

	public static float readFloat(String key, CompoundTag tag, float alternate) {
		if (tag.contains(key))
			return tag.getFloat(key);

		return alternate;
	}

	public static double readDoubleOrThrow(String key, CompoundTag tag) {
		if (tag.contains(key))
			return tag.getDouble(key);

		throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
	}

	public static double readDouble(String key, CompoundTag tag, double alternate) {
		if (tag.contains(key))
			return tag.getDouble(key);

		return alternate;
	}

	public static boolean readBooleanOrThrow(String key, CompoundTag tag) {
		if (tag.contains(key))
			return tag.getBoolean(key);

		throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
	}

	public static boolean readBoolean(String key, CompoundTag tag, boolean alternate) {
		if (tag.contains(key))
			return tag.getBoolean(key);

		return alternate;
	}

	public static CompoundTag readCompoundOrThrow(String key, CompoundTag tag) {
		if (tag.contains(key))
			return tag.getCompound(key);

		throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
	}

	public static CompoundTag readCompound(String key, CompoundTag tag, CompoundTag alternate) {
		if (tag.contains(key))
			return tag.getCompound(key);

		return alternate;
	}

	public static ListTag readListOrThrow(String key, CompoundTag tag) {
		if (tag.contains(key))
			return (ListTag) tag.get(key);

		throw new IllegalArgumentException("[Modern Beta] NBT compound does not contain field " + key);
	}

	public static ListTag readList(String key, CompoundTag tag, ListTag alternate) {
		if (tag.contains(key))
			return (ListTag) tag.get(key);

		return alternate;
	}

	/*
	 * Conversion methods for extracting primitive values from NbtElement objects
	 */

	public static String toStringOrThrow(Tag element) {
		if (element instanceof StringTag nbtString)
			return nbtString.getAsString();

		throw new IllegalArgumentException("[Modern Beta] NBT element is not a string! Type:" + element.getId());
	}

	public static String toString(Tag element, String alternate) {
		if (element instanceof StringTag nbtString)
			return nbtString.getAsString();

		return alternate;
	}

	public static int toIntOrThrow(Tag element) {
		if (element instanceof IntTag nbtInt)
			return nbtInt.getAsInt();

		if (element instanceof ShortTag nbtShort)
			return nbtShort.getAsInt();

		throw new IllegalArgumentException("[Modern Beta] NBT element is not an int! Type: " + element.getId());
	}

	public static int toInt(Tag element, int alternate) {
		if (element instanceof IntTag nbtInt)
			return nbtInt.getAsInt();

		if (element instanceof ShortTag nbtShort)
			return nbtShort.getAsInt();

		return alternate;
	}

	public static float toFloatOrThrow(Tag element) {
		if (element instanceof FloatTag nbtFloat)
			return nbtFloat.getAsFloat();

		throw new IllegalArgumentException("[Modern Beta] NBT element is not an float! Type: " + element.getId());
	}

	public static float toFloat(Tag element, float alternate) {
		if (element instanceof FloatTag nbtFloat)
			return nbtFloat.getAsFloat();

		return alternate;
	}

	public static double toDoubleOrThrow(Tag element) {
		if (element instanceof DoubleTag nbtDouble)
			return nbtDouble.getAsDouble();

		throw new IllegalArgumentException("[Modern Beta] NBT element is not an double! Type: " + element.getId());
	}

	public static double toDouble(Tag element, double alternate) {
		if (element instanceof DoubleTag nbtDouble)
			return nbtDouble.getAsDouble();

		return alternate;
	}

	public static boolean toBooleanOrThrow(Tag element) {
		if (element instanceof ByteTag nbtByte)
			return nbtByte.getAsByte() == 1;

		throw new IllegalArgumentException("[Modern Beta] NBT element is not an byte/boolean! Type: " + element.getId());
	}

	public static boolean toBoolean(Tag element, boolean alternate) {
		if (element instanceof ByteTag nbtByte)
			return nbtByte.getAsByte() == 1;

		return alternate;
	}

	public static CompoundTag toCompoundOrThrow(Tag element) {
		if (element instanceof CompoundTag nbtCompound)
			return nbtCompound;

		throw new IllegalArgumentException("[Modern Beta] NBT element is not a compound! Type: " + element.getId());
	}

	public static CompoundTag toCompound(Tag element, CompoundTag alternate) {
		if (element instanceof CompoundTag nbtCompound)
			return nbtCompound;

		return alternate;
	}

	public static ListTag toListOrThrow(Tag element) {
		if (element instanceof ListTag nbtList)
			return nbtList;

		throw new IllegalArgumentException("[Modern Beta] NBT element is not a list! Type: " + element.getId());
	}
}
