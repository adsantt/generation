package com.turismo.apimonumentos.util;

import java.util.List;

public interface DAO <T> {
	
	public boolean create(T t);

	public List<T> getAll();
	
	public T getByName(String nombre);
	
	public void update(T t);
	
	public void delete(int id);
	
}
