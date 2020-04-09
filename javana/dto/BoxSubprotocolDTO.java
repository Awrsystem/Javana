package br.com.javana.dto;

import java.sql.Timestamp;

public class BoxSubprotocolDTO extends BaseDTO
{
   private BoxDTO box;
   private SubProtocolDTO subprotocol;
   private Timestamp backDate;
   
   public Timestamp getBackDate()
   {
      return backDate;
   }
   public void setBackDate(Timestamp backDate)
   {
      this.backDate = backDate;
   }
   public BoxDTO getBox()
   {
      return box;
   }
   public void setBox(BoxDTO box)
   {
      this.box = box;
   }
   public SubProtocolDTO getSubprotocol()
   {
      return subprotocol;
   }
   public void setSubprotocol(SubProtocolDTO subprotocol)
   {
      this.subprotocol = subprotocol;
   }
}
