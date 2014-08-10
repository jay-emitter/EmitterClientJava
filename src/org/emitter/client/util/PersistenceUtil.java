package org.emitter.client.util;

import org.emitter.error.EmitterException;
import org.emitter.utils.JsonUtil;

/**
 * @author jeremy
 *
 */
public class PersistenceUtil 
{
	private final Persistence per;
	
	/**
	 * @param _per
	 */
	public PersistenceUtil(Persistence _per) 
	{
		per = _per;
	}
	
	/**
	 * @param ob Object to be saved
	 * @param key Key to the Object
	 * @throws EmitterException
	 */
	public <T> void saveObject(T ob, String key) throws EmitterException
	{
		per.saveData(JsonUtil.to(ob), key);
	}
	
	/**
	 * @param key Key to the object
	 * @param cl Type of Object to retrieve
	 * @return The Object or NULL
	 * @throws EmitterException if serialization fails
	 */
	public <T> T getObject(String key, Class<T> cl) throws EmitterException
	{
		return JsonUtil.<T>from(per.getData(key), cl);
	}
	
	/**
	 * @return
	 */
	public Persistence getPersistence()
	{
		return per;
	}
	
}
