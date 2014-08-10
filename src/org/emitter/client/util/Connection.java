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

/**
 * @author jeremy
 *
 */
public final class Connection
{
	private HttpURLConnection	httpCon;
	private URL					url;

	/**
	 * @param _url
	 */
	public Connection(URL _url)
	{
		url = _url;
	}

	/**
	 * @return the object passed back from the server as a response
	 * @throws EmitterException
	 */
	public <RESP> RESP getResponseObject(Class<RESP> cl) throws EmitterException
	{
		InputStream resp;
		try
		{
			resp = httpCon.getInputStream();
		}
		catch (IOException ex)
		{
			throw new EmitterException("Connection Failed", ex);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(resp));
		RESP obj = JsonUtil.<RESP> from(reader, cl);
		return obj;
	}

	/**
	 * @param obj
	 * @throws EmitterException
	 */
	public <REQ> void post(REQ obj) throws EmitterException
	{
		if (null == httpCon)
		{
			setupConnection("POST");
		}
		try
		{
			OutputStreamWriter out = new OutputStreamWriter(
					httpCon.getOutputStream());
			JsonUtil.<REQ> to(obj, out);
		}
		catch (IOException ex)
		{
			throw new EmitterException("Could not post data to server", ex);
		}
	}

	/**
	 * @param json
	 * @throws EmitterException
	 */
	public void postJson(String json) throws EmitterException
	{
		if (null == httpCon)
		{
			setupConnection("POST");
		}
		try
		{
			OutputStreamWriter out = new OutputStreamWriter(
					httpCon.getOutputStream());
			out.write(json);
		}
		catch (IOException ex)
		{
			throw new EmitterException("Could not post data to server", ex);
		}
	}

	private void setupConnection(String method) throws EmitterException
	{
		try
		{
			httpCon = (HttpURLConnection) url.openConnection();
		}
		catch (Exception ex)
		{
			throw new EmitterException("Could not open connection", ex);
		}
		httpCon.setDoOutput(true);
		httpCon.setRequestProperty("Content-Type", "application/json");
		httpCon.setRequestProperty("Accept", "application/json");
		try
		{
			httpCon.setRequestMethod("POST");
		}
		catch (ProtocolException e)
		{
			throw new EmitterException("Could not set request method to: "
					+ method, e);
		}
	}
}