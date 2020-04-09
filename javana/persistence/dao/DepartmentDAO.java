package br.com.javana.persistence.dao;

import br.com.javana.persistence.pojo.DepartmentPOJO;

public class DepartmentDAO extends BaseDAO<DepartmentPOJO>
{
   public DepartmentDAO (Class pClass)
   {
      super(DepartmentPOJO.class);
   }
}
