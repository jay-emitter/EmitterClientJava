package org.emitter.client.util;

import org.emitter.error.EmitterException;
import org.emitter.utils.JsonUtil;

public class PersistenceUtil 
{
	private final Persistence per;
	
	public PersistenceUtil(Persistence _per) 
	{
		per = _per;
	}
	
	public <T> void saveObject(T ob, String key) throws EmitterException
	{
		per.saveData(JsonUtil.to(ob), key);
	}
	
	public <T> T getObject(String key) throws EmitterException
	{
		return JsonUtil.<T>from(per.getData(key));
	}
	
	public Persistence getPersistence()
	{
		return per;
	}
	
}
