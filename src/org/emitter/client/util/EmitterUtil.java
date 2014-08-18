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

/**
 * A utility for handling interactions in the emitter network
 * @author jeremy
 *
 */
public class EmitterUtil 
{
	private ConnectUtil util;
	private static EmitterUtil instance; //Singleton
	private EmitterUtil(String ProgramKey, PersistenceUtil persist, ConnectionOptions opt)
	{
		util = new ConnectUtil(ProgramKey, persist, opt);
	}
	/**
	 * 
	 * @return true if create has been called
	 */
	public static boolean isCreated()
	{
		return instance != null;
	}
	
	/**
	 * 
	 * @param appKey The key you got when you registered your app.
	 * @param persist A concrete implementation of the persistence interface
	 * @param opt Connection Options
	 * @return An instance of EmitterUtil
	 */
	public static EmitterUtil create(String appKey, PersistenceUtil persist, ConnectionOptions opt)
	{
		instance = new EmitterUtil(appKey, persist, opt);
		return instance;
	}
	/**
	 * 
	 * @return an instance of EmitterUtil
	 * @throws EmitterException if not yet created. Call create or isCreated first
	 */
	public static EmitterUtil get() throws EmitterException
	{
		if(!isCreated())
			throw new EmitterException("Emitter util has not been created yet");
		return instance;
	}
	
	/**
	 * Generic Emit interface
	 * @param req The emitter request
	 * @return A response receiver to the request
	 * @throws EmitterException if something goes wrong
	 */
	public EmitRespReciever emit(EmitReq req) throws EmitterException
	{
		return util.emit(req);
	}
	
	/**
	 * Emit WiFi and GPS data
	 * @param signal Array of WiFi signals seen
	 * @param gps GPS Coordinates where the WiFi signals were seen
	 * @return a receiver to the response
	 * @throws EmitterException
	 */
	public EmitRespReciever emitWifi(WifiSignal[] signal, GPSCoord gps) throws EmitterException
	{
		EmitReq req = new EmitReq();
		req.setGps(gps);
		req.setWifi(signal);
		return util.emit(req);
	}
	
	/**
	 * Emit logs
	 * @param l A log object with log data
	 * @return A receiver of the response
	 * @throws EmitterException
	 */
	public EmitRespReciever emitLogs(Log l) throws EmitterException 
	{
		EmitReq req = new EmitReq();
		req.setLog(l);
		return util.emit(req);
	}
	
	/**
	 * Generic Login 
	 * @param req A login request
	 * @return A receiver to the login request
	 * @throws EmitterException
	 */
	public LoginRespReciever login(LoginReq req) throws EmitterException
	{
		return util.login(req);
	}
	
	/**
	 * Login with username, password, and device name
	 * @param email User's email address
	 * @param pass User's password
	 * @param deviceName name of the device
	 * @return A receiver of the login request
	 * @throws EmitterException if object is missing
	 */
	public LoginRespReciever login(String email, String pass, String deviceName) throws EmitterException
	{
		if(email == null || email.isEmpty())
			throw new EmitterException("Email is missing");
		if(pass == null || pass.isEmpty())
			throw new EmitterException("Password is missing");
		if(deviceName == null || deviceName.isEmpty())
			throw new EmitterException("deviceName is missing");
		
		AuthReq req = new AuthReq();
		req.setPassword(pass);
		req.setEmail(email);
		LoginReq lr = new LoginReq();
		lr.setAuth(req);
		return util.login(lr);

	}
	
	/**
	 * 
	 * @param email User's email address
	 * @param pass User's chosen password
	 * @param dName Device Name
	 * @return A response receiver for this registration
	 * @throws EmitterException
	 */
	public RegisterRespReciever register(String email, String pass, String dName) throws EmitterException
	{
		RegisterReq req = new RegisterReq();
		req.setDevice(dName);
		AuthReq auth = new AuthReq();
		auth.setPassword(pass);
		auth.setEmail(email);
		req.setAuth(auth);
		return register(req);
	}

	/**
	 * 
	 * @param req The registration request
	 * @return A response receiver to the registration request
	 * @throws EmitterException
	 */
	public RegisterRespReciever register(RegisterReq req) throws EmitterException 
	{
		return util.register(req);
	}

	
}
