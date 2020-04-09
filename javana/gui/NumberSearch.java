package br.com.javana.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.LostBoxNumbersDTO;

public class NumberSearch extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = 5982977066891325050L;
	private JPanel panel;
	private JButton btnOK;
	private JTable tblSearchResult;
	private List<LostBoxNumbersDTO> searchResult = new ArrayList<LostBoxNumbersDTO>();
	private JScrollPane scrollPanel;

	public NumberSearch() {
		super();
		initGUI();
	}

	private void initGUI() {
		this.setPreferredSize(new java.awt.Dimension(400, 310));
		this.setBounds(25, 25, 400, 310);
		setVisible(true);
		this.setTitle("Números Disponíveis");
		this.setResizable(false);
		this.setClosable(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setIconifiable(true);

		this.createPanel();
	}

	private void createPanel() {
		panel = new JPanel();
		TableLayout panelLayout = new TableLayout(
				new double[][] {
						{ 10.0, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL,
								TableLayout.FILL, 10.0 },
						{ 10.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, TableLayout.FILL, 10.0 } });
		panelLayout.setHGap(5);
		panelLayout.setVGap(5);
		panel.setLayout(panelLayout);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setPreferredSize(new java.awt.Dimension(395, 291));

		btnOK = new JButton();
		panel.add(btnOK, "5, 9");
		btnOK.setText("OK");
		btnOK.setEnabled(false);
		btnOK.addActionListener(this);

		tblSearchResult = new JTable();

		this.populateTable();

		scrollPanel = new JScrollPane(tblSearchResult);
		panel.add(scrollPanel, "1, 1, 5, 8");
	}

	private void populateTable() {
		DefaultTableModel tblClientSearchModel = new DefaultTableModel(new String[] { "Número" }, 0);
		try {
			this.searchResult = BusinessDelegate.getInstance().getLostBox().findAll();
		} catch (BusinessException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}

		for (int i = 0; i < this.searchResult.size(); i++) {
			String data[] = new String[1];
			data[0] = this.searchResult.get(i).getNumber() + "";
			tblClientSearchModel.addRow(data);
		}
		this.tblSearchResult.setModel(tblClientSearchModel);
		this.btnOK.setEnabled(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.btnOK)) {
			this.btnOKAction();
		}
	}

	private void btnOKAction() {
		Component[] components = super.getDesktopPane().getComponents();
		String selectedValue = this.tblSearchResult.getValueAt(this.tblSearchResult.getSelectedRow(), 0).toString();

		for (int i = 0; i < components.length; i++) {
			if (components[i] instanceof BoxRegistration) {
				BoxRegistration br = (BoxRegistration) components[i];
				br.getTxtCode().setText(selectedValue);

				br.setLost(true);
			}
		}

		this.dispose();
	}
}
