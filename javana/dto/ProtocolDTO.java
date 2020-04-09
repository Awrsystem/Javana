package br.com.javana.dto;

import java.sql.Timestamp;

public class ProtocolDTO extends BaseDTO
{
   private long number;
   private Timestamp leaveDate;
   private Integer status;

   public Timestamp getLeaveDate()
   {
      return leaveDate;
   }

   public void setLeaveDate(Timestamp leaveDate)
   {
      this.leaveDate = leaveDate;
   }

   public long getNumber()
   {
      return number;
   }

   public void setNumber(long number)
   {
      this.number = number;
   }

   public Integer getStatus()
   {
      return status;
   }

   public void setStatus(Integer status)
   {
      this.status = status;
   }
}
