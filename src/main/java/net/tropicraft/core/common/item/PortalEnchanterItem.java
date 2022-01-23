package net.tropicraft.core.common.item;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.tropicraft.core.common.config.TropicraftConfig;
import net.tropicraft.core.common.dimension.PortalTropics;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.tropicraft.core.common.item.PortalEnchanterItem.PortalEnchanterMode.PORTAL_CREATION;

public class PortalEnchanterItem extends Item {

    private PortalEnchanterMode enchanterMode = PORTAL_CREATION;

    public enum PortalEnchanterMode {
        PORTAL_CREATION(0, "Creation Mode"),
        PORTAL_SEARCH(1, "Search Mode"),
        PORTAL_DESTRUCTION(2, "Destruction Mode");

        private String textComponent;
        private int id;

        PortalEnchanterMode(int id, String string){
            this.textComponent = string;
            this.id = id;
        }

        public String getTextComponent(){
            return this.textComponent;
        }

        public static PortalEnchanterMode getEnchanterMode(int i){
            return PortalEnchanterMode.values()[i];
        }

        public int getId(){
            return this.id;
        }
    }

    public PortalEnchanterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        boolean hasDirectMode = pStack.getTag() != null && pStack.getTag().contains("DirectMode");

        int mode;
        if (hasDirectMode) {
            mode = pStack.getTag().getBoolean("DirectMode") ? 1 : 0;
        } else {
            mode = 0;
        }

        pTooltipComponents.add(new TranslatableComponent("portalenchanter.type_" + mode));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(!pContext.getLevel().isClientSide && pContext.getPlayer() instanceof ServerPlayer serverPlayer) {
            if(((PortalEnchanterItem)pContext.getItemInHand().getItem()).getEnchanterMode() == PORTAL_CREATION) {
                final boolean hasPermission = serverPlayer.canUseGameMasterBlocks() ||
                        serverPlayer.getServer().isSingleplayerOwner(serverPlayer.getGameProfile()) ||
                        TropicraftConfig.portalEnchanterWhitelist.get().contains(serverPlayer.getUUID().toString());

                if (hasPermission && !isPortalTooClose((ServerLevel) pContext.getLevel(), pContext.getClickedPos())) {
                    boolean flag = PortalTropics.placePortalStructure((ServerLevel) pContext.getLevel(), serverPlayer, pContext.getClickedPos());

                    if (!FMLEnvironment.production) {
                        if (flag) {
                            serverPlayer.displayClientMessage(new TextComponent("Debug: Portal was created!"), false);

                            return InteractionResult.SUCCESS;
                        } else {
                            serverPlayer.displayClientMessage(new TextComponent("Debug: Portal was not created"), false);

                            return InteractionResult.FAIL;
                        }
                    }
                }else{
                    serverPlayer.sendMessage(new TextComponent("A portal to the Tropics already exist too close to your position, either remove the portal or go farther from it."), ChatType.GAME_INFO, serverPlayer.getUUID());
                    return InteractionResult.FAIL;
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pPlayer.isShiftKeyDown() && pPlayer instanceof ServerPlayer serverPlayer){
            PortalEnchanterItem enchanter = (PortalEnchanterItem)pPlayer.getItemInHand(pUsedHand).getItem();

            int mode = enchanter.getEnchanterMode().getId();

            if(mode < 2){
                mode++;
            }
            else{
                mode = 0;
            }

            this.setEnchanterMode(mode);
            serverPlayer.sendMessage(new TextComponent(PortalEnchanterMode.getEnchanterMode(mode).getTextComponent()), ChatType.GAME_INFO, serverPlayer.getUUID());

            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
        }

        return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
    }

    public void setEnchanterMode(int i){
        if(i >= 0 && i <= 2){
            this.enchanterMode = PortalEnchanterMode.getEnchanterMode(i);
        }
    }

    public PortalEnchanterMode getEnchanterMode(){
        return this.enchanterMode;
    }

    private boolean isPortalTooClose(ServerLevel world, BlockPos blockPos){
        BlockPos portalPos = PortalTropics.searchForPortalPoi(world, blockPos);

        return portalPos != null;
    }
}
