package org.emitter.client.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.emitter.error.EmitterException;
import org.emitter.types.EmitReq;
import org.emitter.types.EmitResp;
import org.emitter.types.LoginReq;
import org.emitter.types.LoginResp;
import org.emitter.types.RegisterReq;
import org.emitter.types.RegisterResp;
import org.emitter.types.Source;


class ConnectUtil 
{
	private PersistenceUtil persist;
	private final static String EMITTER_SOURCE_KEY = "source";
	private final static String EMITTER_TOKEN_KEY = "token";
	private final String programKey;
	private final ConnectionOptions opt;
	public ConnectUtil(String programKey, PersistenceUtil  persist, ConnectionOptions _opt)
	{
		this.programKey = programKey;
		this.persist = persist;
		this.opt = _opt;
	}
	
	public Source getSource()
	{
		Source src = null;
		try
		{
			src = persist.getObject(EMITTER_SOURCE_KEY);
		}
		catch(EmitterException ex)
		{
			src = new Source();
			src.setAppKey(programKey);
		}
		return src;
	}
	
	private URL getUrl(String URI) throws MalformedURLException
	{
		final String subDomain;
		String name = opt.getDeveloperName();
		switch(opt.getTier())
		{
		case BETA:
			subDomain = "beta-app";
			break;
		case DEV:
			if(name == null)
			{
				subDomain = "dev-app";
			}
			else
			{
				subDomain = name + "-dev-app";
			}
			break;
		case PROD:
		default:
			subDomain = "app";
			break;
		
		}
		return new URL("http://" + subDomain + ".emitter.co:8080/EmitterServer/Emitter/" + URI);
	}
	
	public Connection createConnection(String URI) throws EmitterException 
	{
		try
		{
			return new Connection(getUrl(URI));
		}
		catch(MalformedURLException ex)
		{
			throw new EmitterException("Could not parse uri " + URI, ex);
		}
	}
	
	private void saveToken(String token) throws EmitterException
	{
		persist.saveObject(token, EMITTER_TOKEN_KEY);
	}
	
	private String getToken() throws EmitterException
	{
		return persist.<String>getObject(EMITTER_TOKEN_KEY);
	}

	private void saveSource(Source src) throws EmitterException
	{
		persist.saveObject(src, EMITTER_SOURCE_KEY);
	}
	
	class EmitRespReciever extends ConnectionReciver
	{
		private EmitRespReciever(Connection _con, ConnectUtil _util) {
			super(_con, EmitResp.class);
		}
		
		@Override
		public EmitResp recieve() throws EmitterException
		{
			EmitResp ret = null;
			Object obj = super.recieve();
			if(obj instanceof EmitResp)
			{
				ret = (EmitResp) obj;
				if(ret.getToken() != null)
					saveToken(ret.getToken());
				return ret;
			}
			throw new EmitterException("Illegal response");
		}
	}
	
	public EmitRespReciever emit(EmitReq req) throws EmitterException
	{
		Connection con = createConnection("emit");
		Source src = getSource();
		if(src != null)
		{
			req.setSource(src);
		}
		String token = getToken();
		if(token != null)
		{
			req.setToken(token);
		}
		con.post(req);
		return new EmitRespReciever(con, this);
	}
	
	class LoginRespReciever extends ConnectionReciver
	{
		private LoginRespReciever(Connection _con) {
			super(_con, LoginResp.class);
		}
		
		@Override
		public LoginResp recieve() throws EmitterException
		{
			LoginResp ret = null;
			Object obj = super.recieve();
			if(obj instanceof LoginResp)
			{
				ret = (LoginResp) obj;
				saveSource(ret.getSource());
				if(ret.getToken() != null)
					saveToken(ret.getToken());
				return ret;
			}
			throw new EmitterException("Illegal response");
		}
	}
	
	public LoginRespReciever login(LoginReq req) throws EmitterException  
	{
		Connection con = createConnection("login");
		Source src = getSource();
		if(src != null)
		{
			req.setSource(src);
		}
		con.post(req);
		return new LoginRespReciever(con);

	}
	
	class RegisterRespReciever extends ConnectionReciver
	{
		private RegisterRespReciever(Connection _con) {
			super(_con, RegisterResp.class);
		}
		
		@Override
		public RegisterResp recieve() throws EmitterException
		{
			RegisterResp ret = null;
			Object obj = super.recieve();
			if(obj instanceof RegisterResp)
			{
				ret = (RegisterResp) obj;
				if(ret.getFailReson() == null)
				{
					saveSource(ret.getSource());
					return ret;
				}
			}
			throw new EmitterException("Illegal response");
		}
	}

	public RegisterRespReciever register(RegisterReq req) throws EmitterException {
		Connection con = createConnection("register");
		con.post(req);
		return new RegisterRespReciever(con);
	}
}
