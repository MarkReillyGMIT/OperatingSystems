package ie.gmit.sw;

public class Player {

	String name, status, position, agentID, clubID;
	int age, playerID;
	double valuation;

	public Player(String name, int age, int playerID, String agentID, String clubID, String status, String position,
			double valuation) {
		super();
		this.name = name;
		this.playerID = playerID;
		this.age = age;
		this.agentID = agentID;
		this.clubID = clubID;
		this.status = status;
		this.position = position;
		this.valuation = valuation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public String getClubID() {
		return clubID;
	}

	public void setClubID(String clubID) {
		this.clubID = clubID;
	}

	public String getAgentID() {
		return agentID;
	}

	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public double getValuation() {
		return valuation;
	}

	public void setValuation(double valuation) {
		this.valuation = valuation;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", clubID=" + clubID + ", agentID=" + agentID + ", status=" + status
				+ ", position=" + position + ", age=" + age + ", playerID=" + playerID + ", valuation=" + valuation
				+ "]";
	}

}