package br.com.javana.gui;

import java.awt.Component;
import java.util.List;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.ClientDTO;

/**
 * Janela de pesquisa de clientes
 */
public class ClientSearch extends AbstractSearchFrame<ClientDTO>
{
   private static final long serialVersionUID = -8635362147590937355L;

   /**
    * Guarda o resultado da pesquisa realizada
    */
   private List<ClientDTO> searchResult;
   /**
    * Nome das colunas que irão aparecer no resultado da pesquisa
    */
   private String[] colunas = new String[] { "Cliente" };

   @Override
   public String getWindowTitle()
   {
      return "Buscar Cliente";
   }

   @Override
   public String[] getColumnsNames()
   {
      return this.colunas;
   }

   @Override
   public List<ClientDTO> getSearchResult()
   {
      return this.searchResult;
   }

   @Override
   public void setSearchResult(List<ClientDTO> list)
   {
      this.searchResult = list;      
   }

   @Override
   public void makeSearch() throws BusinessException
   {
      this.searchResult = BusinessDelegate.getInstance().getClient().findByName(this.txtSearchCriteria.getText());
   }

   @Override
   public String getLineValue(ClientDTO dto)
   {
      return dto.getName();
   }

   @Override
   public void btnOKAction()
   {
      String value = (String) (this.tblSearchResult.getValueAt(this.tblSearchResult.getSelectedRow(), 0));
      Component[] components = super.getDesktopPane().getComponents();

      for (int i = 0; i < components.length; i++)
      {
         if (components[i] instanceof ClientRegistration)
         {
            ClientRegistration cr = (ClientRegistration) components[i];

            ClientDTO selectedClient = null;
            for (ClientDTO client : searchResult)
            {
               if (client.getName().equals(value))
               {
                  selectedClient = client;
               }
            }

            cr.getTxtClientBairro().setText(selectedClient.getBairro());
            cr.getTxtClientCity().setText(selectedClient.getCity());
            cr.getTxtClientEmail().setText(selectedClient.getEmail());
            cr.getTxtClientName().setText(selectedClient.getName());
            cr.getTxtClientNumber().setText(selectedClient.getNumber() + "");
            cr.getTxtClientPhone().setText(selectedClient.getPhone());
            cr.getTxtClientStreet().setText(selectedClient.getStreet());
            cr.getTxtClientZipcode().setText(selectedClient.getZipcode());

            cr.getBtnDelete().setEnabled(true);
            cr.getBtnUpdate().setEnabled(true);
            cr.getBtnOK().setEnabled(false);
            cr.getTxtSelectedClient().setText(selectedClient.getId() + "");
         }
      }

      this.dispose();
   }
}
