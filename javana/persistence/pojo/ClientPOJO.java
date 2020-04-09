package br.com.javana.persistence.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "client")
@SequenceGenerator(name = "client_sequence", sequenceName = "client_id_seq", allocationSize=1)
public class ClientPOJO extends BasePOJO
{
	private long id;
	private String name;
	private int number;
	private String phone;
	private String street;
	private String bairro;
	private String city;
	private String zipcode;
	private String email;

	public ClientPOJO()
	{
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_sequence")
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@Column(nullable = false)
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getBairro()
	{
		return bairro;
	}

	public void setBairro(String bairro)
	{
		this.bairro = bairro;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public String getZipcode()
	{
		return zipcode;
	}

	public void setZipcode(String zipcode)
	{
		this.zipcode = zipcode;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}
}