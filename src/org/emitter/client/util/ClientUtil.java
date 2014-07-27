package org.emitter.client.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.emitter.types.AuthReq;
import org.emitter.types.LoginReq;
import org.emitter.types.LoginResp;
import org.emitter.types.Source;
import org.emitter.utils.JsonUtil;


public class ClientUtil 
{
	private Persitance persist;
	private final static String EMITTER_SOURCE_FILE = "source";
	private final String programKey;
	
	public interface Persitance
	{
		public void saveObject(String obj, String name);
		public String getObject(String name);
	}
	
	public ClientUtil(String programKey, Persitance persist) {
		this.programKey = programKey;
		this.persist = persist;
	}
	private <E> E stringToObject(Class<E> c, String key)
	{
		E ret = null;
		String obj = persist.getObject(key);
		if(null != obj)
		{
			StringReader reader = new StringReader(obj);
			ret = JsonUtil.<E>from(reader);
		}
		return ret;
	}
	private <E> void objectToString(E obj, String key)
	{
		StringWriter writer = new StringWriter();
		JsonUtil.<E>to(obj, writer);
		persist.saveObject(writer.toString(), key);
	}
	
	public Source getSource()
	{
		Source src = null;
		try
		{
			src = stringToObject(Source.class, EMITTER_SOURCE_FILE);
		}
		catch(Exception e)
		{
			
		}
		if(src == null)
		{
			src = new Source();
			src.setProgramKey(programKey);
		}
		return src;
	}
	
	public <REQ, RESP> RESP post(String urlString, REQ req, Class<REQ> reqc, Class<RESP> respc) throws IOException
	{
		URL url;
		try {
			url = new URL("http://beta-app.emitter.co:8080/EmitterServer/Emitter/" + urlString);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage());
		}
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestProperty("Content-Type", "application/json");
		httpCon.setRequestProperty("Accept", "application/json");
		httpCon.setRequestMethod("POST");
		OutputStreamWriter out = new OutputStreamWriter(
		    httpCon.getOutputStream());
		String json = JsonUtil.to(req, reqc);
		out.write(json);
		out.close();
		InputStream resp;
		try
		{
			resp = httpCon.getInputStream();
		} catch (IOException ex)
		{
			ex.printStackTrace();
			resp = httpCon.getErrorStream();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(resp));
		try
		{
			RESP obj = JsonUtil.<RESP>from(reader);
			
			return obj;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	public void saveSource(Source src) throws FileNotFoundException
	{
		objectToString(src, EMITTER_SOURCE_FILE);
	}
	
	public LoginResp login( Source src, String deviceName) throws FileNotFoundException
	{
		AuthReq ar = stringToObject(AuthReq.class, "cred");
		LoginReq req = new LoginReq();
		req.setDeviceName(deviceName);
		req.setAuth(ar);
		req.setSource(src);
		LoginResp resp = null;
		try {
			resp = post("login", req, LoginReq.class, LoginResp.class);
		} catch (IOException e) {
			
		}
		if(resp != null && resp.getErrorMsg() == null)
		{
			saveSource(resp.getSource());
			if(resp.getToken() == null)
			{
				//something's fishy
			
			}
		}
		return resp;
	}
}
