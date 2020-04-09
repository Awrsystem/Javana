package br.com.javana.persistence.pojo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "protocol")
@SequenceGenerator(name = "protocol_sequence", sequenceName = "protocol_id_seq", allocationSize = 1)
public class ProtocolPOJO extends BasePOJO
{
	private long id;
	private long number;
	private Timestamp leaveDate;
	/**
	 * 0 - normal 1- invalidado
	 */
	private Integer status;

	public ProtocolPOJO()
	{
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "protocol_sequence")
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@Column(nullable = false)
	public Timestamp getLeaveDate()
	{
		return leaveDate;
	}

	public void setLeaveDate(Timestamp leaveDate)
	{
		this.leaveDate = leaveDate;
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

	@Column(nullable = false)
	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}
}
