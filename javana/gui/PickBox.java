package br.com.javana.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.BoxDTO;
import br.com.javana.utils.MessageLoader;

public class PickBox extends AbstractPickBox {
	private static final long serialVersionUID = 1L;
	private String client;
	protected List<BoxDTO> searchResult = new ArrayList<BoxDTO>();

	@Override
	public void btnSearchNumberAction() {
		this.searchResult = new ArrayList<BoxDTO>();
		try {
			Long value = Long.parseLong(this.txtNumber.getText());
			List<BoxDTO> result = BusinessDelegate.getInstance().getBox().findByIntervalClientAvail(value, value,
					client);

			if (result.size() > 0) {
				for (BoxDTO dto : result) {
					this.searchResult.add(dto);
				}
				this.updateTable();
			} else {
				JOptionPane.showMessageDialog(this, MessageLoader.getInstance().getMessage(67));
			}

		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, MessageLoader.getInstance().getMessage(50));
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	public void btnOKAction() {
		int[] selected = this.tblSearchResult.getSelectedRows();
		Component[] components = super.getDesktopPane().getComponents();

		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof ProtocolRegistration) {
				ProtocolRegistration pr = (ProtocolRegistration) components[i];

				for (int j = 0; j < selected.length; j++) {
					String data[] = new String[1];
					data[0] = this.tblSearchResult.getModel().getValueAt(selected[j], 0).toString();
					pr.getModel().addRow(data);
				}
			}
		}

		this.dispose();
	}

	@Override
	public void btnSearchIntervalAction() {
		this.searchResult = new ArrayList<BoxDTO>();
		try {
			Long from = Long.parseLong(this.txtFrom.getText());
			Long to = Long.parseLong(this.txtTo.getText());

			List<BoxDTO> result = BusinessDelegate.getInstance().getBox().findByIntervalClientAvail(from, to, client);

			if (result.size() > 0) {
				for (BoxDTO dto : result) {
					this.searchResult.add(dto);
				}

				this.updateTable();

			} else {
				JOptionPane.showMessageDialog(this, MessageLoader.getInstance().getMessage(68));
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, MessageLoader.getInstance().getMessage(50));
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	protected void updateTable() {

		if (this.searchResult.get(0) == null || this.searchResult.size() == 0) {
			JOptionPane.showMessageDialog(null, "Nenhum valor encontrado.");
		} else {
			for (int i = 0; i < this.searchResult.size(); i++) {
				String data[] = new String[1];
				data[0] = this.searchResult.get(i).getCode() + "";
				tblModel.addRow(data);
			}

			this.btnOK.setEnabled(true);
		}
	}
}
