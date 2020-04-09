package br.com.javana.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.PersistenceException;
import br.com.javana.persistence.pojo.ClientPOJO;

public class ReadClients
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		ClientPOJO client = new ClientPOJO();
		File arq = new File("F:/kelly/banco/Clientes.txt");
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
					
					//O código do cliente vai ser desprezado
					client.setName(result[1]);
					client.setPhone(result[2]);
					client.setStreet(result[3]);
					client.setNumber(Integer.parseInt(result[4]));
					client.setBairro(result[5]);
					client.setCity(result[6]);
					
					if(result.length > 7)
					{
						client.setZipcode(result[7]);
					}
					if(result.length > 8)
					{
						client.setEmail(result[8]);
					}
					
					
					BaseDAO<ClientPOJO> dao = new BaseDAO<ClientPOJO>(ClientPOJO.class);
					dao.insert(client);
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
