package br.com.javana.dto;

import java.sql.Timestamp;

public class ProtocolPrintDTO extends BaseDTO
{
	private String requester;
	private String boxes;
	private String client;
	private long protocolNumber;
	private String department;
	private long boxCount;
	private Timestamp date;
	
	public String getBoxes()
	{
		return boxes;
	}
	public void setBoxes(String boxes)
	{
		this.boxes = boxes;
	}
	public String getRequester()
	{
		return requester;
	}
	public void setRequester(String requester)
	{
		this.requester = requester;
	}
	public String getClient()
	{
		return client;
	}
	public void setClient(String client)
	{
		this.client = client;
	}
	public long getProtocolNumber()
	{
		return protocolNumber;
	}
	public void setProtocolNumber(long protocolNumber)
	{
		this.protocolNumber = protocolNumber;
	}
	public String getDepartment()
	{
		return department;
	}
	public void setDepartment(String department)
	{
		this.department = department;
	}
	public long getBoxCount()
	{
		return boxCount;
	}
	public void setBoxCount(long boxCount)
	{
		this.boxCount = boxCount;
	}
	public Timestamp getDate()
	{
		return date;
	}
	public void setDate(Timestamp date)
	{
		this.date = date;
	}
}
