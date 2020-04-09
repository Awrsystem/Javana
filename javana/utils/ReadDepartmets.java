package br.com.javana.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.PersistenceException;
import br.com.javana.persistence.pojo.DepartmentPOJO;

public class ReadDepartmets
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		DepartmentPOJO department = new DepartmentPOJO();
		File arq = new File("F:/kelly/banco/setores.txt");
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
					
					String[] result = linha.split(";");
					if(!result[0].equals("") )
					{
						department.setName(result[0].trim());
						BaseDAO<DepartmentPOJO> dao = new BaseDAO<DepartmentPOJO>(DepartmentPOJO.class);
						dao.insert(department);
					}
					
					i++;
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

	}

}
