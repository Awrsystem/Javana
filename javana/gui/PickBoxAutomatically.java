package br.com.javana.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.utils.MessageLoader;

public class PickBoxAutomatically extends JInternalFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	protected JPanel panel;
	protected JLabel lblNumber;
	protected JButton btnOK;
	protected JTable tblSearchResult;
	protected JButton btnCancel;
	protected JTextField txtNumber;
	protected String[] colunas = new String[] { "Código" };
	protected DefaultTableModel tblModel = new DefaultTableModel(this.colunas, 0);
	private JScrollPane scrollPane;

	private String client;
	protected List<Long> searchResult = new ArrayList<Long>();
	protected Set<Long> allBoxes = new HashSet<Long>();

	public PickBoxAutomatically() {
		super();
		initGUI();
		txtNumber.requestFocus(false);
		txtNumber.addActionListener(this);
	}

	private void initGUI() {
		this.setPreferredSize(new java.awt.Dimension(558, 386));
		this.setBounds(0, 0, 558, 386);
		setVisible(true);
		this.setTitle("Seleção de Caixas");
		this.setResizable(false);
		this.setClosable(true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.createPanel();
	}

	private void createPanel() {
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		TableLayout panelLayout = new TableLayout(
				new double[][] {
						{ 10.0, 70.0, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, 61.0,
								90.0, 10.0 },
						{ 10.0, 24.0, 24.0, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL,
								TableLayout.FILL, TableLayout.FILL, 24.0, 10.0 } });
		panelLayout.setHGap(5);
		panelLayout.setVGap(5);
		panel.setLayout(panelLayout);

		lblNumber = new JLabel();
		panel.add(lblNumber, "1, 1, r, f");
		lblNumber.setText("Número:");

		txtNumber = new JTextField();
		panel.add(txtNumber, "2, 1, 6, 1");

		btnOK = new JButton();
		panel.add(btnOK, "6, 9");
		btnOK.setText("OK");
		btnOK.addActionListener(this);
		btnOK.setEnabled(false);

		btnCancel = new JButton();
		panel.add(btnCancel, "7, 9");
		btnCancel.setText("Cancelar");
		btnCancel.addActionListener(this);

		tblSearchResult = new JTable();
		this.tblSearchResult.setModel(tblModel);
		panel.add(tblSearchResult, "1, 3, 7, 8");

		scrollPane = new JScrollPane();
		panel.add(scrollPane, "1, 3, 7, 8");
		scrollPane.setPreferredSize(new java.awt.Dimension(522, 243));
		scrollPane.setViewportView(tblSearchResult);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnCancel)) {
			this.dispose();
		}

		if (e.getSource().equals(btnOK)) {
			this.btnOKAction();
		}

		if (e.getSource().equals(txtNumber)) {
			putValueOnTable();
			txtNumber.requestFocus(false);
		}
	}

	private void putValueOnTable() {
		this.searchResult.add(Long.parseLong(txtNumber.getText()));
		this.allBoxes.add(Long.parseLong(txtNumber.getText()));
		this.updateTable();
		this.txtNumber.setText("");
	}

	public String[] getColunas() {
		return colunas;
	}

	public void setColunas(String[] colunas) {
		this.colunas = colunas;
	}

	public DefaultTableModel getTblModel() {
		return tblModel;
	}

	public void setTblModel(DefaultTableModel tblModel) {
		this.tblModel = tblModel;
	}

	public JTextField getTxtNumber() {
		return txtNumber;
	}

	public void setTxtNumber(JTextField txtNumber) {
		this.txtNumber = txtNumber;
	}

	public void btnOKAction() {
		try {
			BusinessDelegate.getInstance().getBox().checkClientIsOwner(allBoxes, client);

			List<Long> av = new ArrayList<Long>();
			for (Long l : allBoxes) {
				av.add(l);
			}

			List<Long> unavailable = BusinessDelegate.getInstance().getBoxSubprotocol().checkForUnavailableBoxes(av);

			for (Long box : unavailable) {
				if (allBoxes.contains(box)) {
					allBoxes.remove(box);
				}
			}

			if (allBoxes.size() > 0) {
				Component[] components = super.getDesktopPane().getComponents();

				for (int i = 0; i < components.length; i++) {
					if (components[i] instanceof ProtocolRegistration) {
						ProtocolRegistration pr = (ProtocolRegistration) components[i];

						for (Long code : allBoxes) {
							String data[] = new String[1];
							data[0] = code + "";
							pr.getModel().addRow(data);
						}
					}
				}

				this.dispose();
			}
			else{
				JOptionPane.showMessageDialog(this, MessageLoader.getInstance().getMessage(67));
			}

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
				data[0] = this.searchResult.get(i) + "";
				tblModel.addRow(data);
			}

			this.searchResult.remove(0);
			this.btnOK.setEnabled(true);
		}
	}
}
