package br.com.javana.persistence.pojo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;

@Entity
@Table(name = "box", uniqueConstraints = {@UniqueConstraint(columnNames={"code"})})
@org.hibernate.annotations.Table(appliesTo="box", indexes = {@Index(name="idx_code", columnNames = {"code"})})
@SequenceGenerator(name = "box_sequence", sequenceName = "box_id_seq", allocationSize = 1)
public class BoxPOJO extends BasePOJO
{
   private long id;
   private long code;
   private String subject;
   private DepartmentClientPOJO deptClient;
   private Timestamp registrationDate;
   private String month;
   private String year;

   public BoxPOJO ()
   {
      super();
   }

   @Column(nullable = false)
   public long getCode()
   {
      return code;
   }

   public void setCode(long code)
   {
      this.code = code;
   }

   @ManyToOne()
   @JoinColumn(name = "ID_DEPARTMENT_COMPANY", nullable = false)
   public DepartmentClientPOJO getDeptClient()
   {
      return deptClient;
   }

   public void setDeptClient(DepartmentClientPOJO deptClient)
   {
      this.deptClient = deptClient;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "box_sequence")
   public long getId()
   {
      return id;
   }

   public void setId(long id)
   {
      this.id = id;
   }

   @Column(nullable = false)
   public String getSubject()
   {
      return subject;
   }

   public void setSubject(String subject)
   {
      this.subject = subject;
   }

   public String getMonth()
   {
      return month;
   }

   public void setMonth(String month)
   {
      this.month = month;
   }

   @Column(nullable = false)
   public Timestamp getRegistrationDate()
   {
      return registrationDate;
   }

   public void setRegistrationDate(Timestamp registrationDate)
   {
      this.registrationDate = registrationDate;
   }

   public String getYear()
   {
      return year;
   }

   public void setYear(String year)
   {
      this.year = year;
   }
}
