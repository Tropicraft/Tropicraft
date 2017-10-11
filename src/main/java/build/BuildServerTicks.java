package build;

import build.world.BuildManager;

public class BuildServerTicks
{
	
	public static BuildManager buildMan = new BuildManager();

    public static void onTickInGame()
    {
    	buildMan.updateTick();
    }
}
