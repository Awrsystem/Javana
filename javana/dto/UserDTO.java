package br.com.javana.dto;


public class UserDTO extends BaseDTO
{
   private String name;
   private DepartmentClientDTO deptClient;
   
   public DepartmentClientDTO getDeptClient()
   {
      return deptClient;
   }
   public void setDeptClient(DepartmentClientDTO deptClient)
   {
      this.deptClient = deptClient;
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
