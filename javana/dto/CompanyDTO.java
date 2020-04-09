package br.com.javana.dto;

public class CompanyDTO extends BaseDTO
{
	private ClientDTO client;
	private String name;
	public ClientDTO getClient()
	{
		return client;
	}
	public void setClient(ClientDTO client)
	{
		this.client = client;
	}

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
}
