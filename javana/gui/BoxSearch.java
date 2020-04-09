package br.com.javana.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.BoxDTO;

public class BoxSearch extends AbstractSearchFrame<BoxDTO>
{
   private static final long serialVersionUID = 1L;

   /**
    * Guarda o resultado da pesquisa realizada
    */
   private List<BoxDTO> searchResult = new ArrayList<BoxDTO>();
   /**
    * Nome das colunas que irão aparecer no resultado da pesquisa
    */
   private String[] colunas = new String[] { "Código" };

   public BoxSearch ()
   {
      super();
      super.getLblSearchCriteria().setText("Código:");
   }

   @Override
   public void btnOKAction()
   {
      String value = (String) (this.tblSearchResult.getValueAt(this.tblSearchResult.getSelectedRow(), 0));
      Component[] components = super.getDesktopPane().getComponents();

      for (int i = 0; i < components.length; i++)
      {
         if (components[i] instanceof BoxRegistration)
         {
            BoxRegistration br = (BoxRegistration) components[i];

            BoxDTO selectedBox = null;
            for (BoxDTO box : searchResult)
            {
               if (box.getCode() == new Long(value))
               {
                  selectedBox = box;
               }
            }

            br.getTxtCode().setText(selectedBox.getCode() + "");
            br.getTxtMonth().setText(selectedBox.getMonth());
            br.getTxtSubject().setText(selectedBox.getSubject());
            br.getTxtYear().setText(selectedBox.getYear());
            br.getCbClient().setSelectedItem(selectedBox.getDeptClient().getClient().getName());
            br.getCbDepartment().setSelectedItem(selectedBox.getDeptClient().getDepartment().getName());
            br.getBtnUpdate().setEnabled(true);
            br.getBtnOK().setEnabled(false);
            br.getTxtSelectedBox().setText(selectedBox.getId() + "");
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
   public String getLineValue(BoxDTO dto)
   {
      return dto.getCode() + "";
   }

   @Override
   public List<BoxDTO> getSearchResult()
   {
      return this.searchResult;
   }

   @Override
   public String getWindowTitle()
   {
      return "Buscar Caixas";
   }

   @Override
   public void makeSearch() throws BusinessException
   {
      try
      {
         Long value = Long.parseLong(this.txtSearchCriteria.getText());
         this.searchResult.add(BusinessDelegate.getInstance().getBox().findByCode(value));
      }
      catch(NumberFormatException e)
      {
         JOptionPane.showMessageDialog(this, "Digite um número para a pesquisar.");
      }
   }

   @Override
   public void setSearchResult(List<BoxDTO> list)
   {
      this.searchResult = list;
   }

}
