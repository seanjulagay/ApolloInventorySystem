import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class signUp extends JFrame implements ActionListener{

	// this was the sign up window you've been looking at
	// all along.
	// JFrame = new JFrame("Sign Up");

	// VS Code is nagging me about this.
	private static final long serialVersionUID = 8944989365139228735L;

	JTextField userName = new JTextField();
	JPasswordField passWord = new JPasswordField();
	JLabel usernameLbl = new JLabel ("Username:");
	JLabel passwordLbl = new JLabel ("Password:");
	JButton signupBtn = new JButton ("Sign Up");
	JButton doneBtn = new JButton ("Done");
	JLabel blank = new JLabel();
	JLabel typeLbl = new JLabel("Account Type");
	JRadioButton radAdmin = new JRadioButton("Admin");
	JRadioButton radCashier = new JRadioButton("Cashier");
	Font z = new Font("TimesRoman",Font.BOLD, 15);
	Font f = new Font("TimesRoman",Font.BOLD, 13);
	ImageIcon img1;
	FileWriter fileWriter;
	Scanner loginFile;

	ButtonGroup grpType = new ButtonGroup();

	int accType = 0;
	int i = 0;

	ArrayList<String> usernameData = new ArrayList<String>();

    public signUp() {

    	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setUndecorated(true);
		JLabel background1;
		img1 = new ImageIcon("portal/background04.jpg");

		Container c = getContentPane();
		c.setLayout(null);

		//signupBtn.setIcon(new ImageIcon("misc/loginBtn1.png"));
		signupBtn.setBackground(Color.BLACK);
		signupBtn.setForeground(Color.WHITE);
		signupBtn.setFont(new Font("Times New Roman", 0, 16));
		//doneBtn.setIcon(new ImageIcon("misc/doneBtn02.png"));
		doneBtn.setBackground(Color.BLACK);
		doneBtn.setForeground(Color.WHITE);
		doneBtn.setFont(new Font("Times New Roman", 0, 16));

    	usernameLbl.setForeground(Color.black);
		passwordLbl.setForeground(Color.black);
		typeLbl.setForeground(Color.black);
		radCashier.setBackground(Color.white);
		radCashier.setForeground(Color.black);
		radAdmin.setBackground(Color.white);
		radAdmin.setForeground(Color.black);

    	getContentPane().setBackground(Color.BLACK);

		/*
    	GridLayout gridLayout = new GridLayout();
    	gridLayout.setColumns(2);
    	gridLayout.setRows(3);

    	setLayout(null);
		*/

		JPanel whitebox = new JPanel(); // signupPanel is placed inside whitebox, so we don't have to manually readjust the bounds of every component. just adjust signupPanel itself.
		whitebox.setLayout(null);
		whitebox.setBounds(390, 160, 500, 400);
		whitebox.setVisible(true);
		whitebox.setBackground(Color.WHITE);

		JPanel signupPanel = new JPanel();
		signupPanel.setLayout(null);
		signupPanel.setBounds(110, 200, 300, 220);
		signupPanel.setVisible(true);
		signupPanel.setBackground(Color.WHITE);
		whitebox.add(signupPanel);
		c.add(whitebox);

		// Logo
		ImageIcon imgLogo = new ImageIcon("misc/logo_white200x150.png");
		JLabel lblLogo = new JLabel();
		lblLogo.setIcon(imgLogo);
		lblLogo.setBounds(150, 20, 200, 150);
		whitebox.add(lblLogo);

    	signupBtn.addActionListener(this);
    	doneBtn.addActionListener(this);

		grpType.add(radCashier);
    	grpType.add(radAdmin);

    	signupPanel.add(usernameLbl);
    	usernameLbl.setFont(z);
    	signupPanel.add(userName);
    	signupPanel.add(passwordLbl);
    	passwordLbl.setFont(z);
    	//add(blank);
    	signupPanel.add(passWord);
    	signupPanel.add(radAdmin);
    	signupPanel.add(radCashier);
    	signupPanel.add (doneBtn);
    	signupPanel.add(signupBtn);
    	signupPanel.add(typeLbl);
 		signupPanel.add(radCashier);
 		signupPanel.add(radAdmin);

    	usernameLbl.setBounds(20, -15, 100,50);
    	passwordLbl.setBounds(25, 15, 100,50);
    	userName.setBounds(100, 0, 150, 20);
    	passWord.setBounds(100, 30, 150, 20);
    	typeLbl.setBounds(105, 60, 150, 20);
    	typeLbl.setFont(f);
		radCashier.setBounds(60, 80, 100, 20);
		radCashier.setFont(f);
		radAdmin.setBounds(160, 80, 100, 20);
		radAdmin.setFont(f);
		doneBtn.setBounds(40, 110, 90, 20);
		signupBtn.setBounds(150, 110, 90, 20);

		background1 = new JLabel("",img1,JLabel.CENTER);
		background1.setBounds(0, 0, 1280, 720);
		add(background1);

    	setSize(1280, 720);
    	setVisible(true);
    	setLocation(320, 180);
    	setResizable(false);

    	getLoginFile();
    }

    public void actionPerformed(ActionEvent actionEvent){
    	if (actionEvent.getActionCommand() == doneBtn.getActionCommand()){
			Admin.showAdmin();
			dispose();
		}

    	if (actionEvent.getActionCommand() == signupBtn.getActionCommand()) {
    		if(radAdmin.isSelected()){
    			accType = 1;
    		} else if(radCashier.isSelected()) {
    			accType = 2;
    		}

			if(userName.getText().equals("") || passWord.getText().equals("")){
				JOptionPane.showMessageDialog(null, "Error! Enter data in all the text fields.", "Error", JOptionPane.ERROR_MESSAGE);
			} else if(usernameData.contains(userName.getText())){
				JOptionPane.showMessageDialog(null, "Error! That username already exists in the database.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				if(accType == 0){
    				JOptionPane.showMessageDialog(null, "Error! Select an account type.", "Error", JOptionPane.ERROR_MESSAGE);
    			} else {
    				try {
						fileWriter = new FileWriter("login.txt", true);
						fileWriter.write(accType + "\n");
						fileWriter.write(userName.getText() + "\n");
						fileWriter.write(passWord.getText() + "\n");
						fileWriter.close();
						JOptionPane.showMessageDialog(null, "You have signed up successfully!");
						// clear usernameData so the changes are reflected
						usernameData.clear();
						getLoginFile();
	    			} catch (IOException ex) {
						// Changed Exception -> IOException.
						// IOException is thrown by the FileWriter
						// if it can't open the file for writing.

						JOptionPane.showMessageDialog(null, ex.getMessage());
						// ex.getMessage() is a simple way to show only the exception its thrown.
						// Log the whole exception in the console so you can trace it in development.
					}

					accType = 0;
					grpType.clearSelection();
					userName.setText("");
					passWord.setText("");
    			}
			}



    	}
    }

	public void getLoginFile(){
		i = 0;

		try {
			loginFile = new Scanner(new FileReader("login.txt"));

			while(loginFile.hasNext()){
				loginFile.nextLine(); // skip usertype line
				usernameData.add(i, loginFile.nextLine());
				loginFile.nextLine(); // skip password line

				i++;
			}

			loginFile.close();
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "getLoginFile(): " + e);
		}
	}

    public static void main (String args[]) {
    	new signUp();
	}

}


