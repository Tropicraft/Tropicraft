package net.tropicraft.core.common.dimension.feature.jigsaw.piece;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.template.TemplateManager;

/*
 * Hack interface used by home trees to support using a different bounding box for generation than jigsaw allocation.
 * For the case of home tree branches, they need to be packed quite tightly, but the jigsaw system does not allow
 * self-intersection. So, we need to return an empty bounding box for the general bounds accessor, but use a larger
 * one when populating the piece into the world so that it gets called for all the appropriate chunks.
 */
public interface PieceWithGenerationBounds {
    MutableBoundingBox getGenerationBounds(TemplateManager templates, BlockPos pos, Rotation rotation);
}
