package br.com.javana.business;

/**
 * @author Rafael Classe delegate para a camada de negocio. Instancia os objetos
 *         de negocio responsaveis pelas funcionalidades
 */
public class BusinessDelegate implements IBusinessDelegate {
	private static BusinessDelegate instance = new BusinessDelegate();

	private Client client = new Client();

	private Department department = new Department();

	private DepartmentClient deptClient = new DepartmentClient();

	private Box box = new Box();

	private LostBoxNumbers lostBox = new LostBoxNumbers();

	private Protocol protocol = new Protocol();

	private BoxSubprotocol boxSubprotocol = new BoxSubprotocol();

	private RemovedBox removedBox = new RemovedBox();

	private SubProtocol subprotocol = new SubProtocol();

	private User user = new User();

	private LostProtocolNumber lostProtocol = new LostProtocolNumber();

	public BusinessDelegate() {
		super();
	}

	public static BusinessDelegate getInstance() {
		return instance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public DepartmentClient getDeptClient() {
		return deptClient;
	}

	public void setDeptClient(DepartmentClient deptClient) {
		this.deptClient = deptClient;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public LostBoxNumbers getLostBox() {
		return lostBox;
	}

	public void setLostBox(LostBoxNumbers lost) {
		this.lostBox = lost;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public BoxSubprotocol getBoxSubprotocol() {
		return boxSubprotocol;
	}

	public void setBoxSubprotocol(BoxSubprotocol boxSubprotocol) {
		this.boxSubprotocol = boxSubprotocol;
	}

	public RemovedBox getRemovedBox() {
		return removedBox;
	}

	public void setRemovedBox(RemovedBox removedBox) {
		this.removedBox = removedBox;
	}

	public SubProtocol getSubprotocol() {
		return subprotocol;
	}

	public void setSubprotocol(SubProtocol subprotocol) {
		this.subprotocol = subprotocol;
	}

	public LostProtocolNumber getLostProtocol() {
		return lostProtocol;
	}

	public void setLostProtocol(LostProtocolNumber lostProtocol) {
		this.lostProtocol = lostProtocol;
	}
}
