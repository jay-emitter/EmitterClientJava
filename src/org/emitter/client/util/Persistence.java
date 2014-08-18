package org.emitter.client.util;

import org.emitter.error.EmitterException;
import org.emitter.utils.JsonUtil;

/**
 * @author jeremy
 *
 */
public abstract class Persistence
{
	/**
	 * @param obj Object to save
	 * @param key They key to associate with the saved obj
	 * @throws EmitterException 
	 */
	public abstract void saveData(String obj, String key) throws EmitterException;
	
	/**
	 * 
	 * @param key the key to the object
	 * @return the String representing the object
	 * @throws EmitterException
	 */
	public abstract String getData(String key) throws EmitterException;
	
	/**
	 * 
	 * @return true if user is logged in
	 */
	public final boolean isLoggedIn()
	{
		boolean ret = false;
		try
		{
			String data = getData("isloggedin");
			if(data != null)
			{
				Boolean b = JsonUtil.from(data,Boolean.class);
				ret = b.booleanValue();
			}
		}
		catch (EmitterException e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 
	 * @param b
	 */
	public final void setIsLoggedIn(boolean b)
	{
		Boolean bool = new Boolean(b);
		try
		{
			String data = JsonUtil.to(bool);
			this.saveData(data, "isloggedin");
		}
		catch (EmitterException e)
		{
			
			e.printStackTrace();
		}
	}
	
}