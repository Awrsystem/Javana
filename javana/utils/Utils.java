package br.com.javana.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.ClientDTO;
import br.com.javana.dto.DepartmentDTO;
import br.com.javana.dto.UserDTO;

public class Utils {
	private static final String BACKUP_PROPERTIES = "conf/backup.properties";

	public static String[] populateClientsCombo(String errorMessage) throws BusinessException {
		List<ClientDTO> dtos = BusinessDelegate.getInstance().getClient().findAllByNameOrder();

		String[] companies = new String[dtos.size() + 1];
		if (dtos.size() > 0) {
			companies[0] = "Selecione";

			for (int i = 0; i < dtos.size(); i++) {
				companies[i + 1] = dtos.get(i).getName();
			}

			return companies;
		} else {
			throw new BusinessException(errorMessage);
		}
	}

	public static String[] populateDepartmentsCombo(String clientName) throws BusinessException {
		List<DepartmentDTO> dtos = BusinessDelegate.getInstance().getDeptClient().findByClient(clientName);

		String[] departments = new String[dtos.size() + 1];
		if (dtos.size() > 0) {
			departments[0] = "Selecione";
			for (int i = 0; i < dtos.size(); i++) {
				departments[i + 1] = dtos.get(i).getName();
			}

			return departments;
		} else {
			departments[0] = "Selecione";
			if (!clientName.equals("Selecione")) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(75));
			}
		}

		return null;
	}

	public static String[] populateUsersCombo(String client, String department) throws BusinessException {

		List<UserDTO> dtos = BusinessDelegate.getInstance().getUser().findByClientDept(client, department);

		String[] users = new String[dtos.size() + 1];
		if (dtos.size() > 0) {
			users[0] = "Selecione";
			for (int i = 0; i < dtos.size(); i++) {
				users[i + 1] = dtos.get(i).getName();
			}

			return users;
		} else {
			users[0] = "Selecione";
			if (!client.equals("Selecione")) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(58));
			}
		}

		return users;
	}

	/**
	 * Para fazer o restore:
	 * 
	 * psql yourdb SUPERUSER -f dumpfile.txt
	 * 
	 * @param dir
	 * @return
	 * @throws BusinessException
	 */
	public static int backup(File dir) throws BusinessException {
		Properties props = new Properties();

		try {
			Timestamp today = new Timestamp(System.currentTimeMillis());
			int ws = today.toString().indexOf(" ");
			String filename = today.toString().substring(0, ws);
			String path = dir.getCanonicalPath() + "\\" + filename + ".txt";
			props.load(new FileInputStream(BACKUP_PROPERTIES));
			List<String> cmds = new ArrayList<String>();
			cmds.add(props.getProperty("pgdump.path"));
			cmds.add("-U");
			cmds.add(props.getProperty("user"));
			cmds.add("-f");
			cmds.add(path);
			cmds.add(props.getProperty("db"));
			ProcessBuilder pb = new ProcessBuilder(cmds);
			Map<String, String> env = pb.environment();
			env.put("PGPASSWORD", props.getProperty("passwd"));
			Process proc = pb.start();
			proc.waitFor();

			return proc.exitValue();

		} catch (IOException e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(73));
		} catch (InterruptedException e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(74));
		}
	}
}
