package extendedrenderer.particle.entity;

import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class EntityIconWindFX extends EntityIconFX/* implements WindHandler*/ {

	public EntityIconWindFX(World par1World, double par2, double par4,
			double par6, double par8, double par10, double par12,
			IIcon par14Item) {
		super(par1World, par2, par4, par6, par8, par10, par12, par14Item);
	}

	/*@Override
	public float getWindWeight() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public int getParticleDecayExtra() {
		// TODO Auto-generated method stub
		return 0;
	}*/

}
