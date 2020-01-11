package ie.gmit.sw;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;

public class Client {
	private Socket connection;
	private String message;
	private String loginOrRegister;
	private Scanner console;
	private String ipaddress;
	private int portaddress;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String clubName, clubEmail;
	private double fundsAvailable, valuation;
	private String agentName, agentId, agentEmail;
	private String userInput, name, agentIDs, clubIDs;
	private boolean loggedIn;
	private int agentOption, position, status, age, clubOption;

	public Client() {
		console = new Scanner(System.in);

		System.out.println("Enter the IP Address of the Server");
		ipaddress = console.nextLine();

		System.out.println("Enter the TCP Port");
		portaddress = console.nextInt();

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

	void sendMessage(double msg) {
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

	public static void main(String[] args) {
		Client temp = new Client();
		temp.clientapp();
	}

	public void clientapp() {

		try {
			connection = new Socket(ipaddress, portaddress);

			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			System.out.println("Client Side ready to communicate");

			message = (String) in.readObject();
			System.out.println(message);
			loginOrRegister = loginOrRegister();
			sendMessage(loginOrRegister);
			// Login
			if (loginOrRegister.equals("1")) {
				message = (String) in.readObject();
				System.out.println(message);
				userInput = agentOrClub();
				sendMessage(userInput);

				login(userInput);
			}
			// Register
			else if (loginOrRegister.equals("2")) {
				message = (String) in.readObject();
				System.out.println(message);
				userInput = agentOrClub();
				sendMessage(userInput);

				register(userInput);
			}
			do {
				message = (String) in.readObject();
				System.out.println(message);

				if (userInput.equals("1")) // club
				{
					clubMenu();
					clubOption = (int) in.readObject();

					if (clubOption == 1) {
						message = (String) in.readObject();
						System.out.println(message);
					} else if (clubOption == 2) {
						message = (String) in.readObject();
						System.out.println(message);
					} else if (clubOption == 3) {
						message = (String) in.readObject();
						System.out.println(message);
					} else if (clubOption == 4) {
						message = (String) in.readObject();
						System.out.println(message);
					}
				} else if (userInput.equals("2")) // agent
				{
					agentMenu();
					agentOption = (int) in.readObject();

				}
				if (agentOption == 1) {
					// Add Player
					addPlayer();
					message = (String) in.readObject();
					System.out.println(message);
				} else if (agentOption == 2) {
					// Update player
					updatePlayerValue();
				} else if (agentOption == 3) {
					// Update player status
					updatePlayerStatus();
				}
			} while (agentOption != 4 || clubOption != 5);

			out.close();
			in.close();
			connection.close();

		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	String agentOrClub() throws ClassNotFoundException, IOException {
		String optionAorC;

		do {
			optionAorC = console.next();
		} while (!optionAorC.equalsIgnoreCase("1") && !optionAorC.equalsIgnoreCase("2"));

		return optionAorC;
	}

	String loginOrRegister() throws ClassNotFoundException, IOException {
		String option;

		do {
			option = console.next();
		} while (!option.equalsIgnoreCase("1") && !option.equalsIgnoreCase("2"));

		return option;
	}

	void login(String check) throws ClassNotFoundException, IOException {
		loggedIn = false;

		do {
			if (check.equals("1")) {
				// Club name
				message = (String) in.readObject();
				System.out.println(message);
				clubName = console.next();
				sendMessage(clubName);
				// Club id
				message = (String) in.readObject();
				System.out.println(message);
				clubIDs = console.next();
				sendMessage(clubIDs);
			} else if (check.equals("2")) {
				// Agent name
				message = (String) in.readObject();
				System.out.println(message);
				agentName = console.next();
				sendMessage(agentName);
				// Agent id
				message = (String) in.readObject();
				System.out.println(message);
				agentId = console.next();
				sendMessage(agentId);

			}
			loggedIn = (boolean) in.readObject();
			System.out.println("Logged In: " + loggedIn);

		} while (loggedIn == false);

	}

	void register(String check) throws ClassNotFoundException, IOException {

		boolean registerSuccessful = true;

		do {
			// Club
			if (check.equals("1")) {
				clubRegisterMenu();
			}
			// Agent
			else if (check.equals("2")) {
				agentRegisterMenu();
			}

			registerSuccessful = (boolean) in.readObject();
			System.out.println(registerSuccessful);

			if (registerSuccessful == false) {
				message = (String) in.readObject();
				System.out.println(message);
			}
		} while (registerSuccessful == false);

		message = (String) in.readObject();
		System.out.println(message);

		message = (String) in.readObject();
		System.out.println(message);

		login(check);
	}

	// Club Register method
	void clubRegisterMenu() throws ClassNotFoundException, IOException {
		// Club name
		message = (String) in.readObject();
		System.out.println(message);
		clubName = console.next();
		sendMessage(clubName);
		// Club id
		message = (String) in.readObject();
		System.out.println(message);
		clubIDs = console.next();
		sendMessage(clubIDs);
		// Club email
		message = (String) in.readObject();
		System.out.println(message);
		clubEmail = console.next();
		sendMessage(clubEmail);
		// Funds Available
		message = (String) in.readObject();
		System.out.println(message);
		fundsAvailable = console.nextDouble();
		sendMessage(fundsAvailable);
	}

	// Agent Register method
	void agentRegisterMenu() throws ClassNotFoundException, IOException {
		// Agent name
		message = (String) in.readObject();
		System.out.println(message);
		agentName = console.next();
		sendMessage(agentName);
		// Agent id
		message = (String) in.readObject();
		System.out.println(message);
		agentId = console.next();
		sendMessage(agentId);
		// Agent email
		message = (String) in.readObject();
		System.out.println(message);
		agentEmail = console.next();
		sendMessage(agentEmail);
	}

	// Club Menu method
	void clubMenu() throws ClassNotFoundException, IOException {
		int option;
		message = (String) in.readObject();
		System.out.println(message);

		option = console.nextInt();
		sendMessage(option);
	}

	// Agent Menu method
	void agentMenu() throws ClassNotFoundException, IOException {
		int option;
		message = (String) in.readObject();
		System.out.println(message);

		option = console.nextInt();
		sendMessage(option);
	}

	// Add player method
	void addPlayer() throws ClassNotFoundException, IOException {
		// Player name
		message = (String) in.readObject();
		System.out.println(message);
		name = console.next();
		sendMessage(name);

		// Player age
		message = (String) in.readObject();
		System.out.println(message);
		age = console.nextInt();
		sendMessage(age);

		// Player agentID
		message = (String) in.readObject();
		System.out.println(message);
		agentIDs = console.next();
		sendMessage(agentIDs);

		// Player clubID
		message = (String) in.readObject();
		System.out.println(message);
		clubIDs = console.next();
		sendMessage(clubIDs);

		// Player valuation
		message = (String) in.readObject();
		System.out.println(message);
		valuation = console.nextDouble();
		sendMessage(valuation);

		// Player status
		message = (String) in.readObject();
		System.out.println(message);
		status = console.nextInt();
		sendMessage(status);

		// Player position
		message = (String) in.readObject();
		System.out.println(message);
		position = console.nextInt();
		sendMessage(position);
	}

	// updatePlayerValue method
	void updatePlayerValue() throws ClassNotFoundException, IOException {
		int playerID;
		double newValuation;
		boolean found;

		message = (String) in.readObject();
		System.out.println(message);
		playerID = console.nextInt();
		sendMessage(playerID);

		message = (String) in.readObject();
		System.out.println(message);
		newValuation = console.nextDouble();
		sendMessage(newValuation);

		found = (boolean) in.readObject();
		if (found) {
			message = (String) in.readObject();
			System.out.println(message);
		}

	}

	// updatePlayerStatus method
	void updatePlayerStatus() throws ClassNotFoundException, IOException {
		int playerID;
		int newStatus;
		boolean found;

		message = (String) in.readObject();
		System.out.println(message);
		playerID = console.nextInt();
		sendMessage(playerID);

		message = (String) in.readObject();
		System.out.println(message);
		newStatus = console.nextInt();
		sendMessage(newStatus);

		found = (boolean) in.readObject();
		if (found) {
			message = (String) in.readObject();
			System.out.println(message);
		}
	}
}