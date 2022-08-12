package net.tropicraft.core.mixin.plugins;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TropicraftMixinPlugin implements IMixinConfigPlugin {

    private Boolean isPatchouliLoaded = null;

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String mixinClassNameOnly = getMixinClassName(mixinClassName);

        if(Objects.equals(mixinClassNameOnly, "pageAddButtonInvoker")){
            //Slightly Cursed way of getting the Modinfo as mixin plugins can't use ModList due to the loading phase
            if(isPatchouliLoaded == null){
                isPatchouliLoaded = LoadingModList.get().getMods().stream().anyMatch(modInfo -> Objects.equals(modInfo.getModId(), "patchouli"));
            }

            return isPatchouliLoaded;
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    private static String getMixinClassName(String mixinClassTarget){
        String[] stringPathParts = mixinClassTarget.split("\\.");

        return stringPathParts[stringPathParts.length - 1];
    }
}
