package br.com.javana.business;

import br.com.javana.business.BusinessException.BusinessException;

public class ChildException extends BusinessException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ChildException(Exception e)
	{
		super(e);
	}
	
	public ChildException()
	{
		super();
	}

}
