import java.io.*; // Used for FileReader and PrintWriter
import java.nio.file.*; // Used for "Path" datatype and the "Paths.get()" function
import java.util.*; // Used for ArrayLists, Array functions, and the Scanner function
import javax.swing.*; // Used for JOptionPane functions and adding components (JLabel, JButton, etc)
import javax.swing.table.*; // Used for TableModels, which allow table editing functions
import java.awt.*; // Used for container, Color functions, Font functions, and component editing
import java.awt.event.*; // Used for listeners which are added to Swing components
import java.text.*; // Used for the DecimalFormat function

// EDIT MORE CLEANLY: JCreator -> Edit -> Disable Auto Folding -> Collapse Child Folds 01

public class Apollo extends JFrame implements ActionListener, MouseListener {
	// These ArrayLists hold masterfile JTable values
	ArrayList<String> id = new ArrayList<String>();
	ArrayList<Double> price = new ArrayList<Double>();
	ArrayList<Integer> stock = new ArrayList<Integer>();
	ArrayList<String> desc = new ArrayList<String>();
	// These ArrayLists hold cart JTable values
	ArrayList<String> pID = new ArrayList<String>();
	ArrayList<Double> pPrice = new ArrayList<Double>();
	ArrayList<Integer> pQuantity = new ArrayList<Integer>();
	ArrayList<String> pDesc = new ArrayList<String>();
	// These ArrayLists hold sales values from salesfile.txt
	ArrayList<String> sID = new ArrayList<String>();
	ArrayList<Double> sPrice = new ArrayList<Double>();
	ArrayList<Integer> sQuantity = new ArrayList<Integer>();
	ArrayList<String> sDesc = new ArrayList<String>();
	// DOES NOT USE sCategory, but algorithm can be copied from Admin.java if products are dynamically created in the future (katamad na i-edit)
	ArrayList<String> passwords = new ArrayList<String>();

	ImageIcon[] imgProduct = {
		new ImageIcon("guitars/g1.jpg"), new ImageIcon("guitars/g2.jpg"), new ImageIcon("guitars/g3.jpg"),
		new ImageIcon("guitars/g4.jpg"), new ImageIcon("guitars/g5.jpg"), new ImageIcon("drums/d1.jpg"),
		new ImageIcon("drums/d2.jpg"), new ImageIcon("drums/d3.jpg"), new ImageIcon("drums/d4.jpg"),
		new ImageIcon("drums/d5.jpg"), new ImageIcon("amplifiers/am1.jpg"), new ImageIcon("amplifiers/am2.jpg"),
		new ImageIcon("amplifiers/am3.jpg"), new ImageIcon("amplifiers/am4.jpg"), new ImageIcon("amplifiers/am5.jpg"),
		new ImageIcon("interfaces/i1.jpg"), new ImageIcon("interfaces/i2.jpg"), new ImageIcon("interfaces/i3.jpg"),
		new ImageIcon("interfaces/i4.jpg"), new ImageIcon("interfaces/i5.jpg"), new ImageIcon("acoustics/ac1.jpg"),
		new ImageIcon("acoustics/ac2.jpg"), new ImageIcon("acoustics/ac3.jpg"), new ImageIcon("acoustics/ac4.jpg"),
		new ImageIcon("acoustics/ac5.jpg")
	}; // ImageIcon array which holds image files
	ImageIcon imgLogo = new ImageIcon("misc/banner03.png");

	String category[] = {"GUITARS", "DRUMS", "AMPLIFIERS", "INTERFACES", "ACOUSTICS"}; // Categories to be used for JComboBox 'cbCategory'
	String tblHeader[] = {"ID", "Description", "Stock", "Price"}; // Table headers to be used for JTables
	String tblHeaderCart[] = {"ID", "Description", "Quantity", "Price"}; // Cart table header
	String currentCategory; // Comparator for category[] array
	int idNo; // Parsed integer version of ID number, for use in addToCart() method
	int index; // Index of inputted ID number (from addToCart()) in the ArrayList 'id'
	int pIndex; // Index of active item in cart JTable
	int sIndex; // Index of active item being added in salesfile (used in addToSalesfile)
	int holder; // Temporarily holds pQuantity/stocks value to subtract/add and then be added to table
	int newItemIndex; // Index of the next blank row to be filled when new item is entered.
	// ALL INTEGERS ABOVE ARE ESSENTIAL TO THE addToCart() METHOD.
	int txtIDindex; // Index of inputted ID number, but for use with setDescriptionID();
	int i; // Universal iterator, use this in 'for' loops
	int selectedRow; // Integer value of the row selected by user through JTable mouse click
	int min; // Lowest index of a selected category, is used in for loop in setDescription()
	int max; // Highest index of a selected category, is used in for loop in setDescription()
	int enterCount; // Counts how many times 'enter' is pressed through txtID, used in txtIDwaiter()
	String prevID; // Compares current ID in txtID to the previous one entered, used in txtIDwaiter()
	int idEdit; // The ID admin enters to edit (used in editCart())
	int pQuantityEdit; // The pQuantity admin enters to edit (used in editCart())
	int affectedpQuantity; // pQuantity of products affected, used for ensuring pQuantity consistency upon value edit (used in editCart())
	double cashTendered; // Cash paid by customer
	double cashTotal; // Total cash in receipt. Both of these values are used in payment() method
	double change; // change = cashTendered - cashTotal

	DecimalFormat money = new DecimalFormat("###,###,##0.00");

	Container c = getContentPane();
	JPanel pLeft = new JPanel();
	JPanel pUp = new JPanel();
	JPanel pDown = new JPanel();
	JPanel pRight = new JPanel();
	JPanel pPassword = new JPanel(); // Panel for admin authentication

	JButton btnAdd = new JButton("buy");
	JButton btnExit = new JButton("Exit");
	JButton btnLogout = new JButton("Logout");
	JButton btnNew = new JButton("New Transaction");
	JButton btnPayment = new JButton("Payment");
	JButton btnEdit = new JButton("Edit Items");
	JComboBox cbCategory = new JComboBox(category);
	JTextField txtID = new JTextField(10);
	JTextField txtTotal = new JTextField(10);
	JPasswordField txtPassword = new JPasswordField(10);
	JTable tblMF = new JTable();
	JTable tblCart = new JTable();
	JScrollPane scrollMF = new JScrollPane(tblMF);
	JScrollPane scrollCart = new JScrollPane(tblCart);
	JLabel lblImg = new JLabel();
	JLabel lblImgLogo = new JLabel();
	JLabel lblGuide = new JLabel("Click ONCE to view description, TWICE to add to cart.");
	JLabel lblID = new JLabel();
	JLabel lblDesc = new JLabel();
	JLabel lblPrice = new JLabel();
	JLabel lblStatus = new JLabel();
	JLabel lblAmountDue = new JLabel("Amount Due:");
	JLabel lblTotal = new JLabel("Php 0.00");
	JLabel lblSearch = new JLabel("Enter Product ID:");
	JLabel lblPassword = new JLabel("Enter administrator password to continue: ");
	JTextArea txtDesc = new JTextArea(10, 10);

	// Tables need to be assigned a model to do edits and changes. //
	// Each table in this program is assigned to a separate table model. //
	DefaultTableModel modelMF = new DefaultTableModel(){
		public boolean isCellEditable(int row, int column){
			return false;
		}
	};
	DefaultTableModel modelCart = new DefaultTableModel(){
		public boolean isCellEditable(int row, int column){
			return false;
		}
	};

	Font fontDue = new Font("Courier New", 0, 16);
	Font fontTotal = new Font("Arial", 1, 20);
	Font fontHead = new Font("Courier New", 1, 16);
	Font fontBody = new Font("Courier New", 0, 14);
	Font fontBodyItalic = new Font("Courier New", 2, 14);
	Font desc1 = new Font("Times New Roman",1, 15);
	Font desc2 = new Font("Times New Roman",1, 15);
	Font desc3 = new Font("Times New Roman",1, 14);

	File filecheck = new File("misc/logo_black.png");

	// PRODUCTS ORDER: 1. GUITAR 2. DRUMS 3. AMPLIFIERS 4. INTERFACES 5. ACOUSTICS

	// ----------------- CONSTRUCTOR METHOD ----------------- //
	public Apollo(){
		//LAYOUT
		c.setLayout(null);
		pLeft.setLayout(null);
		pUp.setLayout(null);
		pDown.setLayout(null);
		pRight.setLayout(null);

		//BUTTON DESIGNS
		btnExit.setIcon(new ImageIcon("misc/exitBtn.png"));
		btnLogout.setIcon(new ImageIcon("misc/logoutBtn.png"));
		btnNew.setIcon(new ImageIcon("misc/newBtn.png"));
		btnPayment.setIcon(new ImageIcon("misc/paymentBtn.png"));
		btnEdit.setIcon(new ImageIcon("misc/editBtn.png"));

		//ADD
		c.add(pUp);
		c.add(pLeft);
		c.add(pRight);
		c.add(pDown);
		c.add(btnAdd);
		c.add(btnExit);
		c.add(btnNew);
		c.add(btnLogout);
		c.add(lblImgLogo);

		pLeft.add(cbCategory);
		pLeft.add(scrollMF);
		pLeft.add(lblGuide);
		pUp.add(lblID);
		pUp.add(lblDesc);
		pUp.add(lblPrice);
		pUp.add(lblStatus);
		pUp.add(lblImg);
		pUp.add(txtID);
		pUp.add(lblSearch);
		pDown.add(btnPayment);
		pDown.add(btnEdit);
		pDown.add(lblAmountDue);
		pDown.add(lblTotal);
		pRight.add(scrollCart);

		//CONTAINER
		pLeft.setBounds(20, 150, 400, 550);
    	pUp.setBounds(450, 150, 370, 400);
    	pDown.setBounds(450, 570, 370, 130);
    	pRight.setBounds(850, 150, 400, 550);
    	btnExit.setBounds(1150, 30, 125, 40);
    	btnNew.setBounds(20, 30, 125, 40);
    	btnLogout.setBounds(1000, 30, 125, 40);
    	lblImgLogo.setBounds(65, -10, 900, 120);

    	//PLEFT
    	cbCategory.setBounds(0, 0, 400, 40);
    	scrollMF.setBounds(0, 40, 400, 700);
    	lblGuide.setBounds(45, 500, 300, 40);

    	//PUP
    	lblID.setBounds(35, 250, 300, 30);
    	lblDesc.setBounds(35, 270, 300, 30);
    	lblPrice.setBounds(35, 290, 300, 30);
    	lblStatus.setBounds(35, 310, 300, 30);
    	lblImg.setBounds(85, 45, 200, 200);
    	txtID.setBounds(140, 370, 100, 20);
    	lblSearch.setBounds(120, 350, 140, 20);

    	//PDOWN
    	btnPayment.setBounds(230, 80, 105, 35);
    	btnEdit.setBounds(230, 25, 105, 35);
    	lblAmountDue.setBounds(30, 25, 200, 50);
    	lblTotal.setBounds(30, 50, 200, 50);

    	//PRIGHT
    	scrollCart.setBounds(0, 0, 400, 550);

    	//COLORS
    	c.setBackground(Color.BLACK);
    	pLeft.setBackground(Color.WHITE);
    	pUp.setBackground(Color.WHITE);
    	pDown.setBackground(Color.WHITE);
    	pRight.setBackground(Color.WHITE);

		//FONT PROPERTIES
		lblID.setHorizontalAlignment(JLabel.CENTER);
		lblDesc.setHorizontalAlignment(JLabel.CENTER);
		lblPrice.setHorizontalAlignment(JLabel.CENTER);
		lblStatus.setHorizontalAlignment(JLabel.CENTER);
		lblGuide.setHorizontalAlignment(JLabel.CENTER);
		lblSearch.setHorizontalAlignment(JLabel.CENTER);
		lblID.setFont(desc3);
		lblDesc.setFont(desc1);
		lblPrice.setFont(fontBody);
		lblStatus.setFont(fontBody);
		lblGuide.setFont(fontBodyItalic);
		lblSearch.setFont(desc2);
		lblAmountDue.setFont(fontDue);
		lblTotal.setFont(fontTotal);

		//LISTENERS
		btnAdd.addActionListener(this);
		btnNew.addActionListener(this);
		btnExit.addActionListener(this);
		btnLogout.addActionListener(this);
		btnEdit.addActionListener(this);
		btnPayment.addActionListener(this);
		cbCategory.addActionListener(this);
		txtID.addActionListener(this);
		tblMF.addMouseListener(this);

		//MISC PROPERTIES
		tblMF.setModel(modelMF);
		tblCart.setModel(modelCart);
		modelMF.setColumnIdentifiers(tblHeader);
		modelCart.setColumnIdentifiers(tblHeaderCart);
		tblMF.getTableHeader().setReorderingAllowed(false);
		tblMF.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblCart.getTableHeader().setReorderingAllowed(false);
		tblCart.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lblImgLogo.setIcon(imgLogo);
		tblMF.getTableHeader().setBackground(Color.WHITE);
		tblCart.getTableHeader().setBackground(Color.WHITE);
		scrollMF.getViewport().setBackground(Color.WHITE);
		scrollCart.getViewport().setBackground(Color.WHITE);
		cbCategory.setBackground(Color.BLACK);
		cbCategory.setForeground(Color.WHITE);

		pPassword.setLayout(new BoxLayout(pPassword, BoxLayout.Y_AXIS));
		pPassword.add(lblPassword);
		pPassword.add(txtPassword);

		setLocation(320, 180);
		setUndecorated(true);
		setSize(1280, 720);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		loadMasterfile();
		loadSalesfile();
		loadAdminPasswords();
		initializeValues();
	}

	// ----------------- LISTENER METHODS ----------------- //
	public void actionPerformed(ActionEvent e){
		Object src = e.getSource();

		if(src == btnNew){
			if(pQuantity.get(0) == 0){
				JOptionPane.showMessageDialog(null, "Error! No items in cart.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to make a new transaction? Existing cart items will be returned.", "New Transaction", JOptionPane.WARNING_MESSAGE);

				if(adminAuthenticate() == true){
					if(confirm == JOptionPane.YES_OPTION){
						returnValues();
					}
				}
			}

		}

		if(src == btnLogout){
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out? Existing cart items will be returned.", "Log Out", JOptionPane.WARNING_MESSAGE);

			if(confirm == JOptionPane.YES_OPTION){
				Login.showLogin();
				returnValues();
				saveMasterfile();
				saveSalesfile();
				this.dispose();
			}
		}

		if(src == btnExit){
			int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit? Existing cart items will be returned.", "Exit", JOptionPane.WARNING_MESSAGE);

			if(confirm == JOptionPane.YES_OPTION){
				returnValues();
				saveMasterfile();
				saveSalesfile();
				System.exit(0);
			}
		}
		if(src == btnEdit){
			if(pQuantity.get(0) == 0){
				JOptionPane.showMessageDialog(null, "Error! No items in cart.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				if(adminAuthenticate() == true){
					editCart();
				}
			}
		}
		if(src == btnPayment){
			if(pQuantity.get(0) == 0){
				JOptionPane.showMessageDialog(null, "Error! No items in cart.", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				payment();
			}
		}

		// INDEXES: 0-4 GUITARS, 5-9 DRUMS, 10-14 AMPS, 15-19 INT, 20-24 ACOUSTICS
		if(src == cbCategory){
			setTblMF();
		}

		if(src == txtID){
			txtIDwaiter(txtID.getText());
		}
	} // Listener for JButton, JCombobox and JTextField

	public void mouseClicked(MouseEvent e){
		if(e.getSource() == tblMF){
			selectedRow = tblMF.getSelectedRow();
			if(e.getClickCount() == 1){ //SET DESC
				setDescription();
			} else if(e.getClickCount() == 2){ //INCREMENT BY 1
				addToCart(modelMF.getValueAt(selectedRow, 0).toString(), 1); //BASED ON ID
				setTblMF();
			}
		}
	} // Listener for JTable

	// ----------------- USER METHODS----------------- // (Fold/minimize the blocks under this area to make everything organized and accessible.)

	// STARTUP METHODS //
	public void initializepQuantity(){
		for(int i = 0; i < 30; i++){ //INITIALIZE pQuantity: add empty elements so addToCart() can simply replace them later
			pQuantity.add(i, 0);
		}
	} // Separated this because we will call this upon clearing the cart -- cleared lists are empty, so we can't replace elements.

	public void initializeValues(){ //initialize/define values upon startup
		initializepQuantity();

		for(i = 0; i <= 4; i++){ //DEFINE DEFAULT TABLE VALUES
			currentCategory = "GUITARS"; // for use with method setDescription()
			modelMF.addRow(new Object[]{id.get(i), desc.get(i), stock.get(i), money.format(price.get(i))}); //add new set of products accdng to category
		}
		lblImg.setIcon(imgProduct[0]); //DEFINE DEFAULT LABEL VALUES
		lblID.setText(id.get(0));
		lblDesc.setText(desc.get(0));
		lblPrice.setText(Double.toString(price.get(0)));
	} // Display default JTable contents and item descriptions upon startup (runs once only)

	// TXTFILE METHODS // (HANDLES LOADING, EDITING AND SAVING OF MASTERFILE & SALESFILE)
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
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "loadMasterFile(): " + e);
		}

	} // Transfers data from masterfile.txt and assigns values to variables

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
	} // Saves the changes done to the masterfile table and reflects it on the masterfile.txt

	public void returnValues(){
		for(i = 0; i < pID.size(); i++){
			index = id.indexOf(pID.get(i));

			holder = pQuantity.get(i);
			holder = holder + stock.get(index);

			stock.remove(index);
			stock.add(index, holder);
		}

		clearCart();
	} // Returns cart quantities to masterfile when closing without saving

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
			//System.out.println("\n\n" + sID + "\n" + sPrice + "\n" + sQuantity + "\n" + sDesc);
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "loadSalesFile(): " + e);
		}
	}

	public void saveSalesfile(){
		try{
			PrintWriter salesfileTemp = new PrintWriter("temp/salesfile.txt");
			Path source = Paths.get("temp/salesfile.txt");
			Path target = Paths.get("salesfile.txt");

			//rewriting the masterfile with the performed changes in a temporary file
			for(i = 0; i < id.size(); i++){
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
			JOptionPane.showMessageDialog(null, "saveSalesfile(): " + e);
		}
	}

	public void loadAdminPasswords(){
		try{
			Scanner loginfile = new Scanner(new FileReader("login.txt"));
			int currentType;

			while(loginfile.hasNext()){
				currentType = loginfile.nextInt();
				loginfile.nextLine();
				loginfile.nextLine();
				if(currentType == 1){
					passwords.add(loginfile.nextLine());
				} else {
					loginfile.nextLine();
				}
			}

			loginfile.close();
		} catch (Exception e){
			JOptionPane.showMessageDialog(null, "loadAdminPasswords(): " + e);
		}
	}

	// DESCRIPTION/DISPLAY METHODS //
	public void setTblMF(){ //set products on table
		modelMF.setRowCount(0); //clear current table as new category is chosen

		if(cbCategory.getSelectedItem().equals("GUITARS")){
			for(i = 0; i <= 4; i++){
				currentCategory = "GUITARS"; // for use with method setDescription()
				modelMF.addRow(new Object[]{id.get(i), desc.get(i), stock.get(i), money.format(price.get(i))}); //add new set of products accdng to category
			}
		} else if(cbCategory.getSelectedItem().equals("DRUMS")){
			for(i = 5; i <= 9; i++){
				currentCategory = "DRUMS";
				modelMF.addRow(new Object[]{id.get(i), desc.get(i), stock.get(i), money.format(price.get(i))});
			}
		} else if(cbCategory.getSelectedItem().equals("AMPLIFIERS")){
			for(i = 10; i <= 14; i++){
				currentCategory = "AMPLIFIERS";
				modelMF.addRow(new Object[]{id.get(i), desc.get(i), stock.get(i), money.format(price.get(i))});
			}
		} else if(cbCategory.getSelectedItem().equals("INTERFACES")){
			for(i = 15; i <= 19; i++){
				currentCategory = "INTERFACES";
				modelMF.addRow(new Object[]{id.get(i), desc.get(i), stock.get(i), money.format(price.get(i))});
			}
		} else if(cbCategory.getSelectedItem().equals("ACOUSTICS")){
			for(i = 20; i <= 24; i++){
				currentCategory = "ACOUSTICS";
				modelMF.addRow(new Object[]{id.get(i), desc.get(i), stock.get(i), money.format(price.get(i))});
			}
		}

		tblMF.revalidate(); //.revalidate() makes sure that the table is graphically refreshed
	} // Sets masterfile JTable according to JCombobox category, also reflects values of id, price, stock & desc

	public void setTblCart(){
		modelCart.setRowCount(0); //clear the table so we can repopulate it with updated data

		for(i = 0; i < pID.size(); i++){
			modelCart.addRow(new Object[]{pID.get(i), pDesc.get(i), pQuantity.get(i), money.format(pPrice.get(i) * pQuantity.get(i))});
		}

		tblCart.revalidate();
		displayTotal();
	} // Sets cart JTable, also reflects values of pID, pPrice, pQuantity & desc

	public void setDescription(){ //img included, lblStatus out of stock display is at method addToCart()
		//INDEXES: 0-4 GUITARS, 5-9 DRUMS, 10-14 AMPS, 15-19 INT, 20-24 ACOUSTICS
		lblStatus.setText("");
		switch(currentCategory){
			case "GUITARS":
				min = 0;
				max = 4;
				break;
			case "DRUMS":
				min = 5;
				max = 9;
				selectedRow += (5); //selectedRow only from 0-5, add 5 per category so selectedRow matches index of masterfile
				break;
			case "AMPLIFIERS":
				min = 10;
				max = 14;
				selectedRow += (5*2);
				break;
			case "INTERFACES":
				min = 15;
				max = 19;
				selectedRow += (5*3);
				break;
			case "ACOUSTICS":
				min = 20;
				max = 24;
				selectedRow += (5*4);
				break;
			default:
				break;
		}

		for(i = min; i <= max; i++){
			if(selectedRow == i){
				lblID.setText(id.get(i));
				lblDesc.setText(desc.get(i));
				lblPrice.setText(Double.toString(price.get(i)));
				lblImg.setIcon(imgProduct[i]);

				if(stock.get(i) <= 0){
					lblStatus.setText("Out of stock!");
				} else {
					lblStatus.setText("");
				}
			}
		}
	} // Sets description based on the masterfile JTable element chosen, is called when clicking on table itself

	public void setDescriptionID(String input){
		index = id.indexOf(input);
		lblID.setText(id.get(index));
		lblDesc.setText(desc.get(index));
		lblPrice.setText(Double.toString(price.get(index)));
		lblImg.setIcon(imgProduct[index]);
	} // Sets description based on ID searched through txtID JTextArea component

	public void displayTotal(){
		cashTotal = 0; //reset total

		for(i = 0; i < pID.size(); i++){
			//cashTotal += Double.parseDouble(modelCart.getValueAt(i, 1).toString()) * Integer.parseInt(modelCart.getValueAt(i, 2).toString());
			cashTotal += pQuantity.get(i) * pPrice.get(i);
		}
		lblTotal.setText("Php " + money.format(cashTotal));
	} // Displays the total price using JLabel lblTotal; setTblCart calls this method automatically for cleanliness

	// PROCESS METHODS
	public boolean adminAuthenticate(){
		String password = "";

		txtPassword.setText("");

		//int result = JOptionPane.showConfirmDialog(null, "Enter administrator password to continue:" + txtPass, "Authentication", JOptionPane.INFORMATION_MESSAGE);
		int result = JOptionPane.showConfirmDialog(null, pPassword,"Enter Password", JOptionPane.OK_CANCEL_OPTION);

		if(result == JOptionPane.YES_OPTION){
			password = txtPassword.getText();
		}

		if(passwords.contains(password)){
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Error! Invalid administrator password.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public void addToCart(String input, int amount){ //MF = masterfile
		try{
			idNo = Integer.parseInt(input);

			if(id.contains(input)){
				index = id.indexOf(input); //get index of current ID being processed

				if(!(stock.get(index) <= 0)){ //if there is stock
					holder = stock.get(index); //reduce stock from MF
					holder -= amount;
					stock.remove(index); //essential, used for replacing previous values at index
					stock.add(index, holder);

					if(pID.contains(Integer.toString(idNo))){ //if product already in cart
						pIndex = pID.indexOf(input); //get index of existing item from cart based on the user input
						holder = pQuantity.get(pIndex); //get pQuantity of item being processed, then store it in holder variable
						holder += amount; //add holder (temporary pQuantity) by +1
						pQuantity.remove(pIndex); //remove the previous pQuantity stored in the pIndex (index of current item)
						pQuantity.add(pIndex, holder); //add the new pQuantity value (which was stored in holder) to pIndex, effectively replacing it

						//NOTE: we cannot do a shortcut of pQuantity.get(pIndex)++; because of type mismatch (ArrayLists =/= arrays)
					} else { //if new product
						pIndex = newItemIndex; // pIndex in the context of a new item is equal to the blank available index

						pID.add(pIndex, id.get(index)); //add new ID at blank cart index -- list.add(index, object);
						pDesc.add(desc.get(index)); //carryover desc, price
						pPrice.add(price.get(index));

						holder = pQuantity.get(pIndex);
						holder += amount;
						pQuantity.remove(pIndex);
						pQuantity.add(pIndex, holder);
						newItemIndex++; //prepare for new blank cart index
					}

					setTblMF();
					setTblCart();

					lblStatus.setText("Added to cart!");
				} else {
					lblStatus.setText("Out of stock!");
				}
			}
		} catch (Exception e){
			System.out.println("Error: no input");
			System.out.println(e);
		}
	} // Add to card algorithm, tables are also refreshed in this method

	public void addToSalesfile(){
		for(i = 0; i < pID.size(); i++){
			sIndex = sID.indexOf(pID.get(i));

			holder = sQuantity.get(sIndex) + pQuantity.get(i);
			sQuantity.remove(sIndex);
			sQuantity.add(sIndex, holder);
			System.out.println(sID.get(sIndex) + ": " + sQuantity.get(sIndex));
		}

		saveMasterfile();
		saveSalesfile();
		clearCart();
	} // Called after successful payment in payment() method, purchased cart items are reflected on the salesfile.

	public void txtIDwaiter(String myID){
		enterCount++;

		if(enterCount == 1){
			setDescriptionID(txtID.getText());
			lblStatus.setText(""); //clear lblStatus from "Added to Cart" messsage
		} else if(enterCount >= 2 && myID.equals(prevID)){
			int myIndex = id.indexOf(myID);
			int amount = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter bulk amount for selected product: ", "Bulk", JOptionPane.PLAIN_MESSAGE));

			if(amount > stock.get(myIndex)){
				JOptionPane.showMessageDialog(null, "Error! Your entered quantity exceeds the store's stock.", "Error!", JOptionPane.ERROR_MESSAGE);
			} else if(amount < 0) {
				JOptionPane.showMessageDialog(null, "Error! Invalid quantity.", "Error!", JOptionPane.ERROR_MESSAGE);
			} else {
				addToCart(txtID.getText(), amount);
				setTblMF();
			}
		} else if(enterCount >= 2 && myID != prevID){
			enterCount = 0;
			txtIDwaiter(txtID.getText());
		}

		prevID = myID;
	} // Allows it so first 'enter' press on txtID shows description first, then second 'enter' press onwards adds the searched product to cart

	public void editCart(){
		String input;

		try{
			input = JOptionPane.showInputDialog(null, "Enter the ID of the product you wish to edit.", "Edit", JOptionPane.PLAIN_MESSAGE);

			if(input != null){ // if there is an input
				idNo = Integer.parseInt(input);

				if(pID.contains(Integer.toString(idNo))){
					editpQuantity();
				} else {
					JOptionPane.showMessageDialog(null, "Error! This ID does not exist in the cart.", "Error!", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch(NullPointerException e){
			//NullPointerException triggers when input dialog is closed without input. We skip this exception and try to catch other exceptions that may appear.
		} catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, "Error! Enter only numerical digits", "Error", JOptionPane.ERROR_MESSAGE);
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "Oh no! " + e, "Error", JOptionPane.ERROR_MESSAGE); //resolve immediately if flow reaches here
			editCart();
		}

		setTblMF();
		setTblCart(); //refresh tables to reflect the new values
	} // Process to edit the quantities in cart and return the items to stock

	public void editpQuantity(){
		pIndex = pID.indexOf(Integer.toString(idNo)); //syntax: int = List.indexOf(string);
		index = id.indexOf(Integer.toString(idNo)); //get the indexes of the inputted ID from their respective ArrayLists for use in the alogrithm

		holder = pQuantity.get(pIndex);

		pQuantityEdit = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the new quantity for " + pDesc.get(pIndex), "Edit", JOptionPane.PLAIN_MESSAGE));

		if(pQuantityEdit > (stock.get(index) + pQuantity.get(pIndex))){
			JOptionPane.showMessageDialog(null, "Error! Your new value exceeds the store's stock", "Error", JOptionPane.ERROR_MESSAGE);
		} else if(pQuantityEdit < 0){
			JOptionPane.showMessageDialog(null, "Error! Cannot enter a negative integer.", "Error", JOptionPane.ERROR_MESSAGE);
			editpQuantity();
		} else {
			if(pQuantityEdit == 0){
				int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this item from the cart?", "Confirm", JOptionPane.WARNING_MESSAGE);

				if(reply == JOptionPane.YES_OPTION){ //if admin wants to delete, return cart item quantities to the masterfile table
					pID.remove(pIndex);
					pPrice.remove(pIndex);
					pQuantity.remove(pIndex);
					pDesc.remove(pIndex); //remove traces of the products from the cart arrays

					holder += stock.get(index); //holder alrady holds the pQuantity of cart items before deleting, so we add it to stock of current item to return it

					stock.remove(index);
					stock.add(index, holder); //add the returned items to the item's stock
					JOptionPane.showMessageDialog(null, "Item deleted!", "Deleted", JOptionPane.PLAIN_MESSAGE);

					newItemIndex--; //reduce the index of a new item since an index has been deleted (this is used in addToCart())
				} else {
					editpQuantity();
				}
			} else if(pQuantityEdit > holder || pQuantityEdit < holder){ //simply edit the values on both tables
				affectedpQuantity = pQuantityEdit - holder;

				holder += affectedpQuantity;

				pQuantity.remove(pIndex);
				pQuantity.add(pIndex, holder);

				holder = stock.get(index);

				holder -= affectedpQuantity;

				stock.remove(index);
				stock.add(index, holder);
			} else if(pQuantityEdit == holder){ //if pQuantity does not change, show error msg
				JOptionPane.showMessageDialog(null, "Error! This item has that pQuantity already.", "Error!", JOptionPane.ERROR_MESSAGE);
			}
		}

		lblStatus.setText("");
	} // Subprocess of editCart, placed into a new method for cleanliness & to allow recursion (method calling itself)

	public void payment(){
		try{
			cashTendered = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter cash: ", "Payment", JOptionPane.PLAIN_MESSAGE));

			if(cashTendered < cashTotal){
				JOptionPane.showMessageDialog(null, "Error! Insufficient amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
				payment();
			} else { // Successful payment
				Receipt receipt = new Receipt();

				for(i = 0; i < pID.size(); i++){
					System.out.println("HELLO");
					receipt.processProducts(pID.get(i), pPrice.get(i), pQuantity.get(i), pDesc.get(i));
				}
				receipt.processDetails(cashTotal, cashTendered);
				addToSalesfile();
			}
		} catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, "Error! Enter a valid currency amount.", "Error!", JOptionPane.ERROR_MESSAGE);
		} catch(NullPointerException e){
			// JOptionPane cancel = null; skip this exception
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "payment(): " + e);
		}
	} // Processes payment before printing receipt

	public void clearCart(){
		pID.clear();
		pDesc.clear();
		pQuantity.clear();
		pPrice.clear();
		initializepQuantity();
		newItemIndex = 0;
		modelCart.setRowCount(0);
		setTblMF();
		setTblCart();
	} // Called upon new or successful transaction

	// TEST METHODS //
	public void fileChecker(){ //call only when testing
		if(filecheck.exists()){
			lblStatus.setText("File exists");
		} else {
			lblStatus.setText("Does not exist");
		}
	}

	// ----------------- MAIN METHOD----------------- //
	public static void main(String[] args) {
		Apollo app = new Apollo();
		//call the startup methods
	}

	// ----------------- UNUSED METHODS ----------------- //
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){} // These are all required to be called when using MouseListener. We do not need them, so we just leave them blank.
}