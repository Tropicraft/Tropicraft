package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.common.block.tileentity.IMachineTile;

import java.util.function.Function;

public class EIHMachineModel<T extends TileEntity & IMachineTile> extends MachineModel<T> {
    final ModelRenderer base;
    final ModelRenderer back;
    final ModelRenderer nose;
    final ModelRenderer forehead;
    final ModelRenderer leftEye;
    final ModelRenderer rightEye;
    final ModelRenderer basinNearBack;
    final ModelRenderer basinSide;
    final ModelRenderer basinSide2;
    final ModelRenderer basinNearFront;
    final ModelRenderer basinCorner1;
    final ModelRenderer basinCorner2;
    final ModelRenderer basinCorner3;
    final ModelRenderer basinCorner4;
    final ModelRenderer lidBase;
    final ModelRenderer lidTop;
    final ModelRenderer mouth;

    public EIHMachineModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        textureWidth = 64;
        textureHeight = 64;

        base = new ModelRenderer(this, 0, 44);
        base.addBox(-8F, -1F, -8F, 16, 3, 16);
        base.setRotationPoint(0F, 22F, 0F);
        base.mirror = true;
        base.setTextureSize(64, 64);
        back = new ModelRenderer(this, 0, 0);
        back.addBox(-3F, -15F, -8F, 6, 25, 16);
        back.setRotationPoint(5F, 11F, 0F);
        back.mirror = true;
        back.setTextureSize(64, 64);
        nose = new ModelRenderer(this, 0, 0);
        nose.addBox(-1F, -7F, -2F, 2, 14, 4);
        nose.setRotationPoint(1F, 8F, 0F);
        nose.mirror = true;
        nose.setTextureSize(64, 64);
        forehead = new ModelRenderer(this, 0, 0);
        forehead.addBox(-1F, -1F, -8F, 3, 5, 16);
        forehead.setRotationPoint(0F, -3F, 0F);
        forehead.mirror = true;
        forehead.setTextureSize(64, 64);
        leftEye = new ModelRenderer(this, 1, 35);
        leftEye.addBox(0F, -1F, -3F, 1, 4, 6);
        leftEye.setRotationPoint(1F, 2F, 5F);
        leftEye.mirror = true;
        leftEye.setTextureSize(64, 64);
        rightEye = new ModelRenderer(this, 1, 35);
        rightEye.addBox(0F, -1F, -3F, 1, 4, 6);
        rightEye.setRotationPoint(1F, 2F, -5F);
        rightEye.mirror = true;
        rightEye.setTextureSize(64, 64);
        basinNearBack = new ModelRenderer(this, 0, 0);
        basinNearBack.addBox(-1F, 0F, -4F, 1, 1, 8);
        basinNearBack.setRotationPoint(2F, 20F, 0F);
        basinNearBack.mirror = true;
        basinNearBack.setTextureSize(64, 64);
        basinSide = new ModelRenderer(this, 0, 0);
        basinSide.addBox(-5F, 0F, -2F, 10, 1, 4);
        basinSide.setRotationPoint(-3F, 20F, 6F);
        basinSide.mirror = true;
        basinSide.setTextureSize(64, 64);
        basinSide2 = new ModelRenderer(this, 0, 0);
        basinSide2.addBox(-5F, 0F, -2F, 10, 1, 4);
        basinSide2.setRotationPoint(-3F, 20F, -6F);
        basinSide2.mirror = true;
        basinSide2.setTextureSize(64, 64);
        basinNearFront = new ModelRenderer(this, 0, 0);
        basinNearFront.addBox(-1F, 0F, -4F, 2, 1, 8);
        basinNearFront.setRotationPoint(-7F, 20F, 0F);
        basinNearFront.mirror = true;
        basinNearFront.setTextureSize(64, 64);
        basinCorner1 = new ModelRenderer(this, 0, 0);
        basinCorner1.addBox(0F, 0F, 0F, 1, 1, 1);
        basinCorner1.setRotationPoint(0F, 20F, 3F);
        basinCorner1.mirror = true;
        basinCorner1.setTextureSize(64, 64);
        basinCorner2 = new ModelRenderer(this, 0, 0);
        basinCorner2.addBox(0F, 0F, 0F, 1, 1, 1);
        basinCorner2.setRotationPoint(0F, 20F, -4F);
        basinCorner2.mirror = true;
        basinCorner2.setTextureSize(64, 64);
        basinCorner3 = new ModelRenderer(this, 0, 0);
        basinCorner3.addBox(0F, 0F, 0F, 1, 1, 1);
        basinCorner3.setRotationPoint(-6F, 20F, 3F);
        basinCorner3.mirror = true;
        basinCorner3.setTextureSize(64, 64);
        basinCorner4 = new ModelRenderer(this, 0, 0);
        basinCorner4.addBox(0F, 0F, 0F, 1, 1, 1);
        basinCorner4.setRotationPoint(-6F, 20F, -4F);
        basinCorner4.mirror = true;
        basinCorner4.setTextureSize(64, 64);
        lidBase = new ModelRenderer(this, 0, 0);
        lidBase.addBox(-4F, 0F, -8F, 9, 1, 16);
        lidBase.setRotationPoint(3F, -5F, 0F);
        lidBase.mirror = true;
        lidBase.setTextureSize(64, 64);
        lidTop = new ModelRenderer(this, 0, 0);
        lidTop.addBox(-2F, 0F, -2F, 4, 1, 4);
        lidTop.setRotationPoint(3F, -6F, 0F);
        lidTop.mirror = true;
        lidTop.setTextureSize(64, 64);
        mouth = new ModelRenderer(this, 54, 0);
        mouth.addBox(0F, -1F, -2F, 1, 3, 4);
        mouth.setRotationPoint(1F, 16F, 0F);
        mouth.mirror = true;
        mouth.setTextureSize(64, 64);
    }

    @Override
    public float getScale(T te) {
        return 0.0625F;
    }

    @Override
    public String getTexture(T te) {
        return "drink_mixer";
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(
            base, back, nose, forehead, leftEye, rightEye, basinNearBack,
            basinSide, basinSide2, basinNearFront, basinCorner1, basinCorner2,
            basinCorner3, basinCorner4, lidBase, lidTop, mouth
        );
    }

}
