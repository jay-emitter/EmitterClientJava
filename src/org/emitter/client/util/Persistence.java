package org.emitter.client.util;

import org.emitter.error.EmitterException;
import org.emitter.utils.JsonUtil;

public abstract class Persistence
{
	public abstract void saveData(String obj, String key) throws EmitterException;
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
			Boolean b = JsonUtil.from(data,Boolean.class);
			ret = b.booleanValue();
		}
		catch (EmitterException e)
		{
			
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
	
	/**
	 * 
	 * @return
	 */
	public final boolean isRegistered()
	{
		boolean ret = false;
		try
		{
			String data = getData("registered");
			Boolean b = JsonUtil.from(data, Boolean.class);;
			ret = b.booleanValue();
		}
		catch(EmitterException ex)
		{	
		}
		return ret;
	}
	
	/**
	 * 
	 * @param b
	 */
	public final void setIsRegistered(boolean b)
	{
		Boolean bool = new Boolean(b);
		try
		{
			String data = JsonUtil.to(bool);
			this.saveData(data, "registered");
		}
		catch (EmitterException e)
		{
			
			e.printStackTrace();
		}
	}
	
	
}