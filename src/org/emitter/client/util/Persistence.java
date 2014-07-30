package org.emitter.client.util;

import org.emitter.error.EmitterException;

public interface Persistence
{
	public void saveData(String obj, String key) throws EmitterException;
	public String getData(String key) throws EmitterException;
}