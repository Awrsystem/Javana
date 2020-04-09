package br.com.javana.dto;

public class DepartmentClientDTO extends BaseDTO
{
   private DepartmentDTO department;
   private ClientDTO client;
   
   public ClientDTO getClient()
   {
      return client;
   }
   public void setClient(ClientDTO company)
   {
      this.client = company;
   }
   public DepartmentDTO getDepartment()
   {
      return department;
   }
   public void setDepartment(DepartmentDTO department)
   {
      this.department = department;
   }
}
