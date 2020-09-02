import java.io.*;
import java.util.*;
import java.nio.file.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;


public class Users extends JFrame implements ActionListener, MouseListener{
	ArrayList<Integer> usertype = new ArrayList<Integer>();
	ArrayList<String> username = new ArrayList<String>();
	ArrayList<String> password = new ArrayList<String>();

	String tblHeader[] = {"Username", "Account Type"};

	int i;
	int index;
	boolean edit = true;

	JButton btnEdit = new JButton("Edit");
	JButton btnDelete = new JButton("Delete");
	JButton btnCancel = new JButton("Cancel");
	JButton btnConfirm = new JButton("Confirm");
	JButton btnReturn = new JButton("Return");
	JTable tblUsers = new JTable();
	JScrollPane scrollUsers = new JScrollPane(tblUsers);
	JLabel lblMessage = new JLabel("Default accounts (admin & cashier) cannot be edited!");
	JLabel lblUsername = new JLabel("Username:");
	JLabel lblPassword = new JLabel("Password:");
	JLabel lblType = new JLabel("Account Type:");
	JLabel image05 = new JLabel("");
	JLabel image06 = new JLabel("");
	JTextField txtUsername = new JTextField(10);
	JPasswordField txtPassword = new JPasswordField(10);
	JRadioButton radAdmin = new JRadioButton("Admin");
	JRadioButton radCashier = new JRadioButton("Cashier");

	ButtonGroup grpType = new ButtonGroup();

	JPanel pUsers = new JPanel();
	JPanel pEdit = new JPanel();

	DefaultTableModel modelUsers = new DefaultTableModel(){
		public boolean isCellEditable(int row, int column){
			return false;
		}
	};

	Font fontBody = new Font("Times New Roman", 1, 16);

	public Users(){
		Container c = getContentPane();
		c.setLayout(null);
		pUsers.setLayout(null);
		pEdit.setLayout(null);
		pEdit.setVisible(false);

		c.add(pUsers);
		c.add(pEdit);

		pUsers.setBackground(Color.black);
		pEdit.setBackground(Color.black);

		image05.setBounds(-15,275,700,200);
		image05.setIcon(new ImageIcon("misc/image03.png"));
		pUsers.add(image05);

		image06.setBounds(-15,275,700,200);
		image06.setIcon(new ImageIcon("misc/image03.png"));
		pEdit.add(image06);

		pUsers.setBounds(0, 0, 400, 400);
		pUsers.add(scrollUsers);
		pUsers.add(btnEdit);
		pUsers.add(btnDelete);
		pUsers.add(btnCancel);

		btnEdit.setEnabled(false);
		btnDelete.setEnabled(false);

		scrollUsers.setBounds(30, 20, 330, 180);
		btnEdit.setBounds(150, 230, 90, 30);
		btnDelete.setBounds(150, 270, 90, 30);
		btnCancel.setBounds(150, 310, 90, 30);

		btnEdit.setIcon(new ImageIcon("misc/editBtn1.png"));
        btnDelete.setIcon(new ImageIcon("misc/deleteBtn.png"));
        btnCancel.setIcon(new ImageIcon("misc/cancelBtn2.png"));
        btnConfirm.setBackground(Color.BLACK);
        btnReturn.setBackground(Color.BLACK);
		btnConfirm.setForeground(Color.WHITE);
		btnReturn.setForeground(Color.WHITE);
		btnConfirm.setFont(fontBody);
		btnReturn.setFont(fontBody);


		pEdit.setBounds(0, 0, 400, 400);
		pEdit.add(lblMessage);
		pEdit.add(lblUsername);
		pEdit.add(lblPassword);
		pEdit.add(lblType);
		pEdit.add(txtUsername);
		pEdit.add(txtPassword);
		pEdit.add(radAdmin);
		pEdit.add(radCashier);
		pEdit.add(btnConfirm);
		pEdit.add(btnReturn);

		grpType.add(radAdmin);
		grpType.add(radCashier);

		lblMessage.setBounds(45, 40, 400, 25);
		lblUsername.setBounds(40, 70, 100, 25);
		txtUsername.setBounds(150, 70, 200, 25);
		lblPassword.setBounds(40, 110, 100, 25);
		txtPassword.setBounds(150, 110, 200, 25);
		lblType.setBounds(40, 150, 200, 25);
		radAdmin.setBounds(85, 190, 100, 25);
		radCashier.setBounds(200, 190, 100, 25);

		btnConfirm.setBounds(150, 240, 100, 30);
		btnReturn.setBounds(150, 290, 100, 30);

		btnEdit.addActionListener(this);
		btnDelete.addActionListener(this);
		btnCancel.addActionListener(this);
		btnConfirm.addActionListener(this);
		btnReturn.addActionListener(this);
		tblUsers.addMouseListener(this);

		lblMessage.setForeground(Color.RED);
		lblMessage.setVisible(false);
		lblUsername.setFont(fontBody);
		lblUsername.setForeground(Color.WHITE);
		lblPassword.setFont(fontBody);
		lblPassword.setForeground(Color.WHITE);
		lblType.setFont(fontBody);
		lblType.setForeground(Color.WHITE);
		radAdmin.setFont(fontBody);
		radAdmin.setBackground(Color.BLACK);
		radAdmin.setForeground(Color.WHITE);
		radCashier.setFont(fontBody);
		radCashier.setBackground(Color.BLACK);
		radCashier.setForeground(Color.WHITE);

		tblUsers.setModel(modelUsers);
		modelUsers.setColumnIdentifiers(tblHeader);
		tblUsers.getTableHeader().setBackground(Color.WHITE);
		scrollUsers.getViewport().setBackground(Color.WHITE);
		tblUsers.getTableHeader().setReorderingAllowed(false);
		tblUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		setTitle("Users");
		setUndecorated(true);
		setSize(400, 400);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);

		loadLoginfile();
		loadTable();
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btnEdit){
			reflectUserSelection(tblUsers.getSelectedRow());
			togglePanels();
		}

		if(e.getSource() == btnDelete){
			index = tblUsers.getSelectedRow();

			if(index == 0 || index == 1){
				JOptionPane.showMessageDialog(null, "Error! Cannot delete default accounts.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Delete", JOptionPane.WARNING_MESSAGE);

				if(confirm == JOptionPane.YES_OPTION){
					int index = tblUsers.getSelectedRow();

					username.remove(index);
					password.remove(index);
					usertype.remove(index);
					loadTable();
				}
			}


		}

		if(e.getSource() == btnConfirm){
			editAccount(tblUsers.getSelectedRow());
			loadTable();
			togglePanels();
		}

		if(e.getSource() == btnCancel){
			saveLoginFile();
			this.dispose();
		}

		if(e.getSource() == btnReturn){
			togglePanels();
		}
	}

	public void mouseClicked(MouseEvent e){
		if(e.getSource() == tblUsers){
			btnEdit.setEnabled(true);
			btnDelete.setEnabled(true);
		}
	}

	public void togglePanels(){
		if(edit == true){
			pUsers.setVisible(false);
			pEdit.setVisible(true);
			edit = false;
		} else if(edit == false){
			pUsers.setVisible(true);
			pEdit.setVisible(false);
			edit = true;
		}
	}

	public void loadLoginfile(){
		try{
			Scanner loginfile = new Scanner(new FileReader("login.txt"));

			while(loginfile.hasNext()){
				usertype.add(loginfile.nextInt());
				loginfile.nextLine();
				username.add(loginfile.nextLine());
				password.add(loginfile.nextLine());
			}

			loginfile.close();

			//System.out.println(usertype + "\n" + username + "\n" + password);
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public void saveLoginFile(){
		try{
			PrintWriter loginfile = new PrintWriter("temp/login.txt");
			Path source = Paths.get("temp/login.txt");
			Path target = Paths.get("login.txt");

			for(i = 0; i < username.size(); i++){
				loginfile.println(usertype.get(i));
				loginfile.println(username.get(i));
				loginfile.println(password.get(i));
			}

			loginfile.close();

			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "saveLoginFile(): " + e);
		}
	}

	public void loadTable(){
		i = 0;

		modelUsers.setRowCount(0);

		for(int type: usertype){
			if(type == 1){
				modelUsers.addRow(new Object[]{username.get(i), "Admin"});
			} else if(type == 2){
				modelUsers.addRow(new Object[]{username.get(i), "Cashier"});
			}
			i++;
		}
	}

	public void reflectUserSelection(int index){
		txtUsername.setText(username.get(index));
		txtPassword.setText(password.get(index));
		if(usertype.get(index) == 1){
			radAdmin.setSelected(true);
		} else if(usertype.get(index) == 2){
			radCashier.setSelected(true);
		}

		if(index == 0 || index == 1){
			lblMessage.setVisible(true);
			txtUsername.setEnabled(false);
			txtPassword.setEnabled(false);
			radAdmin.setEnabled(false);
			radCashier.setEnabled(false);
		} else {
			lblMessage.setVisible(false);
			txtUsername.setEnabled(true);
			txtPassword.setEnabled(true);
			radAdmin.setEnabled(true);
			radCashier.setEnabled(true);
		}
	}

	public void editAccount(int index){
		username.remove(index);
		username.add(index, txtUsername.getText().toString());
		password.remove(index);
		password.add(index, txtPassword.getText().toString());
		usertype.remove(index);
		if(radAdmin.isSelected()){
			usertype.add(index, 1);
		} else if(radCashier.isSelected()){
			usertype.add(index, 2);
		}

		//System.out.println(usertype + "\n" + username + "\n" + password);
	}

	public static void main(String[] args){
		new Users();
	}

	public void keyTyped(KeyEvent e){}
	public void keyPressed(KeyEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
}