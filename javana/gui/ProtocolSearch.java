package br.com.javana.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.BoxSubprotocolDTO;
import br.com.javana.dto.SubProtocolDTO;

public class ProtocolSearch extends AbstractSearchFrame<SubProtocolDTO> {
	private static final long serialVersionUID = 1L;
	/**
	 * Guarda o resultado da pesquisa realizada
	 */
	private List<SubProtocolDTO> searchResult = new ArrayList<SubProtocolDTO>();
	/**
	 * Nome das colunas que irão aparecer no resultado da pesquisa
	 */
	private String[] colunas = new String[] { "Cliente", "Setor", "Solicitante" };

	public ProtocolSearch() {
		super();
		super.getLblSearchCriteria().setText("Núm. Prot:");
	}

	@Override
	public void btnOKAction() {
		String client = (String) (this.tblSearchResult.getValueAt(this.tblSearchResult.getSelectedRow(), 0));
		String department = (String) (this.tblSearchResult.getValueAt(this.tblSearchResult.getSelectedRow(), 1));
		String requester = (String) (this.tblSearchResult.getValueAt(this.tblSearchResult.getSelectedRow(), 2));
		SubProtocolDTO selectedSubProt = null;

		for (SubProtocolDTO dto : searchResult) {
			if (dto.getRequester().getDeptClient().getClient().getName().equals(client)
					&& (dto.getRequester().getDeptClient().getDepartment().getName().equals(department))
					&& (dto.getRequester().getName().equals(requester))) {
				selectedSubProt = dto;
			}
		}

		Component[] components = super.getDesktopPane().getComponents();

		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof ProtocolRegistration) {
				ProtocolRegistration pr = (ProtocolRegistration) components[i];

				List<BoxSubprotocolDTO> boxes;
				try {
					boxes = BusinessDelegate.getInstance().getBoxSubprotocol().findBoxesBySubId(
							selectedSubProt.getId());
					int size = pr.getModel().getRowCount();
					for (int j = 0; j < size; j++) {
						pr.getModel().removeRow(0);
					}

					for (BoxSubprotocolDTO dto : boxes) {
						String data[] = new String[1];
						data[0] = dto.getBox().getCode() + "";
						pr.getModel().addRow(data);
					}
				} catch (BusinessException e) {
					JOptionPane.showMessageDialog(this, e.getMessage());
				}

				pr.getTxtNumber().setText(selectedSubProt.getProtocol().getNumber() + "");
				pr.getTxtNumber().setEditable(false);
				pr.getCbClient().setSelectedItem(client);
				pr.getCbClient().setEnabled(false);
				pr.getCbDepartment().setSelectedItem(department);
				// pr.getCbDepartment().setEnabled(false);
				pr.getCbRequester().setSelectedItem(requester);
				// pr.getCbRequester().setEnabled(false);
				pr.btnOK.setEnabled(false);
				pr.btnUpdate.setEnabled(true);
				pr.getBtnMoreItens().setEnabled(true);
				pr.getBtnManualNUmber().setEnabled(true);
				pr.setSelectedSubprotocol(selectedSubProt);
			}
		}

		this.dispose();
	}

	@Override
	public String[] getColumnsNames() {
		return this.colunas;
	}

	@Override
	public String getLineValue(SubProtocolDTO dto) {
		return null;
	}

	@Override
	public List<SubProtocolDTO> getSearchResult() {
		return this.searchResult;
	}

	@Override
	public String getWindowTitle() {
		return "Busca Protocolos";
	}

	@Override
	public void makeSearch() throws BusinessException {
		try {
			Long value = Long.parseLong(this.txtSearchCriteria.getText());
			this.searchResult.addAll(BusinessDelegate.getInstance().getSubprotocol().findAllByProtocolNumber(value));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Digite um número para a pesquisar.");
		}
	}

	@Override
	public void setSearchResult(List<SubProtocolDTO> list) {
		this.searchResult = list;
	}

	@Override
	protected void btnSearchAction() {
		if (txtSearchCriteria.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Digite algum valor para a pesquisa.");
		} else {
			try {
				this.makeSearch();
				DefaultTableModel tblClientSearchModel = new DefaultTableModel(this.getColumnsNames(), 0);

				if (this.getSearchResult().size() == 0 || this.getSearchResult().get(0) == null) {
					JOptionPane.showMessageDialog(null, "Nenhum valor encontrado.");
				} else {
					for (int i = 0; i < this.getSearchResult().size(); i++) {
						String data[] = new String[3];
						data[0] = this.getSearchResult().get(i).getRequester().getDeptClient().getClient().getName();
						data[1] = this.getSearchResult().get(i).getRequester().getDeptClient().getDepartment()
								.getName();
						data[2] = this.getSearchResult().get(i).getRequester().getName();
						tblClientSearchModel.addRow(data);
					}
					this.tblSearchResult.setModel(tblClientSearchModel);
					this.btnOK.setEnabled(true);
				}
			} catch (BusinessException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
	}

}
