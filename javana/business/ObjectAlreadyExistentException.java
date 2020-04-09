package br.com.javana.business;

import br.com.javana.business.BusinessException.BusinessException;

public class ObjectAlreadyExistentException extends BusinessException
{

   /**
    * 
    */
   private static final long serialVersionUID = -7287676107989259206L;

   public ObjectAlreadyExistentException (Exception e)
   {
      super(e);
   }

   public ObjectAlreadyExistentException ()
   {
      super();
   }

}
