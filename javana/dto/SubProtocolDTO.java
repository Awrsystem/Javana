package br.com.javana.dto;

public class SubProtocolDTO extends BaseDTO
{
   private UserDTO requester;
   private ProtocolDTO protocol;

   public ProtocolDTO getProtocol()
   {
      return protocol;
   }

   public void setProtocol(ProtocolDTO protocol)
   {
      this.protocol = protocol;
   }

   public UserDTO getRequester()
   {
      return requester;
   }

   public void setRequester(UserDTO requester)
   {
      this.requester = requester;
   }
}
