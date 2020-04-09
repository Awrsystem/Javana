package br.com.javana.dto;

public class BoxCountDTO extends BaseDTO implements Comparable<BoxCountDTO> {
	private String client;
	private Integer count;

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public int compareTo(BoxCountDTO dto) {
		
		return this.client.compareTo(dto.client);
	}
}
