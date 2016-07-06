package configuration;

//TODO: 1.9 temporary class placement, relocate to CoroUtil and make requirement for tropicraft, or go API route for configmod

public interface IConfigCategory {

	public String getConfigFileName();
	public String getCategory();
	public void hookUpdatedValues();
	
}
