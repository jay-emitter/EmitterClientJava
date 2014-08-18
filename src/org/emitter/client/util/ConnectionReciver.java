package org.emitter.client.util;

import org.emitter.error.EmitterException;
import org.emitter.utils.JsonUtil;

public class ConnectionReciver
{
	Connection con;
	Class<?> type;
	public ConnectionReciver(Connection _con, Class<?> _type) {
		con = _con;
		type = _type;
	}
	
	private <T> Object recv(Class<T> type) throws EmitterException
	{
		return con.<T>getResponseObject(type);
	}
	
	/**
	 * 
	 * @return
	 * @throws EmitterException
	 */
	public Object recieve() throws EmitterException
	{
		return recv(type);
	}
	
	public String recieveString() throws EmitterException
	{
		Object ob = this.recieve();
		return JsonUtil.to(type.cast(ob));
	}
	
	
}
