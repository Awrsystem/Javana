package br.com.javana.gui;

import java.awt.Component;
import java.util.List;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.DepartmentDTO;

public class DepartmentSearch extends AbstractSearchFrame<DepartmentDTO>
{
   private static final long serialVersionUID = 1L;
   
   /**
    * Guarda o resultado da pesquisa realizada
    */
   private List<DepartmentDTO> searchResult;
   /**
    *  Nome das colunas que irão aparecer no resultado da pesquisa
    */
   private String[] colunas = new String[] { "Setor" };
   
   public DepartmentSearch()
   {
	   super();
	   this.getLblSearchCriteria().setText("Setor:");
   }

   @Override
   public void btnOKAction()
   {
      String value = (String) (this.tblSearchResult.getValueAt(this.tblSearchResult.getSelectedRow(), 0));
      Component[] components = super.getDesktopPane().getComponents();

      for (int i = 0; i < components.length; i++)
      {
         if (components[i] instanceof DepartmentRegistration)
         {
            DepartmentRegistration dr = (DepartmentRegistration) components[i];

            DepartmentDTO selectedDepartment = null;
            for (DepartmentDTO dept : searchResult)
            {
               if (dept.getName().equals(value))
               {
                  selectedDepartment = dept;
               }
            }

            dr.getTxtName().setText(selectedDepartment.getName());

            dr.getBtnDelete().setEnabled(true);
            dr.getBtnUpdate().setEnabled(true);
            dr.getBtnOK().setEnabled(false);
            dr.getTxtSelectedDepartment().setText(selectedDepartment.getId() + "");
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
   public String getLineValue(DepartmentDTO dto)
   {
      return dto.getName();
   }

   @Override
   public List<DepartmentDTO> getSearchResult()
   {
      return this.searchResult;
   }

   @Override
   public String getWindowTitle()
   {
      return "Buscar Setores";
   }

   @Override
   public void makeSearch() throws BusinessException
   {
      this.searchResult = BusinessDelegate.getInstance().getDepartment().findByName(this.txtSearchCriteria.getText());
   }

   @Override
   public void setSearchResult(List<DepartmentDTO> list)
   {
      this.searchResult = list;      
   }
    
}
