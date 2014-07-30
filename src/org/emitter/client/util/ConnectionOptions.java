package org.emitter.client.util;

public class ConnectionOptions 
{
	public enum Tier
	{
		DEV,
		BETA,
		PROD;
	}
	private Tier tier;
	private String developerName;
	
	/**
	 * @return the tier
	 */
	public Tier getTier() {
		return tier;
	}
	/**
	 * @param tier the tier to set
	 */
	public void setTier(Tier tier) {
		this.tier = tier;
	}
	/**
	 * @return the developerName
	 */
	public String getDeveloperName() {
		return developerName;
	}
	/**
	 * @param developerName the developerName to set
	 */
	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}
	
}
