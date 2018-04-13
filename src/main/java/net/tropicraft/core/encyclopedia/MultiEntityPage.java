package net.tropicraft.core.encyclopedia;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@ParametersAreNonnullByDefault
public abstract class MultiEntityPage extends EntityPage {
    
    private final MultiItemPage delegate;
    
    @Nullable
    private EntityLivingBase[] variants;
    private int currentIndex;

    public MultiEntityPage(String id, ItemStack... icons) {
        super(id, icons[0]);
        this.delegate = new MultiItemPage(id, icons);
    }

    public MultiEntityPage(String id, ResourceLocation entityId, ItemStack... icons) {
        super(id, entityId, icons[0]);
        this.delegate = new MultiItemPage(id, icons);
    }

    protected abstract EntityLivingBase[] makeVariants();
    
    @Override
    public void drawIcon(int x, int y, float cycle) {
        delegate.drawIcon(x, y, cycle);
    }
    
    @Override
    @Nullable
    protected EntityLivingBase getEntity() {
        return variants[currentIndex];
    }
    
    @Override
    public void drawHeader(int x, int y, float mouseX, float mouseY, float cycle) {
        if (variants == null) {
            variants = makeVariants();
        }
        float mid = (variants.length - 1) / 2f;
        for (int i = 0; i < variants.length; i++) {
            currentIndex = i;
            super.drawHeader((int) (x + ((i - mid) * variants[i].width * 40)), y, mouseX, mouseY, cycle);
        }
    }
}
