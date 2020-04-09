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
@Table(name = "removed_box")
@SequenceGenerator(name = "removed_box_sequence", sequenceName = "removed_box_id_seq", allocationSize = 1)
public class RemovedBoxPOJO extends BasePOJO
{
   private long id;
   private long code;
   private String client;
   private String department;
   private Timestamp removalDate;
   private String requester;

   @Column(nullable = false)
   public String getClient()
   {
      return client;
   }

   public void setClient(String client)
   {
      this.client = client;
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

   @Column(nullable = false)
   public String getDepartment()
   {
      return department;
   }

   public void setDepartment(String department)
   {
      this.department = department;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "removed_box_sequence")
   public long getId()
   {
      return id;
   }

   public void setId(long id)
   {
      this.id = id;
   }

   @Column(nullable = false)
   public Timestamp getRemovalDate()
   {
      return removalDate;
   }

   public void setRemovalDate(Timestamp removalDate)
   {
      this.removalDate = removalDate;
   }

   @Column(nullable = false)
   public String getRequester()
   {
      return requester;
   }

   public void setRequester(String requester)
   {
      this.requester = requester;
   }
}
