package br.com.javana.gui;

import java.awt.Component;
import java.util.List;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.UserDTO;

public class UserSearch extends AbstractSearchFrame<UserDTO>
{
   private static final long serialVersionUID = 1L;
   private List<UserDTO> searchResult;
   private String[] colunas = new String[] { "Pessoa" };

   public UserSearch()
   {
      super();
      super.getLblSearchCriteria().setText("Nome:");
   }
   
   @Override
   public void btnOKAction()
   {
      String value = (String) (this.tblSearchResult.getValueAt(this.tblSearchResult.getSelectedRow(), 0));
      Component[] components = super.getDesktopPane().getComponents();

      for (int i = 0; i < components.length; i++)
      {
         if (components[i] instanceof UserRegistration)
         {
            UserRegistration ur = (UserRegistration) components[i];

            UserDTO selectedUser = null;
            for (UserDTO user : searchResult)
            {
               if (user.getName().equals(value))
               {
                  selectedUser = user;
               }
            }

            ur.getTxtName().setText(selectedUser.getName());
            ur.getCbClient().setSelectedItem(selectedUser.getDeptClient().getClient().getName());
            ur.getCbDepartment().setSelectedItem(selectedUser.getDeptClient().getDepartment().getName());

            ur.getBtnDelete().setEnabled(true);
            ur.getBtnUpdate().setEnabled(true);
            ur.getBtnOK().setEnabled(false);
            ur.setSelectedUserId(selectedUser.getId());
         }
      }

      this.dispose();      
   }

   @Override
   public String[] getColumnsNames()
   {
      return this.colunas;
   }

   @Override
   public String getLineValue(UserDTO dto)
   {
      return dto.getName();
   }

   @Override
   public List<UserDTO> getSearchResult()
   {
      return this.searchResult;
   }

   @Override
   public String getWindowTitle()
   {
      return "Buscar Pessoas";
   }

   @Override
   public void makeSearch() throws BusinessException
   {
      this.searchResult = BusinessDelegate.getInstance().getUser().findByName(this.txtSearchCriteria.getText());      
   }

   @Override
   public void setSearchResult(List<UserDTO> list)
   {
      this.searchResult = list;            
   }
}
