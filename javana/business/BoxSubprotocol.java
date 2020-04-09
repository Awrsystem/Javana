package br.com.javana.business;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import br.com.javana.business.BusinessException.BusinessException;
import br.com.javana.dto.BaseDTO;
import br.com.javana.dto.BoxProtocolDTO;
import br.com.javana.dto.BoxSubprotocolDTO;
import br.com.javana.dto.ProtocolPrintDTO;
import br.com.javana.persistence.dao.BaseDAO;
import br.com.javana.persistence.dao.BoxSubprotocolDAO;
import br.com.javana.persistence.dao.PersistenceException;
import br.com.javana.persistence.pojo.BoxPOJO;
import br.com.javana.persistence.pojo.BoxSubProtocolPOJO;
import br.com.javana.persistence.pojo.UserPOJO;
import br.com.javana.utils.MessageLoader;

public class BoxSubprotocol extends AbstractBaseBusiness<BoxSubprotocolDTO, BoxSubProtocolPOJO> {
	@Override
	public List<BoxSubprotocolDTO> convertPojosToDTO(List<BoxSubProtocolPOJO> pojos) {
		List<BoxSubprotocolDTO> all = new ArrayList<BoxSubprotocolDTO>();

		for (BoxSubProtocolPOJO pojo : pojos) {
			BoxSubprotocolDTO dto = new BoxSubprotocolDTO();
			dto.setBackDate(pojo.getBackDate());
			dto.setBox(BusinessDelegate.getInstance().getBox().convertPojoToDTO(pojo.getBox()));
			dto.setId(pojo.getId());
			dto.setSubprotocol(BusinessDelegate.getInstance().getSubprotocol().convertPojoToDTO(pojo.getSubprotocol()));

			all.add(dto);
		}

		return all;
	}

	@Override
	public BaseDAO<BoxSubProtocolPOJO> createDAO() {
		return new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
	}

	@Override
	public BoxSubProtocolPOJO createPOJO(BoxSubprotocolDTO dto) {
		BoxSubProtocolPOJO pojo = new BoxSubProtocolPOJO();
		pojo.setBackDate(dto.getBackDate());
		pojo.setBox(BusinessDelegate.getInstance().getBox().createPOJO(dto.getBox()));
		pojo.setId(dto.getId());
		pojo.setSubprotocol(BusinessDelegate.getInstance().getSubprotocol().createPOJO(dto.getSubprotocol()));

		return pojo;
	}

	@Override
	protected boolean verifyExistence(BoxSubprotocolDTO dto) throws BusinessException {
		if (this.findByBoxAndSubProtocolId(dto.getBox().getId(), dto.getSubprotocol().getId()) != null) {
			return true;
		} else {
			return false;
		}
	}

	private BoxSubprotocolDTO findByBoxAndSubProtocolId(long boxId, long subId) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		BoxSubProtocolPOJO pojo;
		try {
			pojo = dao.findByBoxAndSubId(boxId, subId);
			if (pojo == null) {
				return null;
			} else {
				List<BoxSubProtocolPOJO> pojos = new ArrayList<BoxSubProtocolPOJO>();
				pojos.add(pojo);
				List<BoxSubprotocolDTO> dtos = this.convertPojosToDTO(pojos);
				return dtos.get(0);
			}
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public List<Long> checkForUnavailableBoxes(List<Long> boxes) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		List<BoxSubProtocolPOJO> all;
		try {
			all = dao.checkForUnavailableBoxes(boxes);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		List<Long> unavailable = new ArrayList<Long>();

		for (BoxSubProtocolPOJO pojo : all) {
			unavailable.add(pojo.getBox().getCode());
		}
		return unavailable;
	}

	public List<Long> findBoxesByProtocol(long number) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		List<BoxSubProtocolPOJO> all;
		try {
			all = dao.findBoxesByProtocol(number);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}

		List<Long> codes = new ArrayList<Long>();

		for (BoxSubProtocolPOJO pojo : all) {
			codes.add(pojo.getBox().getCode());
		}

		return codes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.javana.business.IBoxSubprotocol#findProtocol(long) Retorna
	 *      um dto pra cada solicitante. As caixas do solicitante sao agrupadas
	 *      de 5 em 5
	 */
	public List<ProtocolPrintDTO> findProtocol(long number) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		List<BoxSubProtocolPOJO> all;
		try {
			all = dao.findBoxesByProtocol(number);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}

		Set<UserPOJO> requesters = new TreeSet<UserPOJO>(new Comparator<UserPOJO>() {

			public int compare(UserPOJO o1, UserPOJO o2) {
				int comp = o1.getDeptClient().getDepartment().getName().compareTo(
						o2.getDeptClient().getDepartment().getName());
				if (comp == 0) {
					comp = o1.getName().compareToIgnoreCase(o2.getName());
				}

				return comp;
			}

		});
		List<ProtocolPrintDTO> protocols = new ArrayList<ProtocolPrintDTO>();

		Timestamp date = all.get(0).getSubprotocol().getProtocol().getLeaveDate();
		for (BoxSubProtocolPOJO pojo : all) {
			requesters.add(pojo.getSubprotocol().getRequester());
		}

		for (UserPOJO user : requesters) {
			List<BoxPOJO> boxes = new ArrayList<BoxPOJO>();
			for (BoxSubProtocolPOJO pojo : all) {
				if (pojo.getSubprotocol().getRequester().getId() == user.getId()) {
					boxes.add(pojo.getBox());
				}
			}

			// tem todas as caixas de um usuario
			ProtocolPrintDTO dto = new ProtocolPrintDTO();
			dto.setRequester(user.getName());

			for (int i = 0; i < boxes.size(); i++) {
				if (i == 0) {
					dto.setBoxes((boxes.get(i)).getCode() + "");
					dto.setClient(user.getDeptClient().getClient().getName());
					dto.setProtocolNumber(number);
				} else {
					dto.setBoxes(dto.getBoxes() + " - " + boxes.get(i).getCode());
				}
			}
			dto.setBoxCount(boxes.size());
			dto.setDepartment(user.getDeptClient().getDepartment().getName());
			dto.setDate(date);
			protocols.add(dto);
		}

		return protocols;
	}

	public BoxSubprotocolDTO findByNumberAndFullyOpen(Long value) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		BoxSubProtocolPOJO pojo;
		try {
			pojo = dao.findByNumberFullyOpen(value);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		return this.convertPojoToDTO(pojo);
	}

	private BoxSubprotocolDTO convertPojoToDTO(BoxSubProtocolPOJO pojo) {
		BoxSubprotocolDTO dto = new BoxSubprotocolDTO();
		dto.setBackDate(pojo.getBackDate());
		dto.setBox(BusinessDelegate.getInstance().getBox().convertPojoToDTO(pojo.getBox()));
		dto.setId(pojo.getId());
		dto.setSubprotocol(BusinessDelegate.getInstance().getSubprotocol().convertPojoToDTO(pojo.getSubprotocol()));
		return dto;
	}

	public List<BoxSubprotocolDTO> findBoxesBySubId(long subprotocolId) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		List<BoxSubProtocolPOJO> pojos;
		try {
			pojos = dao.findBoxesBySubId(subprotocolId);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		return this.convertPojosToDTO(pojos);
	}

	public BoxSubProtocolPOJO findByBoxAndSubId(Long code, long id) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		try {
			return dao.findByBoxAndSubId(code, id);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public BoxSubProtocolPOJO findByBoxCodeAndSubId(Long code, long id) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		try {
			return dao.findByBoxCodeAndSubId(code, id);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public List<Long> findBoxCodesBySubProtocolId(long id) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		List<BoxSubProtocolPOJO> pojos;
		try {
			pojos = dao.findBoxesCodesBySubId(id);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}

		List<Long> result = new ArrayList<Long>();
		for (BoxSubProtocolPOJO pojo : pojos) {
			result.add(pojo.getBox().getCode());
		}

		return result;
	}

	public void checkInBoxes(List<Long> codes) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		for (Long l : codes) {
			BoxSubProtocolPOJO pojo;
			try {
				pojo = dao.checkForUnavailableBoxesByCode(l);
			} catch (PersistenceException e1) {
				throw new BusinessException(e1.getMessage());
			}
			if (pojo == null) {
				throw new BusinessException(MessageLoader.getInstance().getMessage(1, l + ""));
			} else {
				pojo.setBackDate(new Timestamp(System.currentTimeMillis()));
				try {
					dao.update(pojo);
				} catch (Exception e) {
					throw new BusinessException(MessageLoader.getInstance().getMessage(2));
				}
			}
		}
	}

	public List<BoxProtocolDTO> findUnavailableBoxesByInterval(Long from, Long to) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		List<BoxSubProtocolPOJO> pojos;
		try {
			pojos = dao.checkForUnavailableBoxesByInterval(from, to);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}

		List<BoxProtocolDTO> boxes = new ArrayList<BoxProtocolDTO>();

		for (BoxSubProtocolPOJO pojo : pojos) {
			BoxProtocolDTO dto = new BoxProtocolDTO();
			dto.setBoxCode(pojo.getBox().getCode());
			dto.setProtocol(pojo.getSubprotocol().getProtocol().getNumber());
			dto.setRequester(pojo.getSubprotocol().getRequester().getName());
			boxes.add(dto);
		}

		return boxes;
	}

	public List<BoxProtocolDTO> findUnavailableBoxes(List<Long> codes) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		List<BoxSubProtocolPOJO> pojos;
		try {
			pojos = dao.checkForUnavailableBoxes(codes);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}

		List<BoxProtocolDTO> boxes = new ArrayList<BoxProtocolDTO>();

		for (BoxSubProtocolPOJO pojo : pojos) {
			BoxProtocolDTO dto = new BoxProtocolDTO();
			dto.setBoxCode(pojo.getBox().getCode());
			dto.setProtocol(pojo.getSubprotocol().getProtocol().getNumber());
			dto.setRequester(pojo.getSubprotocol().getRequester().getName());
			boxes.add(dto);
		}

		return boxes;
	}

	public List<? extends BaseDTO> findUnavailableBoxes(Map<String, String> params) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);

		List<BoxSubProtocolPOJO> pojos;
		try {
			pojos = dao.findByParams(params);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}

		return this.convertPojosToDTO(pojos);
	}

	public boolean findOpenProtocolsByClient(Long clientId) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		boolean isOpen;
		try {
			isOpen = dao.findOpenProtocolsByClient(clientId);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}

		return isOpen;
	}

	public List<BoxSubProtocolPOJO> findByClientId(long id) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		try {
			return dao.findByClientId(id);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public BoxSubprotocolDTO convertPojosToDTO(BoxSubProtocolPOJO pojo) {
		BoxSubprotocolDTO dto = new BoxSubprotocolDTO();
		dto.setBackDate(pojo.getBackDate());
		dto.setBox(BusinessDelegate.getInstance().getBox().convertPojoToDTO(pojo.getBox()));
		dto.setId(pojo.getId());
		dto.setSubprotocol(BusinessDelegate.getInstance().getSubprotocol().convertPojoToDTO(pojo.getSubprotocol()));

		return dto;
	}

	public BoxProtocolDTO findUnavailableBoxesByCode(Long value) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		BoxSubProtocolPOJO pojo;
		try {
			pojo = dao.checkForUnavailableBoxesByCode(value);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}

		if (pojo == null) {
			return null;
		} else {
			BoxProtocolDTO dto = new BoxProtocolDTO();
			dto.setBoxCode(pojo.getBox().getCode());
			dto.setProtocol(pojo.getSubprotocol().getProtocol().getNumber());
			dto.setRequester(pojo.getSubprotocol().getRequester().getName());

			return dto;
		}

	}

	public List<BoxProtocolDTO> findUnavailableBoxesProtocol(List<Long> values) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		List<BoxSubProtocolPOJO> pojos;
		try {
			pojos = dao.checkForUnavailableBoxes(values);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}

		List<BoxProtocolDTO> dtos = new ArrayList<BoxProtocolDTO>();

		for (BoxSubProtocolPOJO pojo : pojos) {
			BoxProtocolDTO dto = new BoxProtocolDTO();
			dto.setBoxCode(pojo.getBox().getCode());
			dto.setProtocol(pojo.getSubprotocol().getProtocol().getNumber());
			dto.setRequester(pojo.getSubprotocol().getRequester().getName());
			dtos.add(dto);
		}

		return dtos;
	}

	public List<BoxSubprotocolDTO> boxHistory(Long boxCode) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		try {
			return this.convertPojosToDTO(dao.boxHistory(boxCode));
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public List<BoxSubProtocolPOJO> findByBoxCodes(List<Long> codes) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		try {
			return dao.findByBoxCodes(codes);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}

	}

	@SuppressWarnings("deprecation")
	public List<? extends BaseDTO> backBetweenDates(String from, String to) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat formatHour = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date f = format.parse(from);
			Date t = format.parse(to);
			t.setHours(23);
			t.setMinutes(59);
			t.setSeconds(59);
			List<Object[]> pojos = dao.findBackBetweenDates(formatHour.format(f), formatHour.format(t));

			List<BoxProtocolDTO> dtos = new ArrayList<BoxProtocolDTO>();

			for (Object[] pojo : pojos) {
				BoxProtocolDTO dto = new BoxProtocolDTO();
				dto.setBoxCode((Long) pojo[0]);
				dto.setProtocol((Long) pojo[1]);
				dto.setRequester((String) pojo[2]);
				dtos.add(dto);
			}

			return dtos;
		} catch (ParseException e) {
			throw new BusinessException(MessageLoader.getInstance().getMessage(48));
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public List<Long> checkBoxes(List<Long> boxes) throws BusinessException {
		BoxSubprotocolDAO dao = new BoxSubprotocolDAO(BoxSubProtocolPOJO.class);
		List<BoxSubProtocolPOJO> all;
		try {
			all = dao.checkBoxesAreAvailable(boxes);
		} catch (PersistenceException e) {
			throw new BusinessException(e.getMessage());
		}
		List<Long> available = new ArrayList<Long>();

		for (BoxSubProtocolPOJO pojo : all) {
			available.add(pojo.getBox().getId());
		}
		return available;
	}
}
