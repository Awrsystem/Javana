package br.com.javana.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.BoxDTO;
import br.com.javana.utils.MessageLoader;

public class BoxRemoval extends AbstractPickBox {

	private static final long serialVersionUID = 1L;
	protected List<BoxDTO> searchResult = new ArrayList<BoxDTO>();

	@Override
	public void btnOKAction() {
		Object[] options = { "Sim", "Não" };
		int answer = JOptionPane.showOptionDialog(this, "Você gostaria de dar baixa nestas caixas?", "Confirme",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (answer == 0) {

			String requester = JOptionPane.showInputDialog(this, "Nome do solicitante:");

			int[] selected = this.tblSearchResult.getSelectedRows();
			List<Long> codes = new ArrayList<Long>();

			for (int j = 0; j < selected.length; j++) {
				codes.add(Long.parseLong(this.tblSearchResult.getModel().getValueAt(selected[j], 0).toString()));
			}

			try {
				BusinessDelegate.getInstance().getRemovedBox().removeBoxes(requester, codes);
				JOptionPane.showMessageDialog(this, "Caixas removidas com sucesso!");
			} catch (BusinessException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			
		}

	}

	@Override
	public void btnSearchIntervalAction() {
		this.searchResult = new ArrayList<BoxDTO>();
		try {
			Long from = Long.parseLong(this.txtFrom.getText());
			Long to = Long.parseLong(this.txtTo.getText());

			List<BoxDTO> result = BusinessDelegate.getInstance().getBox().findByInterval(from, to);

			for (BoxDTO dto : result) {
				this.searchResult.add(dto);
			}

			this.updateTable();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, MessageLoader.getInstance().getMessage(45));
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	@Override
	public void btnSearchNumberAction() {
		this.searchResult = new ArrayList<BoxDTO>();
		try {
			Long value = Long.parseLong(this.txtNumber.getText());
			this.searchResult.add(BusinessDelegate.getInstance().getBox().findByCode(value));
			this.updateTable();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Digite um número para a pesquisar.");
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	@Override
	protected void updateTable() {

		if (this.searchResult.get(0) == null || this.searchResult.size() == 0) {
			JOptionPane.showMessageDialog(null, "Nenhum valor encontrado.");
		} else {
			for (int i = 0; i < this.searchResult.size(); i++) {
				String data[] = new String[4];
				data[0] = this.searchResult.get(i).getCode() + "";
				data[1] = this.searchResult.get(i).getDeptClient().getClient().getName();
				data[2] = this.searchResult.get(i).getDeptClient().getDepartment().getName();
				data[3] = this.searchResult.get(i).getSubject();
				
				tblModel.addRow(data);
			}

			this.btnOK.setEnabled(true);
		}
	}

}
