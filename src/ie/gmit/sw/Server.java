package ie.gmit.sw;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

//test
public class Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ServerSocket listener;
		int clientid = 0;
		try {
			listener = new ServerSocket(10000, 10);

			while (true) {
				System.out.println("Main thread listening for incoming new connections");
				Socket newconnection = listener.accept();

				System.out.println("New connection received and spanning a thread");
				Connecthandler t = new Connecthandler(newconnection, clientid);
				clientid++;
				t.start();
			}

		}

		catch (IOException e) {
			System.out.println("Socket not opened");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class Connecthandler extends Thread {

	Socket individualconnection;
	int socketid;
	ObjectOutputStream out;
	ObjectInputStream in;
	String loginOrRegister;
	int age, statusInt, positionInt;
	String message, userInput;
	String inputClubName, inputClubID;
	String inputAgentName, inputAgentID;
	String username, password, status, position;
	String clubName, clubID, clubEmail;
	String name, id, agentName, agentID, agentEmail;
	double fundsAvailable, valuation;
	int result, agentOption, idCheck, clubOption;
	boolean loggedIn, registerSuccessful;

	ArrayList<Integer> playerIDs = new ArrayList<Integer>();
	ArrayList<String> clubIDs = new ArrayList<String>();
	ArrayList<String> clubNames = new ArrayList<String>();
	ArrayList<Club> clubs = new ArrayList<Club>();
	ArrayList<String> agentIDs = new ArrayList<String>();
	ArrayList<String> agentNames = new ArrayList<String>();
	ArrayList<Player> players = new ArrayList<Player>();

	public Connecthandler(Socket s, int i) {
		individualconnection = s;
		socketid = i;
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	void sendMessage(boolean msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	void sendMessage(int msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public void run() {

		try {

			out = new ObjectOutputStream(individualconnection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(individualconnection.getInputStream());
			System.out.println("Connection" + socketid + " from IP address " + individualconnection.getInetAddress());

			Scanner agentLogin = new Scanner(new File("src/agentLogin"));
			Scanner clubLogin = new Scanner(new File("src/clubLogin"));
			Scanner p = new Scanner(new File("src/players"));

			// Populate Agent Login arrayList
			while (agentLogin.hasNext()) {
				agentNames.add(agentLogin.next());
				agentIDs.add(agentLogin.next());
			}
			// Populate Club Login arrayList
			while (clubLogin.hasNext()) {
				clubNames.add(clubLogin.next());
				clubIDs.add(clubLogin.next());
			}
			// Populate Player arrayList
			while (p.hasNext()) {
				players.add(new Player(p.next(), p.nextInt(), p.nextInt(), p.next(), p.next(), p.next(), p.next(),
						p.nextDouble()));
			}

			loginOrRegister = loginOrRegister();

			// login
			if (loginOrRegister.equals("1")) {
				userInput = agentOrClub();

				login(userInput);
			}

			// register
			else if (loginOrRegister.equals("2")) {
				userInput = agentOrClub();

				register(userInput);
			}
			do {
				if (loggedIn) {

					if (userInput.equals("1")) {
						sendMessage("In club menu.");
						clubOption = clubMenu();
						sendMessage(clubOption);
						if (clubOption == 1) {
							sendMessage("Not working");
						} else if (clubOption == 2) {
							sendMessage("Not working");
						} else if (clubOption == 3) {
							sendMessage("Not working");
						} else if (clubOption == 4) {
							sendMessage("Not working");
						}

					} else if (userInput.equals("2")) {
						sendMessage("In agent menu.");
						agentOption = agentMenu();
						sendMessage(agentOption);

						if (agentOption == 1) {
							addPlayer();
						} else if (agentOption == 2) {
							updatePlayerValue();
						} else if (agentOption == 3) {
							updatePlayerStatus();
						}
					}

				}
				agentLogin.close();
				clubLogin.close();
				p.close();
			} while (agentOption != 4);
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			try {
				out.close();
				in.close();
				individualconnection.close();
			}

			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public String loginOrRegister() throws ClassNotFoundException, IOException {
		String option;
		do {
			sendMessage("Enter 1 to Login or 2 to Register");
			option = (String) in.readObject();
		} while (!option.equals("1") && !option.equals("2"));

		return option;
	}

	public String agentOrClub() throws ClassNotFoundException, IOException {
		String optionAorC;

		do {
			sendMessage("Enter 1 for club \n2 for agent.");
			optionAorC = (String) in.readObject();
		} while (!optionAorC.equals("1") && !optionAorC.equals("2"));

		return optionAorC;
	}

	public void login(String check) throws ClassNotFoundException, IOException {
		loggedIn = false;
		do {
			if (check.equals("1")) {
				sendMessage("Enter your Club Name:");
				name = (String) in.readObject();
				sendMessage("Enter your Club ID:");
				id = (String) in.readObject();
			} else if (check.equals("2")) {
				sendMessage("Enter your name:");
				name = (String) in.readObject();
				sendMessage("Enter your ID:");
				id = (String) in.readObject();
			}

			if (check.equals("1")) {
				for (int i = 0; i < clubNames.size(); i++) {
					if (name.equals(clubNames.get(i)) && id.equals(clubIDs.get(i))) {
						loggedIn = true;
					}
				}
			} else if (check.equals("2")) {
				for (int k = 0; k < agentNames.size(); k++) {
					if (name.equals(agentNames.get(k)) && id.equals(agentIDs.get(k))) {
						loggedIn = true;
					}
				}
			}

			sendMessage(loggedIn);
		} while (loggedIn == false);
	}

	public void register(String check) throws ClassNotFoundException, IOException {

		FileWriter fra = new FileWriter("src/agentLogin", true);
		BufferedWriter bra = new BufferedWriter(fra);
		FileWriter frc = new FileWriter("src/clubLogin", true);
		BufferedWriter brc = new BufferedWriter(frc);
		registerSuccessful = false;

		do {
			// Club
			if (check.equals("1")) {
				clubRegisterMenu();
			}
			// Agent
			else if (check.equals("2")) {
				agentRegisterMenu();
			}

			registerSuccessful = checkRegister(name, id, check);
			sendMessage(registerSuccessful);

			if (registerSuccessful == false) {
				sendMessage("Name or ID is already in use.");
			}

		} while (registerSuccessful == false);

		if (check.equals("1")) {
			clubNames.add(name);
			clubIDs.add(id);
			brc.write("\n" + name + "\n" + id);
		} else if (check.equals("2")) {
			agentNames.add(name);
			agentIDs.add(id);
			bra.write("\n" + name + "\n" + id);
		}

		bra.close();
		brc.close();
		fra.close();
		frc.close();

		sendMessage("Registration Successful");

		if (check.equals("1")) {
			sendMessage("Name: " + name + "\nID: " + id + "\nEmail: " + clubEmail + "\nFunds Available for transfer: "
					+ fundsAvailable);
			addClub(id, name, clubEmail, fundsAvailable);
		} else if (check.equals("2")) {
			sendMessage("Name: " + name + "\nID: " + id + "\nEmail: " + agentEmail);
		}

		login(check);
	}

	public void clubRegisterMenu() throws ClassNotFoundException, IOException {
		sendMessage("Enter your club name:");
		name = (String) in.readObject();
		sendMessage("Enter your club ID:");
		id = (String) in.readObject();
		sendMessage("Enter your club email:");
		clubEmail = (String) in.readObject();
		sendMessage("Enter your available funds:");
		fundsAvailable = (double) in.readObject();

		Club club = new Club(id, name, clubEmail, fundsAvailable);
		clubs.add(club);
	}

	public void agentRegisterMenu() throws ClassNotFoundException, IOException {
		sendMessage("Enter your agent name:");
		name = (String) in.readObject();
		sendMessage("Enter your agent ID:");
		id = (String) in.readObject();
		sendMessage("Enter your agent email:");
		agentEmail = (String) in.readObject();
	}

	private void addClub(String id, String name, String email, double funds) {

		// Club club = new Club(id, name, email, funds);

		// clubs.add(new Club(id, name, email, funds));

	}

	public boolean checkRegister(String name, String id, String check) {
		boolean exclusive = true;

		if (check.equals("1")) {
			for (int i = 0; i < clubNames.size(); i++) {
				if (name.equals(clubNames.get(i))) {
					exclusive = false;
				}
			}

			for (int i = 0; i < clubIDs.size(); i++) {
				if (id.equals(clubIDs.get(i))) {
					exclusive = false;
				}
			}
		} else if (check.equals("2")) {
			for (int i = 0; i < agentNames.size(); i++) {
				if (name.equals(agentNames.get(i))) {
					exclusive = false;
				}
			}

			for (int i = 0; i < agentIDs.size(); i++) {
				if (id.equals(agentIDs.get(i))) {
					exclusive = false;
				}
			}
		}
		return exclusive;

	}

	public int clubMenu() throws ClassNotFoundException, IOException {
		int option;
		sendMessage("Would you like to: \n1. Search for all Players \n2.Search for all players for sale in their club "
				+ "\n3.Suspend/Resume the sale of a player in their club \n4.Purchase a player");
		option = (int) in.readObject();
		return option;

	}

	public int agentMenu() throws ClassNotFoundException, IOException {
		int option;
		sendMessage(
				"Would you like to: \n1. Add a player \n2. Update a players valuation \n3. Update the players status"
						+ "\n4.Exit");
		option = (int) in.readObject();

		return option;
	}

	public void addPlayer() throws ClassNotFoundException, IOException {
		FileWriter fr = new FileWriter("src/players", true);
		BufferedWriter br = new BufferedWriter(fr);

		int pid = players.size() + 1;
		playerIDs.add(pid);
		sendMessage("Enter your player name:");
		name = (String) in.readObject();
		sendMessage("Enter your player age:");
		age = (int) in.readObject();
		sendMessage("Enter your player agentID:");
		agentID = (String) in.readObject();
		sendMessage("Enter player clubID:");
		clubID = (String) in.readObject();
		sendMessage("Enter your player Valuation:");
		valuation = (double) in.readObject();
		sendMessage("Enter your player Status: \n 1.For Sale \n 2.Sold \n 3.Sale Suspended ");
		statusInt = ((int) in.readObject());
		if (statusInt == 1) {
			status = ("ForSale");
		} else if (statusInt == 2) {
			status = ("Sold");
		} else if (statusInt == 3) {
			status = ("SaleSuspended");
		}
		sendMessage("Enter your player Position: \n 1.Goalkeeper \n 2.Defender \n 3.Midfield \n 4.Forward ");
		positionInt = ((int) in.readObject());
		if (positionInt == 1) {
			position = ("Goalkeeper");
		} else if (positionInt == 2) {
			position = ("Defender");
		} else if (positionInt == 3) {
			position = ("Midfield");
		} else if (positionInt == 4) {
			position = ("Forward");
		}

		Player player = new Player(name, age, pid, agentID, clubID, status, position, valuation);
		// add player
		players.add(player);

		br.write("\n" + player.name + "\n" + player.age + "\n" + player.playerID + "\n" + player.agentID + "\n"
				+ player.clubID + "\n" + player.status + "\n" + player.position + "\n" + player.valuation);

		sendMessage(player.name + " " + player.playerID + " " + player.status + " " + player.valuation);

		br.close();
		fr.close();
	}

	public void updatePlayerValue() throws ClassNotFoundException, IOException {
		int inputPlayerID;
		double newValuation;
		boolean found = false;
		int holder = 0;

		FileWriter fr = new FileWriter("src/players", false);
		BufferedWriter br = new BufferedWriter(fr);

		sendMessage("Enter the ID of the Player: ");
		inputPlayerID = (int) in.readObject();
		sendMessage("Enter the new valuation for the Player: ");
		newValuation = (double) in.readObject();

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).playerID == inputPlayerID) {
				players.get(i).valuation = newValuation;
				found = true;
				holder = i;
			}
		}

		sendMessage(found);

		if (found) {
			sendMessage("\n" + players.get(holder).name + " new valuation is " + players.get(holder).valuation + "\n");
		}

		// Update the file with new valuation
		for (int i = 0; i < players.size(); i++) {
			br.write("\n" + players.get(i).name + "\n" + players.get(i).age + "\n" + players.get(i).playerID + "\n"
					+ players.get(i).agentID + "\n" + players.get(i).clubID + "\n" + players.get(i).status + "\n"
					+ players.get(i).position + "\n" + players.get(i).valuation);
		}

		br.close();
		fr.close();
	}

	public void updatePlayerStatus() throws ClassNotFoundException, IOException {
		int inputPlayerID;
		int newStatus;
		boolean found = false;
		int holder = 0;

		FileWriter fr = new FileWriter("src/players", false);
		BufferedWriter br = new BufferedWriter(fr);

		sendMessage("Enter the id of the player: ");
		inputPlayerID = (int) in.readObject();
		sendMessage("Enter the new status for the player: \n 1. For Sale \n 2.Sold \n 3.Sale Suspended ");
		newStatus = (int) in.readObject();
		if (newStatus == 1) {
			status = ("ForSale");
		} else if (newStatus == 2) {
			status = ("Sold");
		} else if (newStatus == 3) {
			status = ("SaleSuspended");
		}

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).playerID == inputPlayerID) {
				players.get(i).status = status;
				found = true;
				holder = i;
			}
		}

		sendMessage(found);

		if (found) {
			sendMessage("\n" + players.get(holder).name + "\n new status is " + players.get(holder).status + "\n");
		}

		// overwrite file with updated info
		for (int i = 0; i < players.size(); i++) {
			br.write("\n" + players.get(i).name + "\n" + players.get(i).age + "\n" + players.get(i).playerID + "\n"
					+ players.get(i).agentID + "\n" + players.get(i).clubID + "\n" + players.get(i).status + "\n"
					+ players.get(i).position + "\n" + players.get(i).valuation);
		}

		br.close();
		fr.close();
	}
}