package br.com.javana.dto;

import java.sql.Timestamp;

public class BoxDTO extends BaseDTO
{
   private long code;

   private String subject;

   private DepartmentClientDTO deptClient;

   private Timestamp registrationDate;

   private String month;

   private String year;

   public long getCode()
   {
      return code;
   }

   public void setCode(long code)
   {
      this.code = code;
   }

   public DepartmentClientDTO getDeptClient()
   {
      return deptClient;
   }

   public void setDeptClient(DepartmentClientDTO deptComp)
   {
      this.deptClient = deptComp;
   }

   public String getMonth()
   {
      return month;
   }

   public void setMonth(String month)
   {
      this.month = month;
   }

   public Timestamp getRegistrationDate()
   {
      return registrationDate;
   }

   public void setRegistrationDate(Timestamp registrationDate)
   {
      this.registrationDate = registrationDate;
   }

   public String getSubject()
   {
      return subject;
   }

   public void setSubject(String subject)
   {
      this.subject = subject;
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
