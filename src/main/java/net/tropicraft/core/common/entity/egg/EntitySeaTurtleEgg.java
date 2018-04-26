package net.tropicraft.core.common.entity.egg;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tropicraft.core.common.entity.underdasea.EntitySeaTurtle;

public class EntitySeaTurtleEgg extends EntityEgg {

	public BlockPos parentWaterLoc = null;
	public String parentTexRef = null;

	public EntitySeaTurtleEgg(World par1World) {
		super(par1World);
	}

	@Override
	public Entity onHatch() {
		EntitySeaTurtle babyturtle = new EntitySeaTurtle(world, 2);
		if (this.parentWaterLoc != null) {
			babyturtle.log("received parent's water entry point, ms saved \\o/");
		}
		babyturtle.targetWaterSite = this.parentWaterLoc;

		if (this.parentTexRef != null) {
			babyturtle.setTexture(this.parentTexRef);
		}
		babyturtle.isSeekingWater = true;
		babyturtle.isLandPathing = true;
		return babyturtle;
	}

	@Override
	public int getHatchTime() {
		return 760;
	}

	@Override
	public int getPreHatchMovement() {
		return 360;
	}

	@Override
	public String getEggTexture() {
		return "turtle/egg_text";
	}

	@Override
	public boolean shouldEggRenderFlat() {
		return false;
	}

}
