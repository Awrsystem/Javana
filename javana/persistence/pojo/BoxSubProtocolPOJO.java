package br.com.javana.persistence.pojo;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "box_sub_protocol")
@SequenceGenerator(name = "box_sub_protocol_sequence", sequenceName = "box_sub_protocol_id_seq", allocationSize=1)
public class BoxSubProtocolPOJO extends BasePOJO
{
   private long id;
   private BoxPOJO box;
   private SubProtocolPOJO subprotocol;
   private Timestamp backDate;

   public BoxSubProtocolPOJO ()
   {
      super();
   }

   @ManyToOne()
   @JoinColumn(name = "ID_BOX")
   public BoxPOJO getBox()
   {
      return box;
   }

   public void setBox(BoxPOJO box)
   {
      this.box = box;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "box_sub_protocol_sequence")
   public long getId()
   {
      return id;
   }

   public void setId(long id)
   {
      this.id = id;
   }
  
   public Timestamp getBackDate()
   {
      return backDate;
   }

   public void setBackDate(Timestamp returnDate)
   {
      this.backDate = returnDate;
   }

   @ManyToOne()
   @JoinColumn(name = "ID_SUB_PROTOCOL")
   public SubProtocolPOJO getSubprotocol()
   {
      return subprotocol;
   }

   public void setSubprotocol(SubProtocolPOJO subprotocol)
   {
      this.subprotocol = subprotocol;
   }

}
