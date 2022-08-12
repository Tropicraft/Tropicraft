package net.tropicraft.core.common.compat.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.mixin.client.pageAddButtonInvoker;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.client.book.gui.button.GuiButtonBookArrowSmall;
import vazkii.patchouli.client.book.template.TemplateComponent;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class ComponentMultiImage extends TemplateComponent {
    public IVariable[] images;
    @SerializedName("u") public IVariable u;
    @SerializedName("v") public IVariable v;
    @SerializedName("width") public IVariable width;
    @SerializedName("height") public IVariable height;

    @SerializedName("texture_width") public IVariable textureWidth;
    @SerializedName("texture_height") public IVariable textureHeight;

    @SerializedName("scale") public IVariable scale;

    transient public int imageU;
    transient public int imageV;
    transient public int w;
    transient public int h;

    transient public int tWidth = 256;
    transient public int tHeight = 256;

    transient public float imageScale = 1.0F;

    transient ArrayList<ResourceLocation> resources = new ArrayList<>();
    transient int index;

    public ComponentMultiImage() {
    }

    public void build(BookContentsBuilder builder, BookPage page, BookEntry entry, int pageNum) {
        super.build(builder, page, entry, pageNum);

        try {
            imageU = u.asNumber().intValue();
        } catch (NumberFormatException ignored) {}

        try {
            imageV = v.asNumber().intValue();
        } catch (NumberFormatException ignored) {}

        try {
            w = width.asNumber().intValue();
        } catch (NumberFormatException ignored) {}

        try {
            h = height.asNumber().intValue();
        } catch (NumberFormatException ignored) {}

        try {
            tWidth = textureWidth.asNumber().intValue();
        } catch (NumberFormatException ignored) {}

        try {
            tHeight = textureHeight.asNumber().intValue();
        } catch (NumberFormatException ignored) {}

        try {
            imageScale = scale.asNumber().floatValue();
        } catch (NumberFormatException ignored) {}

        for(IVariable image : this.images){
            resources.add(new ResourceLocation(image.asString()));
        }
    }

    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        super.onVariablesAvailable(lookup);
        for(int i = 0; i < this.images.length; ++i) {
            this.images[i] = (IVariable)lookup.apply(this.images[i]);
        }
        this.u = (IVariable)lookup.apply(this.u);
        this.v = (IVariable)lookup.apply(this.u);
        this.width = (IVariable)lookup.apply(this.width);
        this.height = (IVariable)lookup.apply(this.height);
        this.textureWidth = (IVariable)lookup.apply(this.width);
        this.textureHeight = (IVariable)lookup.apply(this.height);
        this.scale = (IVariable)lookup.apply(this.scale);
    }

    public void render(PoseStack ms, BookPage page, int mouseX, int mouseY, float pticks) {
        if (this.imageScale != 0.0F) {
            RenderSystem.setShaderTexture(0, this.resources.get(this.index));
            ms.pushPose();
            ms.translate((double)this.x, (double)this.y, 0.0);
            ms.scale(this.imageScale, this.imageScale, this.imageScale);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            GuiComponent.blit(ms, 0, 0, (float)this.imageU, (float)this.imageV, this.w, this.h, this.tWidth, this.tHeight);
            ms.popPose();
        }
    }

    public void onDisplayed(BookPage page, GuiBookEntry parent, int left, int top) {
        super.onDisplayed(page, parent, left, top);
        int x = 90;
        int y = 90;

        ((pageAddButtonInvoker)page).invokeAddButton(new GuiButtonBookArrowSmall(parent, x, y, true, () -> this.index > 0, this::handleButtonArrow));
        ((pageAddButtonInvoker)page).invokeAddButton(new GuiButtonBookArrowSmall(parent, x + 10, y, false, () -> this.index < this.resources.size() - 1, this::handleButtonArrow));
    }

    public void handleButtonArrow(Button button) {
        boolean left = ((GuiButtonBookArrowSmall)button).left;
        if (left) {
            --this.index;
        } else {
            ++this.index;
        }

    }

}
