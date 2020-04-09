package br.com.javana.gui;

import javax.swing.JTextField;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.report.ReportConstants;
import br.com.javana.utils.MessageLoader;

public class LabelsIntervalChooser extends AbstractIntervalChooser {
	private static final long serialVersionUID = 1L;
	private JTextField txtTo;
	private JTextField txtFrom;
	private Long from;
	private Long to;


	protected void btnOKAction() throws BusinessException {
		try {
			from = Long.parseLong(this.txtFrom.getText());
		} catch (NumberFormatException e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(15));
		}

		try {
			to = Long.parseLong(this.txtTo.getText());
		} catch (NumberFormatException e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(16));
		}
	}

	@Override
	public void setTextFields() {
		txtFrom = new JTextField();
		panel.add(txtFrom, "2, 1");

		txtTo = new JTextField();
		panel.add(txtTo, "2, 2");

	}
	
	@Override
	protected void setParams() {
		params.put(ReportConstants.FROM_PARAMETER, from.toString());
		params.put(ReportConstants.TO_PARAMETER, to.toString());
	}

}
