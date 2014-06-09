package extendedrenderer.particle;

import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

public class ParticleRegistry {

	public static IIcon squareGrey;
	public static IIcon smoke;
	public static IIcon cloud;
	public static IIcon cloud256;
	public static IIcon cloud256dark;
	public static IIcon cloudDownfall;
	
	public static void init(TextureStitchEvent event) {
		if (event.map.getTextureType() == 1) {
			squareGrey = event.map.registerIcon("ExtendedRenderer:particles/white");
			smoke = event.map.registerIcon("ExtendedRenderer:particles/smoke_00");
			cloud = event.map.registerIcon("ExtendedRenderer:particles/cloud64");
			cloud256 = event.map.registerIcon("ExtendedRenderer:particles/cloud256");
			cloud256dark = event.map.registerIcon("ExtendedRenderer:particles/cloud256dark");
			cloudDownfall = event.map.registerIcon("ExtendedRenderer:particles/downfall");
			
		}
	}
}
