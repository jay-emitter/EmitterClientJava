package org.emitter.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.emitter.error.EmitterException;
import org.emitter.utils.JsonUtil;

public final class Connection
{
	private HttpURLConnection httpCon;
	private URL url;
	public Connection(URL _url)
	{
		url = _url;
	}
	
	private void setupConnection(String method) throws EmitterException
	{
		try
		{
			httpCon = (HttpURLConnection) url.openConnection();
		}catch(Exception ex)
		{
			throw new EmitterException("Could not open connection");
		}
		httpCon.setDoOutput(true);
		httpCon.setRequestProperty("Content-Type", "application/json");
		httpCon.setRequestProperty("Accept", "application/json");
		try {
			httpCon.setRequestMethod("POST");
		} 
		catch (ProtocolException e) 
		{
			throw new EmitterException("Could not set request method to: " +  method);
		}
	}
	
	public void postJson(String json) throws EmitterException
	{
		if(null == httpCon)
			setupConnection("POST");
		try
		{
			OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
			out.write(json);
		}
		catch(IOException ex)
		{
			throw new EmitterException("Could not post data to server");
		}
	}
	
	public <REQ> void post(REQ obj) throws EmitterException
	{
		if(null == httpCon)
			setupConnection("POST");
		try
		{
			OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
			JsonUtil.<REQ>to(obj, out);
		}
		catch(IOException ex)
		{
			throw new EmitterException("Could not post data to server");
		}
	}
	
	public <RESP> RESP getResponseObject() throws EmitterException
	{
		InputStream resp;
		try
		{
			resp = httpCon.getInputStream();
		} catch (IOException ex)
		{
			throw new EmitterException("Connection Failed");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(resp));
		RESP obj = JsonUtil.<RESP>from(reader);
		return obj;
	}
}