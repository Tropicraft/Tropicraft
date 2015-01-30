package net.tropicraft.item;

import java.util.List;

import javax.swing.Icon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityHook;
import net.tropicraft.fishing.FishingEvent;
import net.tropicraft.fishing.FishingEventReelComplete;
import net.tropicraft.fishing.FishingManager;
import net.tropicraft.fishing.LinkedRods;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRod extends Item
{
    @SideOnly(Side.CLIENT)
    private Icon theIcon;

    public static int TYPE_OLD = 0, TYPE_GOOD = 1, TYPE_SUPER = 2;
    public int rodType;
    public LinkedRods rodLink;

    public ItemRod() {
        super();
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
        this.setCreativeTab(TCCreativeTabRegistry.tabTools);
    }

    public ItemRod setType(int i) {
        rodType = i;
        return this;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    /**
     * Returns true if this item should be rotated by 180 degrees around the Y axis when being held in an entities
     * hands.
     */
    @SideOnly(Side.CLIENT)
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if(rodLink == null && !par2World.isRemote) {
            rodLink = new LinkedRods(); 
        }

        //par3EntityPlayer.swingItem();

        if(!par2World.isRemote) {
            if(!rodLink.playerHasFloat(par3EntityPlayer)) {
                List<FishingEvent> evts = FishingManager.getEventsForPlayer(par3EntityPlayer);
                if(!evts.isEmpty()){
                    for(FishingEvent evt : evts) {
                        if(evt instanceof FishingEventReelComplete) {
                            if(evt.currentTick < 20)
                                return par1ItemStack;
                        }
                    }
                }
                par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                EntityHook hook = new EntityHook(par2World, par3EntityPlayer).setRodType(rodType);
                rodLink.createLink(hook, par3EntityPlayer);
                par2World.spawnEntityInWorld(hook);

            }else{
                EntityHook hook = rodLink.getLinkedHook(par3EntityPlayer);

                if(hook != null){
                    if(hook.isDead){
                        rodLink.destroyLink(rodLink.getLinkedHook(par3EntityPlayer), par3EntityPlayer);
                    };
                    if(!hook.hasLanded){
                        hook.hasLanded = true;
                        return par1ItemStack;
                    }
                    if(hook.getDistanceToEntity(par3EntityPlayer) > 3D){
                        //System.out.println(hook.getWi);
                        float len = 0F;
                        if(this.rodType == this.TYPE_OLD){
                            len = 0.5f;
                        }
                        if(this.rodType == this.TYPE_GOOD){
                            len = 2f;
                        }
                        if(this.rodType == this.TYPE_SUPER){
                            len = 5f;
                        }
                        if(par3EntityPlayer.isSneaking()){
                            len = len/4;
                        }
                        if(hook.getWireLength()-len >= 0)
                            hook.setWireLength(hook.getWireLength()-len);
                    }else{
                        if(hook.bobber != null){
                            hook.bobber.setPosition(hook.posX, hook.posY, hook.posZ);
                        }
                        FishingManager.initEvent(new FishingEventReelComplete(hook, par3EntityPlayer));
                        hook.setDead();
                        return par1ItemStack;
                    }


                    //System.out.println(hook.posX +" "+hook.angler.posX+" | "+hook.posZ+" "+hook.angler.posZ);

                }
            }
        }
        return par1ItemStack;
    }



    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        //super.registerIcons(par1IconRegister);
        this.theIcon = par1IconRegister.registerIcon(ModInfo.MODID + ":fishingRod");
        this.oldIcon = par1IconRegister.registerIcon(ModInfo.MODID + ":fishingRodOld");
        this.superIcon = par1IconRegister.registerIcon(ModInfo.MODID + ":fishingRodSuper");
        this.goodIcon = par1IconRegister.registerIcon(ModInfo.MODID + ":fishingRodGood");

    }

    public Icon oldIcon, goodIcon, superIcon;


    public Icon getType(int i){
        switch(i){
        case 0:
            return oldIcon;
        case 1:
            return goodIcon;
        case 2:
            return superIcon;
        default:
            return theIcon;
        }
    }


    // in slot
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1)
    {
        return getType(rodType);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamageForRenderPass(int par1, int par2)
    {
        // TODO Auto-generated method stub
        //  return Item.appleGold.getIconFromDamageForRenderPass(par1, par2);

        return theIcon;
    }

    public boolean playerHasLureOutHybrid(EntityPlayer p){
        World w = p.worldObj;
        List<Entity> ents = w.loadedEntityList;
        for(Entity e : ents){
            if(e instanceof EntityHook){
                EntityHook hook = (EntityHook)e;
                if(hook.angler != null)
                    if(hook.angler.entityId == p.entityId){
                        return true;
                    }
            }
        }

        return false;
    }

    //in hand
    @Override
    public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player,
            ItemStack usingItem, int useRemaining)
    {
        if(this.playerHasLureOutHybrid(player)){
            //System.out.println("Does have one!");
            return theIcon;
        }
        return this.getType(rodType);
        // TODO Auto-generated method stub

    }

    @Override
    public Icon getIcon(ItemStack stack, int pass)
    {
        // TODO Auto-generated method stub
        return theIcon;
    }
}