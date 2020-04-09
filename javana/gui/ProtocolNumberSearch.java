package br.com.javana.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.LostProtocolNumberDTO;

public class ProtocolNumberSearch extends AbstractNumberSearch {

	private static final long serialVersionUID = 852730151112382730L;
	private List<LostProtocolNumberDTO> searchResult = new ArrayList<LostProtocolNumberDTO>();
	   
		protected void populateTable() {
			DefaultTableModel tblClientSearchModel = new DefaultTableModel(
					new String[] { "Número" }, 0);
			try {
				this.searchResult = BusinessDelegate.getInstance().getLostProtocol().findAll();
			} catch (BusinessException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}

			for (int i = 0; i < this.searchResult.size(); i++) {
				String data[] = new String[1];
				data[0] = this.searchResult.get(i).getNumber() + "";
				tblClientSearchModel.addRow(data);
			}
			getTblSearchResult().setModel(tblClientSearchModel);
			getBtnOK().setEnabled(true);
		}
		
		protected void btnOKAction() {
			Component[] components = super.getDesktopPane().getComponents();
			String selectedValue = getTblSearchResult().getValueAt(getTblSearchResult().getSelectedRow(), 0).toString();

			for (int i = 0; i < components.length; i++) {
				if (components[i] instanceof ProtocolRegistration) {
					ProtocolRegistration br = (ProtocolRegistration) components[i];
					br.getTxtNumber().setText(selectedValue);

					br.setLost(true);
				}
			}

			this.dispose();
		}


}
