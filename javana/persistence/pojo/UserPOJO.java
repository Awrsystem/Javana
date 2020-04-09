package br.com.javana.persistence.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "person")
@SequenceGenerator(name = "user_sequence", sequenceName = "user_id_seq", allocationSize = 1)
public class UserPOJO extends BasePOJO
{
   private long id;
   private String name;
   private DepartmentClientPOJO deptClient;

   @ManyToOne()
   @JoinColumn(name = "ID_department_client")
   public DepartmentClientPOJO getDeptClient()
   {
      return deptClient;
   }

   public void setDeptClient(DepartmentClientPOJO deptClient)
   {
      this.deptClient = deptClient;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
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
