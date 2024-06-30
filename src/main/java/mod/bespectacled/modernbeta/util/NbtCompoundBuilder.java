package mod.bespectacled.modernbeta.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class NbtCompoundBuilder {
	private final CompoundTag compound;

	public NbtCompoundBuilder() {
		this.compound = new CompoundTag();
	}

	public NbtCompoundBuilder(CompoundTag initial) {
		this.compound = initial.copy();
	}

	public NbtCompoundBuilder putString(String key, String value) {
		this.compound.putString(key, value);

		return this;
	}

	public NbtCompoundBuilder putInt(String key, int value) {
		this.compound.putInt(key, value);

		return this;
	}

	public NbtCompoundBuilder putBoolean(String key, boolean value) {
		this.compound.putBoolean(key, value);

		return this;
	}

	public NbtCompoundBuilder putFloat(String key, float value) {
		this.compound.putFloat(key, value);

		return this;
	}

	public NbtCompoundBuilder putDouble(String key, double value) {
		this.compound.putDouble(key, value);

		return this;
	}

	public NbtCompoundBuilder putList(String key, ListTag list) {
		this.compound.put(key, list);

		return this;
	}

	public NbtCompoundBuilder putCompound(String key, CompoundTag compound) {
		this.compound.put(key, compound);

		return this;
	}

	public NbtCompoundBuilder putBuilder(NbtCompoundBuilder builder) {
		builder.compound.getAllKeys().forEach(key -> this.compound.put(key, compound.get(key)));

		return this;
	}

	public CompoundTag build() {
		return this.compound;
	}
}
