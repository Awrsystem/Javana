package br.com.javana.persistence.pojo;

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
@Table(name = "department_client", uniqueConstraints = {@UniqueConstraint(columnNames={"id_client", "id_department"})})
@org.hibernate.annotations.Table(appliesTo="department_client", indexes = {@Index(name="idx", columnNames = {"id_client", "id_department"})})
@SequenceGenerator(name = "dept_cli_sequence", sequenceName = "dept_cli_id_seq", allocationSize=1)
public class DepartmentClientPOJO extends BasePOJO
{
   private long id;
   private DepartmentPOJO department;
   private ClientPOJO client;

   public DepartmentClientPOJO ()
   {
      super();
   }

   @ManyToOne()
   @JoinColumn(name = "ID_CLIENT")
   public ClientPOJO getClient()
   {
      return client;
   }

   public void setClient(ClientPOJO client)
   {
      this.client = client;
   }

   @ManyToOne()
   @JoinColumn(name = "ID_DEPARTMENT")
   public DepartmentPOJO getDepartment()
   {
      return department;
   }

   public void setDepartment(DepartmentPOJO department)
   {
      this.department = department;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dept_cli_sequence")
   public long getId()
   {
      return id;
   }

   public void setId(long id)
   {
      this.id = id;
   }

}
