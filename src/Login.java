import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class Login extends JFrame implements ActionListener{
	/* stores the object of this class in a static variable so we can access this classes' methods from other classes. the instantiation of the class and
	 * the definition of this variable is handled in main, yet we are able to access its members from outside the main method (and outside this class)
	 * through this (see showLogin() which is called in Apollo() without having to instantiate this main class there).
	 */
	static Login main = null;
	static Admin admin = null;
	static Apollo cashier = null;


	// We declare this Scanner as null to check if the file has been loaded or not.
	Scanner inputFile = null;

	String first, second;

	Font f = new Font("Verdana",Font.PLAIN, 15);
	Font a= new Font("Verdana",Font.PLAIN,15);
	Font y= new Font("Lucida Calligraphy", Font.PLAIN, 35);
	Font z = new Font("TimesRoman",Font.BOLD, 15);

	JLabel Title = new JLabel("Apollo Music Shop");
	JLabel lblUsername = new JLabel("Username:");
	JLabel lblPassword = new JLabel("Password:");

	JLabel lblLogo = new JLabel();
	ImageIcon img;
	ImageIcon imgLogo = new ImageIcon("misc/logo_white200x150.png");

	JTextField txtUsername=new JTextField(10);
	JPasswordField txtPassword = new JPasswordField (10);

	JButton login = new JButton("Log In");
	JButton btnsignup = new JButton("Sign Up");
	JButton btncancel= new JButton("Exit");

	ArrayList<String> usernames = new ArrayList<String>();
	ArrayList<String> passwords = new ArrayList<String>(); //converted these to ArrayList so size is dynamic and indexes automatically adjust
	ArrayList<Integer> usertypes = new ArrayList<Integer>();

	public Login() {
		lblUsername.setForeground(Color.BLACK);
		lblPassword.setForeground(Color.BLACK);
		Title.setForeground(Color.BLACK);

		setUndecorated(true);

		//Button
		login.setIcon(new ImageIcon("misc/loginBtn1.png"));
		btnsignup.setIcon(new ImageIcon("misc/signupBtn1.png"));
		btncancel.setIcon(new ImageIcon("misc/cancelBtn1.png"));

		JLabel background;
    	setLayout(null);
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
		img = new ImageIcon("portal/background04.jpg");
		setTitle("User Login");
		setVisible(true);
		setLocation(320, 180);
		setSize(1280, 720);
		setResizable(false);

		Container c = getContentPane();
		c.setLayout(null);

		// Components are now stored in a JPanel.
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(null);
		loginPanel.setBounds(390, 160, 500, 400);
		loginPanel.setVisible(true);
		loginPanel.setBackground(Color.WHITE);
		c.add(loginPanel);

		// Logo
		loginPanel.add(lblLogo);
		lblLogo.setIcon(imgLogo);
		lblLogo.setBounds(150, 20, 200, 150);

		Title.setBounds(80,15,350,100); 		//loginPanel.add(Title);
		Title.setFont(y);
		lblUsername.setBounds(120,180,100,50);	loginPanel.add(lblUsername);
		lblUsername.setFont(z);
		txtUsername.setBounds(220,195,150,20);	loginPanel.add(txtUsername);
		lblPassword.setBounds(120,210,100,50); 	loginPanel.add(lblPassword);
		lblPassword.setFont(z);
		txtPassword.setBounds(220,225,150,20);	loginPanel.add(txtPassword);
		loginPanel.add(login);
		login.setBounds(200,270,100,20);		login.setFont(a);
		//loginPanel.add(btnsignup);
		//btnsignup.setBounds(200,290,125,20);		btnsignup.setFont(a);
		loginPanel.add(btncancel);
		btncancel.setBounds(200,300,100,20);	btncancel.setFont(a);
		txtUsername.addActionListener(this);
		txtPassword.addActionListener(this);
		login.addActionListener(this);
		btncancel.addActionListener(this);
		btnsignup.addActionListener(this);

		background = new JLabel("",img,JLabel.CENTER);
		background.setBounds(0, 0, 1280, 720);
		add(background);
	}

	public void actionPerformed(ActionEvent e){

		if (e.getSource() == btncancel){
			System.exit(0);
		}
		else{

		String s = txtUsername.getText();
		String s2 = txtPassword.getText();

		loadLoginFile();
		loginSystem(s, s2);

		}
	}

	public void loginSystem(String user, String pass){
		int i = 0;
		boolean unknown = true;

		for(i = 0; i < usernames.size(); i++){
			if(user.toLowerCase().equals(usernames.get(i).toLowerCase())){
				if(pass.equals(passwords.get(i))){
					JOptionPane.showMessageDialog(null, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
					unknown = false;
					displayPanel(usertypes.get(i));
					break;
				} else {
					JOptionPane.showMessageDialog(null, "Incorrect password!", "Error", JOptionPane.WARNING_MESSAGE);
					unknown = false;
					break;
				}
			}
		}

		if(unknown == true){
			JOptionPane.showMessageDialog(null, "Your username was not found in our database!", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	public void loadLoginFile(){
		try{
			Scanner inputFile = new Scanner(new FileReader("login.txt"));
			int i = 0;
			usertypes.clear();
			usernames.clear();
			passwords.clear();

			while(inputFile.hasNext()){
				usertypes.add(i, Integer.parseInt(inputFile.nextLine()));
				usernames.add(i, inputFile.nextLine());
				passwords.add(i, inputFile.nextLine());

				i++;
			}

			inputFile.close();
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, e);
		}

		//System.out.println(usertypes + "\n" + usernames + "\n" + passwords);
	}

	public void displayPanel(int usertype){
		if(usertype == 1){ // ADMIN
			hideLogin();
			System.out.println("admin");
			admin = new Admin();
		} else if(usertype == 2){ // CASHIER
			hideLogin();
			System.out.println("cashier");
			cashier = new Apollo();
		} else {
			System.out.println("error");
		}
	}


	public static void hideLogin(){
		main.txtUsername.setText("");
		main.txtPassword.setText("");
		main.txtUsername.requestFocus();
		main.setVisible(false);
	}

	public static void showLogin(){
		main.loadLoginFile();
		main.setVisible(true);
	}

	public static void showAdmin(){
		admin.setVisible(true);
	}

    public static void main (String args[]) {
		main = new Login();
    }
}





