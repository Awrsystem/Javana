package br.com.javana.persistence.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "department")
@SequenceGenerator(name = "department_sequence", sequenceName = "department_id_seq", allocationSize=1)
public class DepartmentPOJO extends BasePOJO
{
   private long id;
   private String name;

   public DepartmentPOJO ()
   {
      super();
   }

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_sequence")
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
}
