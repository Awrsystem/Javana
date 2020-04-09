package br.com.javana.dto;

public class BoxProtocolDTO extends BaseDTO
{
	private Long boxCode;
	private Long protocol;
	private String requester;

	public Long getBoxCode()
	{
		return boxCode;
	}

	public void setBoxCode(Long boxCode)
	{
		this.boxCode = boxCode;
	}

	public Long getProtocol()
	{
		return protocol;
	}

	public void setProtocol(Long protocol)
	{
		this.protocol = protocol;
	}

	public String getRequester()
	{
		return requester;
	}

	public void setRequester(String requester)
	{
		this.requester = requester;
	}

}
