package br.com.javana.persistence.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "sub_protocol")
@SequenceGenerator(name = "sub_protocol_sequence", sequenceName = "sub_protocol_id_seq", allocationSize = 1)
public class SubProtocolPOJO extends BasePOJO
{
   private long id;
   private ProtocolPOJO protocol;
   private UserPOJO requester;

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sub_protocol_sequence")
   public long getId()
   {
      return id;
   }

   public void setId(long id)
   {
      this.id = id;
   }

   @ManyToOne()
   @JoinColumn(name = "ID_PROTOCOL")
   public ProtocolPOJO getProtocol()
   {
      return protocol;
   }

   public void setProtocol(ProtocolPOJO protocol)
   {
      this.protocol = protocol;
   }

   @ManyToOne()
   @JoinColumn(name = "ID_user")
   public UserPOJO getRequester()
   {
      return requester;
   }

   public void setRequester(UserPOJO requester)
   {
      this.requester = requester;
   }
}
