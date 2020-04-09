package br.com.javana.dto;

import java.sql.Timestamp;

public class RemovedBoxDTO extends BaseDTO
{
   private long code;
   private String client;
   private String department;
   private Timestamp removalDate;
   private String requester;
   
   public String getClient()
   {
      return client;
   }
   public void setClient(String client)
   {
      this.client = client;
   }
   public long getCode()
   {
      return code;
   }
   public void setCode(long code)
   {
      this.code = code;
   }
   public String getDepartment()
   {
      return department;
   }
   public void setDepartment(String department)
   {
      this.department = department;
   }
   public Timestamp getRemovalDate()
   {
      return removalDate;
   }
   public void setRemovalDate(Timestamp removalDate)
   {
      this.removalDate = removalDate;
   }
   public String getRequester()
   {
      return requester;
   }
   public void setRequester(String requester)
   {
      this.requester = requester;
   }
}
