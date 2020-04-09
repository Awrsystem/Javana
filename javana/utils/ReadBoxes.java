package br.com.javana.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.javana.business.BusinessDelegate;
import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.DepartmentClientDTO;
import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.PersistenceException;
import br.com.javana.persistence.pojo.BoxPOJO;

public class ReadBoxes
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		BoxPOJO box = new BoxPOJO();
		File arq = new File("F:/kelly/banco/outros.txt");
		try
		{
			if (arq.exists())
			{
				FileReader arquivo = new FileReader(arq);
				BufferedReader ler = new BufferedReader(arquivo);
				
				boolean fim = false;
				int i = 1;
				while (!fim)
				{
					System.out.println("Lendo linha: " + i);
					String linha = ler.readLine();
					if (linha == null){
						fim = true;
						ler.close();
						arquivo.close();
						System.out.println("Fim do processamento");
						return;
					}
					
					String[] result = linha.split("#");
					if(result[1].equals(""))
					{
						i++;
						return;
					}
					else
					{
						box.setCode(Long.parseLong(result[0]));
						DepartmentClientDTO dcDTO = BusinessDelegate.getInstance().getDeptClient().findByClientDepartment(result[1].trim(), result[7].trim());
						box.setDeptClient(BusinessDelegate.getInstance().getDeptClient().createPOJO(dcDTO));
						box.setMonth(result[3]);
						String data[] = result[5].split("/");
						SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
						Date date = format.parse(data[2].substring(0, 4) + "/" + data[1] + "/" + data[0]); 
						Timestamp dataEntrada = new Timestamp(date.getTime());
						box.setRegistrationDate(dataEntrada);
						box.setSubject(result[2]);
						box.setYear(result[4]);
						
						BaseDAO<BoxPOJO> dao = new BaseDAO<BoxPOJO>(BoxPOJO.class);
						dao.insert(box);
						i++;
					}
				}
				ler.close();
				arquivo.close();
			}
		}
		catch (IOException erro)
		{
			System.out.print("Erro 01 :" + erro.toString());
		}
		catch (SecurityException erro2)
		{
			System.out.print("Erro 02 :" + erro2.toString());
		}
		catch (PersistenceException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (BusinessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
