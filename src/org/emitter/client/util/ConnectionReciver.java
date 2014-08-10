package org.emitter.client.util;

import org.emitter.error.EmitterException;

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
	
	public Object recieve() throws EmitterException
	{
		return recv(type);
	}
}
