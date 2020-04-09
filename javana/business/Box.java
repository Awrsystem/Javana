package br.com.javana.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.BoxCountDTO;
import br.com.javana.dto.BoxDTO;
import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.BoxDAO;
import br.com.javana.persistence.dao.BoxSubprotocolDAO;
import br.com.javana.persistence.dao.PersistenceException;
import br.com.javana.persistence.pojo.BoxPOJO;
import br.com.javana.persistence.pojo.BoxSubProtocolPOJO;
import br.com.javana.utils.MessageLoader;

public class Box extends AbstractBaseBusiness<BoxDTO, BoxPOJO> {
	
	private BoxDAO dao;
	public Box(){
		super();
		this.dao = new BoxDAO(BoxPOJO.class);
	}
	
	@Override
	public List<BoxDTO> convertPojosToDTO(List<BoxPOJO> pojos) {
		List<BoxDTO> dtos = new ArrayList<BoxDTO>();

		for (BoxPOJO pojo : pojos) {
			BoxDTO dto = new BoxDTO();
			dto.setCode(pojo.getCode());
			dto.setDeptClient(BusinessDelegate.getInstance().getDeptClient().convertPojoToDTO(pojo.getDeptClient()));
			dto.setId(pojo.getId());
			dto.setMonth(pojo.getMonth());
			dto.setRegistrationDate(pojo.getRegistrationDate());
			dto.setSubject(pojo.getSubject());
			dto.setYear(pojo.getYear());
			dtos.add(dto);
		}

		return dtos;
	}

	@Override
	public BaseDAO<BoxPOJO> createDAO() {
		return new BoxDAO(BoxPOJO.class);
	}

	@Override
	public BoxPOJO createPOJO(BoxDTO dto) {
		BoxPOJO pojo = new BoxPOJO();
		pojo.setCode(dto.getCode());
		pojo.setDeptClient(BusinessDelegate.getInstance().getDeptClient().createPOJO(dto.getDeptClient()));
		pojo.setId(dto.getId());
		pojo.setMonth(dto.getMonth());
		pojo.setRegistrationDate(dto.getRegistrationDate());
		pojo.setSubject(dto.getSubject());
		pojo.setYear(dto.getYear());

		return pojo;
	}

	@Override
	protected boolean verifyExistence(BoxDTO dto) throws BusinessException {
		if (this.findByCode(dto.getCode()) != null) {
			return true;
		} else {
			return false;
		}
	}

	public BoxDTO findByCode(Long code) throws BusinessException {
		try {
			BoxPOJO pojo = dao.findByCode(code);

			if (pojo == null) {
				return null;
			} else {
				List<BoxPOJO> pojos = new ArrayList<BoxPOJO>();
				pojos.add(pojo);
				List<BoxDTO> dtos = this.convertPojosToDTO(pojos);

				return dtos.get(0);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public long pickANumber() throws BusinessException {
		long lastNumber;
		try {
			lastNumber = dao.pickNumber();
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		return ++lastNumber;
	}

	public List<BoxPOJO> getAllCodes() throws BusinessException {
		BoxPOJO box = new BoxPOJO();
		try {
			return dao.getAllCodes(box);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public List<BoxDTO> findByInterval(Long from, Long to) throws BusinessException {
		List<BoxPOJO> interval;
		try {
			interval = dao.findByInterval(from, to);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		return this.convertPojosToDTO(interval);
	}

	public List<BoxDTO> findBySet(List<Long> codes) throws BusinessException {
		List<BoxPOJO> interval;
		try {
			interval = dao.findByCodes(codes);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		return this.convertPojosToDTO(interval);
	}

	public BoxDTO convertPojoToDTO(BoxPOJO pojo) {
		BoxDTO dto = new BoxDTO();
		dto.setCode(pojo.getCode());
		dto.setDeptClient(BusinessDelegate.getInstance().getDeptClient().convertPojoToDTO(pojo.getDeptClient()));
		dto.setId(pojo.getId());
		dto.setMonth(pojo.getMonth());
		dto.setRegistrationDate(pojo.getRegistrationDate());
		dto.setSubject(pojo.getSubject());
		dto.setYear(pojo.getYear());

		return dto;
	}

	public List<BoxDTO> findBoxesByParams(Map<String, String> params) throws BusinessException {
		List<BoxPOJO> interval;
		try {
			interval = dao.findBoxesByParams(params);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		return this.convertPojosToDTO(interval);
	}

	public void deleteByBoxCodes(List<Long> codes) throws BusinessException{
		// excluir os boxsub
		List<BoxSubProtocolPOJO> boxsubs = BusinessDelegate.getInstance().getBoxSubprotocol().findByBoxCodes(codes);
		Set<Long> subIds = new HashSet<Long>();

		for (BoxSubProtocolPOJO pojo : boxsubs) {
			subIds.add(pojo.getSubprotocol().getId());
		}

		// exclui os box sub
		BoxSubprotocolDAO bsdao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		for (BoxSubProtocolPOJO pojo : boxsubs) {
			try {
				bsdao.delete(pojo);
			} catch (Exception e) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(43));
			}
		}

		// exclui box
		List<BoxPOJO> pojos;
		try {
			pojos = dao.findByCodes(codes);
		} catch (PersistenceException e1) {
			throw new BusinessException(e1.getMessage());
		}

		for (BoxPOJO pojo : pojos) {
			try {
				dao.delete(pojo);
			} catch (Exception e) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(44));
			}
		}
	}

	public List<BoxDTO> findByIntervalClientAvail(Long from, Long to, String client) throws BusinessException {
		List<BoxPOJO> pojos;
		try {
			pojos = dao.findByIntervalClientAvail(from, to, client);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		return this.convertPojosToDTO(pojos);
	}

	public List<BoxCountDTO> boxCountByClient() throws BusinessException {
		List<BoxCountDTO> dtos;
		try {
			dtos = dao.boxCountByClient();
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		HashMap<String, Integer> total = new HashMap<String, Integer>();
		ArrayList<BoxCountDTO> count = new ArrayList<BoxCountDTO>();

		for (BoxCountDTO dto : dtos) {
			if (!total.containsKey(dto.getClient())) {
				total.put(dto.getClient(), dto.getCount());
			} else {
				Integer i = total.get(dto.getClient());
				total.put(dto.getClient(), i + dto.getCount());
			}
		}

		for (String key : total.keySet()) {
			BoxCountDTO dto = new BoxCountDTO();
			dto.setClient(key);
			dto.setCount(total.get(key));
			count.add(dto);
		}
		Collections.sort(count);
		return count;
	}

	public List<BoxDTO> findByClientId(long id) throws BusinessException {
		List<BoxPOJO> interval;
		try {
			interval = dao.findByClientId(id);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		return this.convertPojosToDTO(interval);
	}

	public void checkClientIsOwner(Set<Long> allBoxes, String client) throws BusinessException {
		List<Long> interval;
		try {
			interval = dao.checkOwnership(allBoxes, client);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}

		String msg = "";

		if (interval.size() != allBoxes.size()) {
			// verifica quais as caixas que vieram estao faltando

			for (Long box : allBoxes) {
				if (!interval.contains(box))
					msg += box + ", ";
			}

			throw new BusinessException("As caixas " + msg + " não pertencem ao cliente selecionado.");
		}
	}

	public List<BoxDTO> findNLastBoxes(long n) throws BusinessException {
		List<BoxPOJO> boxes = null;
		try {
			boxes = dao.findAll();
			int s = (int) (boxes.size() - n);
			boxes = boxes.subList(s, boxes.size());
		} catch (Exception e) {
			throw new BusinessException("Erro ao buscar caixas!");
		}

		return this.convertPojosToDTO(boxes);
	}
}
