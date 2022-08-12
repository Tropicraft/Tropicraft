package net.tropicraft.core.common.compat.patchouli;

import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.client.book.template.BookTemplate;

public class ComponenetRegistry {

    public static void init(){
        //BookTemplate.registerComponent(new ResourceLocation("patchouli", "multi_image"), ComponentMultiImage.class);
        BookTemplate.registerComponent(new ResourceLocation("patchouli", "multi_image_debug"), ComponentMultiImage.class);
    }

}
