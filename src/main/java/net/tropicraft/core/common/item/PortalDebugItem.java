package net.tropicraft.core.common.item;

import net.minecraft.ChatFormatting;
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
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.tropicraft.core.common.config.TropicraftConfig;
import net.tropicraft.core.common.dimension.PortalTropics;

import javax.annotation.Nullable;
import java.util.List;

public class PortalDebugItem extends Item {

    private PortalEnchanterMode enchanterMode = PortalEnchanterMode.PORTAL_CREATION;

    public enum PortalEnchanterMode {
        PORTAL_CREATION(0, "tropicraft.enchanterMode.creation"),
        PORTAL_SEARCH(1, "tropicraft.enchanterMode.search"),
        PORTAL_DESTRUCTION(2, "tropicraft.enchanterMode.destroy");

        private String translationKey;
        private int id;

        PortalEnchanterMode(int id, String string){
            this.translationKey = string;
            this.id = id;
        }

        public TranslatableComponent getTranslatableComponent(){
            return new TranslatableComponent(this.translationKey);
        }

        public static PortalEnchanterMode getEnchanterMode(int i){
            return PortalEnchanterMode.values()[i];
        }

        public int getId(){
            return this.id;
        }
    }

    public PortalDebugItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(pStack.getItem() instanceof PortalDebugItem portalEnchanterItem){
            ChatFormatting chatFormatting = ChatFormatting.AQUA;

            if(portalEnchanterItem.getEnchanterMode() == PortalEnchanterMode.PORTAL_SEARCH)
                chatFormatting = ChatFormatting.GREEN;
            else if(portalEnchanterItem.getEnchanterMode() == PortalEnchanterMode.PORTAL_DESTRUCTION)
                chatFormatting = ChatFormatting.RED;

            pTooltipComponents.add(portalEnchanterItem.getEnchanterMode().getTranslatableComponent().withStyle(chatFormatting));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(!pContext.getLevel().isClientSide && pContext.getPlayer() instanceof ServerPlayer serverPlayer) {
            final boolean hasPermission = serverPlayer.canUseGameMasterBlocks() ||
                    serverPlayer.getServer().isSingleplayerOwner(serverPlayer.getGameProfile()) ||
                    TropicraftConfig.portalEnchanterWhitelist.get().contains(serverPlayer.getUUID().toString());

            if (hasPermission){
                if (((PortalDebugItem) pContext.getItemInHand().getItem()).getEnchanterMode() == PortalEnchanterMode.PORTAL_CREATION) {
                    if (!isPortalTooClose((ServerLevel) pContext.getLevel(), pContext.getClickedPos())) {
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
                    } else {
                        serverPlayer.sendMessage(new TranslatableComponent("tropicraft.portalEnchanterProximityWarning"), ChatType.GAME_INFO, serverPlayer.getUUID());
                        return InteractionResult.FAIL;
                    }
                }
            }
            else{
                serverPlayer.sendMessage(new TranslatableComponent("tropicraft.portalEnchanterWarning"), ChatType.GAME_INFO, serverPlayer.getUUID());
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pPlayer.isShiftKeyDown() && pPlayer instanceof ServerPlayer serverPlayer){
            PortalDebugItem enchanter = (PortalDebugItem)pPlayer.getItemInHand(pUsedHand).getItem();

            int mode = enchanter.getEnchanterMode().getId();

            if(mode < 2){
                mode++;
            }
            else{
                mode = 0;
            }

            this.setEnchanterMode(mode);

            ChatFormatting chatFormatting = ChatFormatting.AQUA;

            if(PortalEnchanterMode.getEnchanterMode(mode) == PortalEnchanterMode.PORTAL_SEARCH)
                chatFormatting = ChatFormatting.GREEN;
            else if(PortalEnchanterMode.getEnchanterMode(mode) == PortalEnchanterMode.PORTAL_DESTRUCTION)
                chatFormatting = ChatFormatting.RED;

            serverPlayer.sendMessage(PortalEnchanterMode.getEnchanterMode(mode).getTranslatableComponent().withStyle(chatFormatting), ChatType.GAME_INFO, serverPlayer.getUUID());

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