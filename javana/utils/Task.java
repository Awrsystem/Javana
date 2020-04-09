package br.com.javana.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.SwingWorker;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.gui.TaskObserver;
import br.com.javana.gui.TaskSubject;

public class Task extends SwingWorker<Void, Void> implements TaskSubject{
	private File[] selected;
	private static final String BACKUP_PROPERTIES = "conf/backup.properties";
	protected ArrayList<TaskObserver> observers;
	private String message;

	public Task(File[] selectedFiles) {
		this.selected = selectedFiles;
		observers = new ArrayList<TaskObserver>();
	}

	/*
	 * Main task. Executed in background thread. subject
	 */
	@Override
	public Void doInBackground() {
		try {
			setProgress(0);
			ftpTransfer(this.selected);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Executed in event dispatching thread
	 */
	@Override
	public void done() {
	
	}
	
	private void ftpTransfer(File[] files) throws BusinessException {
		FTPClient ftp = new FTPClient();
		Properties props = new Properties();
		try {
			this.setMessage("Tentando conectar a RG Contadores...");
			props.load(new FileInputStream(BACKUP_PROPERTIES));
			ftp.connect(props.getProperty("ftp.server"));
			this.setMessage("Conectado.");
		} catch (FileNotFoundException e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(76));
		} catch (SocketException e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(78));
		} catch (IOException e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(77));
		}

		if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
			try {
				this.setMessage("Iniciando a autenticação...");
				ftp.login(props.getProperty("ftp.user"), props.getProperty("ftp.pass"));
				this.setMessage("Autenticação concluída.");
			} catch (IOException e) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(79));
			}
		} else {
			try {
				this.setMessage("Desconectando...");
				ftp.disconnect();
				this.setMessage("Desconectado.");
				notifyCursor();
			} catch (IOException e) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(80));
			}
			throw new BusinessException(MessageLoader.getInstance().getMessage(81));
		}

		OutputStream os = null;
		
		try{
			for (File file : files) {
				this.setMessage("Iniciando a tranferencia do arquivo " + file.getName());
				long size = file.length();
				this.notifyPercentage(0);
				double percentage = (100000/size);
				InputStream in = new BufferedInputStream(new FileInputStream(file));
				os = ftp.storeFileStream(file.getName());
				byte[] c = new byte[1000];
				int i = 0;
				while(i != -1){
					i = in.read(c);
					os.write(c);
					notifyPercentage(percentage);
				}
				this.setMessage("Finalizada a tranferencia do arquivo " + file.getName());
			}
		} catch (FileNotFoundException e) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(82));
		} catch (IOException e) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(83));
		}
		finally{
			try {
				os.flush();
				os.close();
				this.setMessage("Desconectando...");
				ftp.disconnect();
				this.setMessage("Desconectado.");
				this.notifyCursor();
			}
			catch (IOException e) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(80));
			}
		} 
	}

	public void notifyObservers() {
		for (TaskObserver o : observers) {
			o.update(this.message);
		}
	}

	public void registerObserver(TaskObserver o) {
		observers.add(o);
	}

	public void removeObserver(TaskObserver o) {
		int i = observers.indexOf(o);

		if (i >= 0) {
			observers.remove(i);
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		this.notifyObservers();
	}

	public void notifyPercentage(double percentage) {
		for (TaskObserver o : observers) {
			o.update(percentage);
		}
		
	}

	public void notifyCursor() {
		for (TaskObserver o : observers) {
			o.updateCursor();
		}
	}
}
