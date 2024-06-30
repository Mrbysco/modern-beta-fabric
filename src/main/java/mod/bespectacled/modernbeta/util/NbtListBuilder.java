package mod.bespectacled.modernbeta.util;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class NbtListBuilder {
	private final ListTag list;
	private int index;

	public NbtListBuilder() {
		this.list = new ListTag();
		this.index = 0;
	}

	public NbtListBuilder add(Tag element) {
		this.list.add(this.index, element);
		index++;

		return this;
	}

	public ListTag build() {
		return this.list;
	}
}
