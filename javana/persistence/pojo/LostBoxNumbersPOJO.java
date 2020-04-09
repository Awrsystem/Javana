package br.com.javana.persistence.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "lost_numbers")
@SequenceGenerator(name = "lost_sequence", sequenceName = "lost_id_seq", allocationSize=1)
public class LostBoxNumbersPOJO extends BasePOJO
{
	private long id;
	private long number;
	
	
	public LostBoxNumbersPOJO()
	{
		super();
	}


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lost_sequence")
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}


	@Column(nullable = false)
	public long getNumber()
	{
		return number;
	}

	public void setNumber(long number)
	{
		this.number = number;
	}

}
