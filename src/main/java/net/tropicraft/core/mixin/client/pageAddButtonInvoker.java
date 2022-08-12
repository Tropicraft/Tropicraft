package net.tropicraft.core.mixin.client;

import net.minecraft.client.gui.components.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Invoker;
import vazkii.patchouli.client.book.BookPage;

@Mixin(BookPage.class)
public interface pageAddButtonInvoker {

    @Invoker(value = "addButton", remap = false)
    void invokeAddButton(Button button);

}
