package br.com.javana.persistence.dao;

import org.hibernate.HibernateException;

public class PersistenceException extends Exception
{
	private String msg;
	
	
	public PersistenceException(HibernateException e)
	{
	}

	public PersistenceException(String string) {
		this.msg = string;
	}

	private static final long serialVersionUID = 1L;


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
