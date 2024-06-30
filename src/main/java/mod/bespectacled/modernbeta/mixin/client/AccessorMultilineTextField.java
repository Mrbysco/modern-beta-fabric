package mod.bespectacled.modernbeta.mixin.client;

import net.minecraft.client.gui.components.MultilineTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultilineTextField.class)
public interface AccessorMultilineTextField {
	@Invoker("onValueChange")
	public void invokeOnValueChange();
}
