package org.emitter.client.util;

import org.emitter.client.util.ConnectUtil.EmitRespReciever;
import org.emitter.client.util.ConnectUtil.LoginRespReciever;
import org.emitter.client.util.ConnectUtil.RegisterRespReciever;
import org.emitter.error.EmitterException;
import org.emitter.types.AuthReq;
import org.emitter.types.EmitReq;
import org.emitter.types.GPSCoord;
import org.emitter.types.Log;
import org.emitter.types.LoginReq;
import org.emitter.types.RegisterReq;
import org.emitter.types.WifiSignal;

public class EmitterUtil 
{
	private ConnectUtil util;
	
	public EmitterUtil(String ProgramKey, PersistenceUtil persist, ConnectionOptions opt)
	{
		util = new ConnectUtil(ProgramKey, persist, opt);
	}
	
	public EmitRespReciever emit(EmitReq req) throws EmitterException
	{
		return util.emit(req);
	}
	
	public EmitRespReciever emitWifi(WifiSignal[] signal, GPSCoord gps) throws EmitterException
	{
		EmitReq req = new EmitReq();
		req.setGps(gps);
		req.setWifi(signal);
		return util.emit(req);
	}
	
	public EmitRespReciever emitLogs(Log l) throws EmitterException 
	{
		EmitReq req = new EmitReq();
		req.setLog(l);
		return util.emit(req);
	}
	
	public LoginRespReciever login(LoginReq req) throws EmitterException
	{
		return util.login(req);
	}
	
	
	public LoginRespReciever login(String user, String pass, String deviceName) throws EmitterException
	{
		AuthReq req = new AuthReq();
		req.setPassword(pass);
		req.setUsername(user);
		LoginReq lr = new LoginReq();
		lr.setAuth(req);
		return util.login(lr);

	}
	
	public RegisterRespReciever register(String user, String pass, String dName) throws EmitterException
	{
		RegisterReq req = new RegisterReq();
		req.setDevice(dName);
		AuthReq auth = new AuthReq();
		auth.setPassword(pass);
		auth.setUsername(user);
		req.setUser(auth);
		return register(req);
	}

	public RegisterRespReciever register(RegisterReq req) throws EmitterException 
	{
		return util.register(req);
	}

	
}
