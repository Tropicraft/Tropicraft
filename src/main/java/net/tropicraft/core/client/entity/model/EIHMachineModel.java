package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.tropicraft.core.common.block.tileentity.IMachineBlock;

public class EIHMachineModel<T extends BlockEntity & IMachineBlock> extends MachineModel<T> {
    private final ModelPart base;
    private final ModelPart back;
    private final ModelPart nose;
    private final ModelPart forehead;
    private final ModelPart leftEye;
    private final ModelPart rightEye;
    private final ModelPart basinNearBack;
    private final ModelPart basinSide1;
    private final ModelPart basinSide2;
    private final ModelPart basinNearFront;
    private final ModelPart basinCorner1;
    private final ModelPart basinCorner2;
    private final ModelPart basinCorner3;
    private final ModelPart basinCorner4;
    private final ModelPart lidBase;
    private final ModelPart lidTop;
    private final ModelPart mouth;

    public EIHMachineModel(ModelPart root) {
        super();

        this.base = root.getChild("base");
        this.back = root.getChild("back");
        this.nose = root.getChild("nose");
        this.forehead = root.getChild("forehead");
        this.leftEye = root.getChild("leftEye");
        this.rightEye = root.getChild("rightEye");
        this.basinNearBack = root.getChild("basinNearBack");
        this.basinSide1 = root.getChild("basinSide1");
        this.basinSide2 = root.getChild("basinSide2");
        this.basinNearFront = root.getChild("basinNearFront");
        this.basinCorner1 = root.getChild("basinCorner1");
        this.basinCorner2 = root.getChild("basinCorner2");
        this.basinCorner3 = root.getChild("basinCorner3");
        this.basinCorner4 = root.getChild("basinCorner4");
        this.lidBase = root.getChild("lidBase");
        this.lidTop = root.getChild("lidTop");
        this.mouth = root.getChild("mouth");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("base",
                CubeListBuilder.create()
                        .texOffs(0, 44)
                        .mirror()
                        .addBox(-8F, -1F, -8F, 16, 3, 16),
                PartPose.offset(0F, 22F, 0F));

        root.addOrReplaceChild("back",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-3F, -15F, -8F, 6, 25, 16),
                PartPose.offset(5F, 11F, 0F));

        root.addOrReplaceChild("nose",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-1F, -7F, -2F, 2, 14, 4),
                PartPose.offset(1F, 8F, 0F));

        root.addOrReplaceChild("forehead",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-1F, -1F, -8F, 3, 5, 16),
                PartPose.offset(0F, -3F, 0F));

        root.addOrReplaceChild("leftEye",
                CubeListBuilder.create()
                        .texOffs(1, 35)
                        .mirror()
                        .addBox(0F, -1F, -3F, 1, 4, 6),
                PartPose.offset(1F, 2F, 5F));

        root.addOrReplaceChild("rightEye",
                CubeListBuilder.create()
                        .texOffs(1, 35)
                        .mirror()
                        .addBox(0F, -1F, -3F, 1, 4, 6),
                PartPose.offset(1F, 2F, -5F));

        root.addOrReplaceChild("basinNearBack",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-1F, 0F, -4F, 1, 1, 8),
                PartPose.offset(2F, 20F, 0F));

        root.addOrReplaceChild("basinSide1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-5F, 0F, -2F, 10, 1, 4),
                PartPose.offset(-3F, 20F, 6F));

        root.addOrReplaceChild("basinSide2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-5F, 0F, -2F, 10, 1, 4),
                PartPose.offset(-3F, 20F, -6F));

        root.addOrReplaceChild("basinNearFront",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-1F, 0F, -4F, 2, 1, 8),
                PartPose.offset(-7F, 20F, 0F));

        root.addOrReplaceChild("basinCorner1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0F, 0F, 0F, 1, 1, 1),
                PartPose.offset(0F, 20F, 3F));

        root.addOrReplaceChild("basinCorner2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0F, 0F, 0F, 1, 1, 1),
                PartPose.offset(0F, 20F, -4F));

        root.addOrReplaceChild("basinCorner3",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0F, 0F, 0F, 1, 1, 1),
                PartPose.offset(-6F, 20F, 3F));

        root.addOrReplaceChild("basinCorner4",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(0F, 0F, 0F, 1, 1, 1),
                PartPose.offset(-6F, 20F, -4F));

        root.addOrReplaceChild("lidBase",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-4F, 0F, -8F, 9, 1, 16),
                PartPose.offset(3F, -5F, 0F));

        root.addOrReplaceChild("lidTop",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .mirror()
                        .addBox(-2F, 0F, -2F, 4, 1, 4),
                PartPose.offset(3F, -6F, 0F));

        root.addOrReplaceChild("mouth",
                CubeListBuilder.create()
                        .texOffs(54, 0)
                        .mirror()
                        .addBox(0F, -1F, -2F, 1, 3, 4),
                PartPose.offset(1F, 16F, 0F));


        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(
                base, back, nose, forehead, leftEye, rightEye, basinNearBack,
                basinSide1, basinSide2, basinNearFront, basinCorner1, basinCorner2,
                basinCorner3, basinCorner4, lidBase, lidTop, mouth
        );
    }

}
