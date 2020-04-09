package br.com.javana.persistence.dao;

import br.com.javana.persistence.pojo.ClientPOJO;

public class ClientDAO extends BaseDAO<ClientPOJO>
{

   public ClientDAO (Class pClass)
   {
      super(ClientPOJO.class);
   }
}
