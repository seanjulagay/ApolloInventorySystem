import java.io.*; // Used for FileReader and PrintWriter
import java.nio.file.*; // Used for "Path" datatype and the "Paths.get()" function
import java.util.*; // Used for ArrayLists, Array functions, and the Scanner function
import javax.swing.*; // Used for JOptionPane functions and adding components (JLabel, JButton, etc)
import javax.swing.table.*; // Used for TableModels, which allow table editing functions
import java.awt.*; // Used for container, Color functions, Font functions, and component editing
import java.awt.event.*; // Used for listeners which are added to Swing components
import java.text.*; // Used for the DecimalFormat function

public class Admin extends JFrame implements ActionListener, KeyListener, MouseListener{
	// These ArrayLists hold masterfile JTable values
	ArrayList<String> id = new ArrayList<String>();
	ArrayList<Double> price = new ArrayList<Double>();
	ArrayList<Integer> stock = new ArrayList<Integer>();
	ArrayList<String> desc = new ArrayList<String>();
	// These ArrayLists hold sales JTable values
	ArrayList<String> sID = new ArrayList<String>();
	ArrayList<Double> sPrice = new ArrayList<Double>();
	ArrayList<Integer> sQuantity = new ArrayList<Integer>();
	ArrayList<String> sDesc = new ArrayList<String>();
	ArrayList<String> sCategory = new ArrayList<String>();

	ImageIcon imgLogo = new ImageIcon("misc/logo_small.png");

	String tblHeader[] = {"ID", "Description", "Stock", "Price", "Category"};
	String tblSalesHeader[] = {"ID", "Description", "Quantities Sold", "Price"};
	String category[] = {"ALL PRODUCTS", "GUITARS", "DRUMS", "AMPLIFIERS", "INTERFACES", "ACOUSTICS"};

	String searchString = "";
	double sum;
	double newPrice;
	int newAmount; // new values after using edit function
	int i = 0;
	int j = 0; // iterators
	int tblType; // 0 = masterifle, 1 = salesfile

	DecimalFormat money = new DecimalFormat("#,###,##0.00");

	Container c = getContentPane();

	JPanel pMaster = new JPanel();
	JPanel pSearch = new JPanel();
	JPanel pSales = new JPanel();
	JPanel pTotal = new JPanel();
	JPanel pCurrent = new JPanel();
	JPanel pMisc = new JPanel();
	JPanel pExtra = new JPanel();

    Font f = new Font("Times New Roman",Font.BOLD, 15);

	JButton btnLogout = new JButton("Logout");
	JButton btnExit = new JButton("Exit");
	JButton btnEdit = new JButton("Edit");
	JButton btnUsers = new JButton("Users");
	JButton btnSignup = new JButton("Sign Up");

	JLabel lblImgLogo = new JLabel();
	JLabel lblMasterHeader = new JLabel("All Items");
	JLabel lblSearch = new JLabel("Search:");
	JLabel lblCategory = new JLabel("Category:");
	JLabel lblTotalSales = new JLabel("Total sales for all products: ");
	JLabel lblTotalSalesAmount = new JLabel("Php 0.00");
	JLabel lblDesc = new JLabel("Item Description", SwingConstants.CENTER);
	JLabel lblID = new JLabel("Product ID:");
	JLabel lblPrice = new JLabel("Price:");
	JLabel lblAmount = new JLabel("Stocks:");

	JLabel image01 = new JLabel("");
	JLabel image02 = new JLabel("");
	JLabel image03 = new JLabel("");
	JLabel image04 = new JLabel("");

	JTextField txtID = new JTextField(10);
	JTextField txtPrice = new JTextField(10);
	JTextField txtAmount = new JTextField(10);
	JTextField txtSearch = new JTextField(10);

	JComboBox cbCategory = new JComboBox(category);

	JTable tblMaster = new JTable();
	JTable tblSales = new JTable();

	JScrollPane scrollMaster = new JScrollPane(tblMaster);
	JScrollPane scrollSales = new JScrollPane(tblSales);

	DefaultTableModel modelMaster = new DefaultTableModel(){
		public boolean isCellEditable(int row, int column){
			return false;
		}
	};
	DefaultTableModel modelSales = new DefaultTableModel(){
		public boolean isCellEditable(int row, int column){
			return false;
		}
	};

	Font fontBody = new Font("Courier New", 1, 16);

	public void p(Object s){
		System.out.println(s); // godlike shortened println method (nakita ko lang sa meme) -- p("Hello World!");
	}

	public Admin(){
		c.setLayout(null);
		pMaster.setLayout(null);
		pExtra.setLayout(null);
		pSearch.setLayout(null);
		pSales.setLayout(null);
		pTotal.setLayout(null);
		pCurrent.setLayout(null);
		pMisc.setLayout(null);

		//BACKGROUND
		image01.setBounds(-105,430,700,200);
		image01.setIcon(new ImageIcon("misc/image03.png"));
		pMaster.add(image01);

		image02.setBounds(-250,0,700,200);
		image02.setIcon(new ImageIcon("misc/image02.png"));
		pCurrent.add(image02);

		image03.setBounds(120,-5,1200, 50);
		image03.setIcon(new ImageIcon("misc/image05.png"));
		pExtra.add(image03);

		image04.setBounds(-208,-10,600, 200);
		image04.setIcon(new ImageIcon("misc/image12.png"));
		pMisc.add(image04);


       //BUTTON DESIGNS
       btnLogout.setIcon(new ImageIcon("misc/logoutBtn.png"));
       btnEdit.setIcon(new ImageIcon("misc/editBtn.png"));
       btnExit.setIcon(new ImageIcon("misc/exitBtn.png"));
       btnUsers.setIcon(new ImageIcon("misc/usersBtn01.png"));
       btnSignup.setIcon(new ImageIcon("misc/signupBtn1.png"));

		// Container level
		c.add(lblImgLogo);
		c.add(btnLogout);
		c.add(btnExit);
		c.add(pMaster);
		c.add(pExtra);
		c.add(pSearch);
		c.add(pSales);
		c.add(pTotal);
		c.add(pCurrent);
		c.add(pMisc);
		lblImgLogo.setBounds(560, 0, 150, 100);
		btnLogout.setBounds(1000, 20, 125, 40);
		btnExit.setBounds(1150, 20, 100, 100);
		pMaster.setBounds(20, 120, 400, 580);
		pExtra.setBounds(440, 120, 820, 40);
		pSearch.setBounds(440, 150, 820, 50);
		pSales.setBounds(440, 200, 820, 250);
		pTotal.setBounds(440, 450, 820, 50);
		pCurrent.setBounds(440, 520, 550, 180);
		pMisc.setBounds(1010, 520, 250, 180);

		// pMaster
		pMaster.add(lblMasterHeader);
		pMaster.add(scrollMaster);
		lblMasterHeader.setBounds(150, 20, 100, 10);
		scrollMaster.setBounds(0, 50, 400, 650);

		// pSearch
		pSearch.add(lblSearch);
		pSearch.add(lblCategory);
		pSearch.add(txtSearch);
		pSearch.add(cbCategory);
		lblSearch.setBounds(20, 10, 70, 30);
		lblCategory.setBounds(550, 10, 100, 30);
		txtSearch.setBounds(100, 15, 150, 25);
		cbCategory.setBounds(650, 10, 150, 30);

		// pSales
		pSales.add(scrollSales);
		scrollSales.setBounds(0, 0, 820, 250);

		// pTotal
		pTotal.add(lblTotalSales);
		pTotal.add(lblTotalSalesAmount);
		lblTotalSales.setBounds(20, 10, 500, 30);
		lblTotalSalesAmount.setBounds(600, 10, 300, 30);

		// pCurrent
		pCurrent.add(lblDesc);
		pCurrent.add(lblID);
		pCurrent.add(lblPrice);
		pCurrent.add(lblAmount);
		pCurrent.add(btnEdit);
		pCurrent.add(txtID);
		pCurrent.add(txtPrice);
		pCurrent.add(txtAmount);
		lblDesc.setBounds(80, 15, 400, 30);
		lblID.setBounds(120, 50, 200, 30);
		lblPrice.setBounds(120, 80, 100, 30);
		lblAmount.setBounds(120, 110, 100, 30);
		btnEdit.setBounds(250, 145, 115, 25);
		txtID.setBounds(250, 50, 180, 25);
		txtPrice.setBounds(250, 80, 180, 25);
		txtAmount.setBounds(250, 110, 180, 25);
        btnExit.setBounds(1150, 20, 125, 40);

		// pMisc
		pMisc.add(btnSignup);
		pMisc.add(btnUsers);
		btnUsers.setBounds(80, 60, 95, 20);
		btnSignup.setBounds(80, 100, 95, 20);

		// Colors
		c.setBackground(Color.BLACK);
		pMaster.setBackground(Color.WHITE);
		pSearch.setBackground(Color.WHITE);
		pSales.setBackground(Color.WHITE);
		pTotal.setBackground(Color.WHITE);
		pCurrent.setBackground(Color.WHITE);
		pMisc.setBackground(Color.WHITE);
		pExtra.setBackground(Color.WHITE);

		// Listeners
		btnLogout.addActionListener(this);
		btnExit.addActionListener(this);
		btnEdit.addActionListener(this);
		btnUsers.addActionListener(this);
		btnSignup.addActionListener(this);
		txtSearch.addActionListener(this);
		cbCategory.addActionListener(this);
		txtSearch.addKeyListener(this);
		tblMaster.addMouseListener(this);
		tblSales.addMouseListener(this);

		// Font
		lblMasterHeader.setFont(f);
		lblMasterHeader.setForeground(Color.BLACK);
		lblSearch.setFont(f);
		lblSearch.setForeground(Color.BLACK);
		lblCategory.setFont(f);
		lblCategory.setForeground(Color.BLACK);
		lblTotalSales.setFont(f);
		lblTotalSales.setForeground(Color.BLACK);
		lblTotalSalesAmount.setFont(f);
		lblTotalSalesAmount.setForeground(Color.BLACK);
		lblDesc.setFont(f);
		lblDesc.setForeground(Color.BLACK);
		lblID.setFont(f);
		lblID.setForeground(Color.BLACK);
		lblPrice.setFont(f);
		lblPrice.setForeground(Color.BLACK);
		lblAmount.setFont(f);
		lblAmount.setForeground(Color.BLACK);

		// Misc
		lblImgLogo.setIcon(imgLogo);
		tblMaster.setModel(modelMaster);
		tblSales.setModel(modelSales);
		modelMaster.setColumnIdentifiers(tblHeader);
		modelSales.setColumnIdentifiers(tblSalesHeader);
		scrollMaster.getViewport().setBackground(Color.WHITE);
		scrollSales.getViewport().setBackground(Color.WHITE);
		tblMaster.getTableHeader().setBackground(Color.WHITE);
		tblSales.getTableHeader().setBackground(Color.WHITE);
		tblMaster.getTableHeader().setReorderingAllowed(false);
		tblSales.getTableHeader().setReorderingAllowed(false);
		tblMaster.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblSales.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		txtID.setEditable(false);
		txtPrice.setEditable(false);
		txtAmount.setEditable(false);

		// JFrame
		setSize(1280, 720);
		setUndecorated(true);
		setLocation(320, 180);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		loadMasterfile();
		loadSalesfile();
		loadComponents();
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btnLogout){
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out?", "Log Out", JOptionPane.WARNING_MESSAGE);

			if(confirm == JOptionPane.YES_OPTION){
				Login.showLogin();
				saveMasterfile();
				saveSalesfile();
				this.dispose();
			}
		}

		if(e.getSource() == btnExit){
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the program?", "Exit", JOptionPane.WARNING_MESSAGE);

			if(confirm == JOptionPane.YES_OPTION){
				saveMasterfile();
				saveSalesfile();
				System.exit(0);
			}
		}

		if(e.getSource() == btnEdit){
			if(Double.parseDouble(txtPrice.getText()) < 0 || Integer.parseInt(txtAmount.getText()) < 0){
				JOptionPane.showMessageDialog(null, "Error! Enter a positive value.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				try{
					int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to edit this item?", "Edit", JOptionPane.WARNING_MESSAGE);

					if(confirm == JOptionPane.YES_OPTION){
						newPrice = Double.parseDouble(txtPrice.getText());
						newAmount = Integer.parseInt(txtAmount.getText());

						p(newPrice + " " + newAmount);

						editSelectedItem(txtID.getText());
						loadComponents();
						searchFunction(txtSearch.getText(), cbCategory.getSelectedItem().toString());
					}
				} catch(Exception ex){
					JOptionPane.showMessageDialog(null, "Error! Enter valid values.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		}

		if(e.getSource() == cbCategory){
			searchFunction(txtSearch.getText(), cbCategory.getSelectedItem().toString());
			reflectTotalSales(cbCategory.getSelectedItem().toString());
		}

		if(e.getSource() == btnUsers){
			new Users();
		}

		if(e.getSource() == btnSignup){
			new signUp();
			this.setVisible(false);
		}
	}

	public void keyReleased(KeyEvent e){
		if(e.getSource() == txtSearch){
			searchFunction(txtSearch.getText(), cbCategory.getSelectedItem().toString());
		}
	}

	public void mouseClicked(MouseEvent e){
		if(e.getSource() == tblMaster){
			tblType = 0;
			tblSales.getSelectionModel().clearSelection();
			displaySelectedItem(modelMaster.getValueAt(tblMaster.getSelectedRow(), 0).toString());
		} else if(e.getSource() == tblSales){
			tblType = 1;
			tblMaster.getSelectionModel().clearSelection();
			displaySelectedItem(modelSales.getValueAt(tblSales.getSelectedRow(), 0).toString());
		}

		txtPrice.setEditable(true);
		txtAmount.setEditable(true);
	}

	public void loadMasterfile(){
		try{
			Scanner masterfile = new Scanner(new FileReader("masterfile.txt"));

			while(masterfile.hasNext()){
				id.add(masterfile.next());
				price.add(masterfile.nextDouble());
				stock.add(masterfile.nextInt());
				masterfile.nextLine();
				desc.add(masterfile.nextLine());
			}

			masterfile.close();
			//System.out.println(id + "\n" + price + "\n" + stock + "\n" + desc);
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "loadMasterFile(): " + e);
		}
	}

	public void saveMasterfile() {
		try{
			PrintWriter masterfileTemp = new PrintWriter("temp/masterfile.txt");
			Path source = Paths.get("temp/masterfile.txt");
			Path target = Paths.get("masterfile.txt");

			//rewriting the masterfile with the performed changes in a temporary file
			for(i = 0; i < id.size(); i++){
				masterfileTemp.print(id.get(i) + " ");
				masterfileTemp.print(price.get(i) + " ");
				masterfileTemp.println(stock.get(i));
				masterfileTemp.println(desc.get(i));

				if((i + 1) % 5 == 0){ //if new category
					masterfileTemp.println(); //add another empty line to visually separate categories in the masterfile.txt
				}
			}

			masterfileTemp.close();

			//overwrite the old masterfile by moving temp masterfile to orig masterfile's directory
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "saveMasterfile(): " + e);
		}
	}

	public void loadSalesfile(){
		try{
			Scanner salesfile = new Scanner(new FileReader("salesfile.txt"));

			while(salesfile.hasNext()){
				sID.add(salesfile.next());
				sPrice.add(salesfile.nextDouble());
				sQuantity.add(salesfile.nextInt());
				salesfile.nextLine();
				sDesc.add(salesfile.nextLine());
			}

			salesfile.close();

			/* SET CATEGORY -- for loops that assign product category depending on starting number (1-5)
			 *   - a better algorithm compared to the manual and inefficient setTblMF() function in Apollo.java */
			for(i = 0; i <= 5; i++){ // for loop that counts categories; (i <= number of catgeories)
				for(j = 0; j < sID.size(); j++){ // for loop that skims through every ID available
					if(sID.get(j).startsWith(Integer.toString(i))){ // check if sID starts with the corresponding number of category and assigns it according to category[] array
						sCategory.add(j, category[i]);
						//p("sCategory" + j + " " + sCategory.get(j));
					}
				}
			}
			//p("\n\n" + sID + "\n" + sPrice + "\n" + sQuantity + "\n" + sDesc);
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "loadSalesFile(): " + e);
		}
	}

	public void saveSalesfile() {
		try{
			PrintWriter salesfileTemp = new PrintWriter("temp/salesfile.txt");
			Path source = Paths.get("temp/salesfile.txt");
			Path target = Paths.get("salesfile.txt");

			//rewriting the salesfile with the performed changes in a temporary file
			for(i = 0; i < sID.size(); i++){
				salesfileTemp.print(sID.get(i) + " ");
				salesfileTemp.print(sPrice.get(i) + " ");
				salesfileTemp.println(sQuantity.get(i));
				salesfileTemp.println(sDesc.get(i));

				if((i + 1) % 5 == 0){ //if new category
					salesfileTemp.println(); //add another empty line to visually separate categories in the salesfile.txt
				}
			}

			salesfileTemp.close();

			//overwrite the old salesfile by moving temp salesfile to orig salesfile's directory
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "saveMasterfile(): " + e);
		}
	}

	public void loadComponents(){
		modelMaster.setRowCount(0);
		modelSales.setRowCount(0);

		for(i = 0; i < id.size(); i++){
			modelMaster.addRow(new Object[]{id.get(i), desc.get(i), stock.get(i), money.format(price.get(i)), sCategory.get(i)});
			modelSales.addRow(new Object[]{sID.get(i), sDesc.get(i), sQuantity.get(i), money.format(sPrice.get(i))});
		}

		tblMaster.revalidate();
		tblSales.revalidate();
		reflectTotalSales(cbCategory.getSelectedItem().toString());
	}

	public void searchFunction(String query, String currCategory){
		modelSales.setRowCount(0);
		query = query.toLowerCase();
		currCategory = currCategory.toLowerCase();

		if(currCategory.equals("all products")){ // Combobox current category == all
			for(i = 0; i < sID.size(); i++){
				if(sID.get(i).contains(query) || sDesc.get(i).toLowerCase().contains(query) || sCategory.get(i).toLowerCase().contains(query)){
					modelSales.addRow(new Object[]{sID.get(i), sDesc.get(i), sQuantity.get(i), money.format(sPrice.get(i))});
				}
			}
		} else { // Combobox current category == guitars, drums, etc
			for(i = 0; i < sID.size(); i++){
				if((sID.get(i).contains(query) || sDesc.get(i).toLowerCase().contains(query) || sCategory.get(i).toLowerCase().contains(query))
					&& sCategory.get(i).toLowerCase().equals(currCategory)){ // additional checker for category sort, allows the category itself to be searched even if combobox is activated
					modelSales.addRow(new Object[]{sID.get(i), sDesc.get(i), sQuantity.get(i), money.format(sPrice.get(i))});
				}
			}
		}


		p(query);

		tblSales.revalidate();
	}

	public void reflectTotalSales(String currCategory){
		sum = 0;
		currCategory = currCategory.toLowerCase();

		lblTotalSales.setText("Total sales for " + currCategory.toLowerCase() + ":");

		for(i = 0; i < sCategory.size(); i++){

			if(currCategory.equals("all products") || currCategory.equals(sCategory.get(i).toLowerCase())){
				sum += sPrice.get(i) * sQuantity.get(i);
			}
		}

		lblTotalSalesAmount.setText("Php " + money.format(sum));
	}

	public void displaySelectedItem(String currID){
		lblDesc.setText(desc.get(id.indexOf(currID)));
		txtID.setText(currID);
		txtPrice.setText(price.get(id.indexOf(currID)).toString());
		if(tblType == 0){ //masterfile table
			lblAmount.setText("Stocks:");
			txtAmount.setText(stock.get(id.indexOf(currID)).toString());
		} else if(tblType == 1){ //salesfile table
			lblAmount.setText("Sold:");
			txtAmount.setText(sQuantity.get(id.indexOf(currID)).toString());
		}
	}

	public void editSelectedItem(String currID){
		int index = id.indexOf(currID);

		if(tblType == 0){ // if masterfile table, change stock
			price.remove(index);
			price.add(index, newPrice);
			sPrice.remove(index);
			sPrice.add(index, newPrice);
			stock.remove(index);
			stock.add(index, newAmount);
		} else if(tblType == 1){ // if salesfile table, change sQuantity
			price.remove(index);
			price.add(index, newPrice);
			sPrice.remove(index);
			sPrice.add(index, newPrice);
			sQuantity.remove(index);
			sQuantity.add(index, newAmount);
		}
	}

	public static void showAdmin(){
		Login.admin.setVisible(true);

		/* Login creates the instance of Admin and stores it in an object.
		 * We can't use the 'this' keyword in this static context perhaps because the instance of this class is non-static. (?)
		 * Through this, this class accesses itself and calls its own method statically.
		 *
		 * Why not use a non-static method? Because we need this to be static for signUp to use. Instantiating this class just to
		 * call a non-static method from the signUp class is redundant. Thus, we do it with this static method.
		 *
		 * Might be bad practice. Not sure. But it works.
		 */
	}

	public static void main(String[] args){
		Admin admin = new Admin();
	}

	public void keyTyped(KeyEvent e){}
	public void keyPressed(KeyEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}

}