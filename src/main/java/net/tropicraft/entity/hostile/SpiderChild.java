package net.tropicraft.entity.hostile;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import net.tropicraft.entity.ai.jobs.JobExtraTargetting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SpiderChild extends SpiderBase {

	public int age = 0;
	public int ageMax = 20*60*10; //10 irl minutes to get to adult, at then i guess transform to adult entity?
	
	public SpiderChild(World par1World) {
		super(par1World);

		agent.jobMan.addJob(new JobExtraTargetting(agent.jobMan));
		
		//texture = "/mods/TropicraftMod/textures/entities/spiderchild.png";
		this.setSize(0.5F, 0.35F);
		//agent.setMoveSpeed(0.33F);
		//this.setSize(0.35F, 0.25F);
		this.experienceValue = 2;
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(5.0D);
	}
	
	@SideOnly(Side.CLIENT)

    /**
     * How large the spider should be scaled.
     */
    public float spiderScaleAmount()
    {
        return 0.5F;
    }

}
