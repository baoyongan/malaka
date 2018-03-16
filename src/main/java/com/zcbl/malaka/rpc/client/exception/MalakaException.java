/**
 * 
 */
package com.zcbl.malaka.rpc.client.exception;

/**
 * @author jys 2016年8月29日
 */
public class MalakaException extends RuntimeException
{

	private static final long serialVersionUID = 8539784209301548070L;

	public MalakaException()
	{
		super();
	}

	public MalakaException(String exception)
	{
		super(exception);
	}

	public MalakaException(Throwable able)
	{
		super(able);
	}

	public MalakaException(String exception, Throwable able)
	{
		super(exception, able);
	}
}
