import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.util.*;
import java.io.*;
import java.text.*;
import java.lang.*;

public class Receipt extends JFrame implements ActionListener{
	ArrayList<String> pID = new ArrayList<String>();
	ArrayList<Double> pPrice = new ArrayList<Double>();
	ArrayList<Integer> pQuantity = new ArrayList<Integer>();
	ArrayList<String> pDesc = new ArrayList<String>();

	ArrayList<Character> descHolder = new ArrayList<Character>();
	ArrayList<Character> priceHolder = new ArrayList<Character>();
	ArrayList<Character> quantityHolder = new ArrayList<Character>();
	ArrayList<Character> amountHolder = new ArrayList<Character>();

	double change = 0;
	double total;
	double totalExcl;
	double cash = 0;
	double vat = 0;
	double amountDoub;
	int productLength;
	int i;
	int j;
	int plusSize; // additional size for long receipts

	DecimalFormat money = new DecimalFormat("###,###,##0.00");

	JButton btnPrint = new JButton("Print");
	JPanel pReceipt = new JPanel();
	JLabel lblImg = new JLabel();
	JTextArea receipt = new JTextArea();
	JScrollPane scrollReceipt = new JScrollPane(pReceipt);

	ImageIcon imgLogo = new ImageIcon("misc/logo_white150x100.png");
	File myFile = new File("misc/logo_white150.png");

	Font fontHeader = new Font("Monospaced", Font.PLAIN, 16);
	Font fontBody = new Font("Monospaced", Font.BOLD, 12);

	Date date = new Date();
	DateFormat dateFormat;
	String strDate;

	public void p(Object s){
		System.out.println(s);
	}

	public void fileCheck(){
		if(myFile.exists()){
			p("File exists");
		} else {
			p("File does not exist");
		}
	}

	public Receipt(){
		Container c = getContentPane();
		c.setLayout(null);

		pReceipt.setLayout(null);
		pReceipt.setPreferredSize(new Dimension(500, 800));
		pReceipt.setBackground(Color.WHITE);
		pReceipt.add(lblImg);
		pReceipt.add(receipt);

		lblImg.setBounds(160, 0, 150, 100);
		lblImg.setIcon(imgLogo);

		// textpane attribute editing
		/*
		StyledDocument doc = receipt.getStyledDocument(); //receipt model
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false); //same concept with jtable and its model, need the model to edit further (?)
		*/

		receipt.setBounds(10, 110, 465, 700);
		receipt.setFont(fontBody);
		receipt.setLineWrap(true);
        receipt.setWrapStyleWord(true);
        receipt.setEditable(false);

		btnPrint.setBounds(175, 915, 150, 30);
		btnPrint.addActionListener(this);
		c.add(btnPrint);

		// Date & Time
		date = Calendar.getInstance().getTime();
		dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		strDate = dateFormat.format(date);

		scrollReceipt.setBounds(0, 0, 500, 900);
		//scrollReceipt.setPreferredSize(new Dimension(500, 900));
        scrollReceipt.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollReceipt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		c.add(scrollReceipt);

		setResizable(false);
		setLocation(760, 20);
		setVisible(true);
		setTitle("Receipt");
		setSize(515, 1000);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);

		fileCheck();

		//testing methods:
		//testValues();
		//updateReceipt();
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == btnPrint){
			dispose();
		}
	}

	public void processProducts(String id, double price, int quantity, String desc){
		pID.add(id);
		pPrice.add(price);
		pQuantity.add(quantity);
		pDesc.add(desc);
	} // import product details to this program, runs in a loop in Apollo.java

	public void processDetails(double totalPrice, double cashTendered){
		total = totalPrice;
		cash = cashTendered;
		change = cash - total;
		//p("total: " + total + " cash: " + cash + " change: " + change);
		productLength = pID.size();
		setReceiptSize();
		updateReceipt();
	} // LATEST UPDATE FOR THIS FILE, RESERVE DOING IMPORTANT STUFF AFTER THIS METHOD (println stuff & updateReceipt)

	public void setReceiptSize(){
		if(productLength > 6){
			for(i = 0; i < productLength - 6; i++){ // for every product after productLength = 6
				plusSize += 25;
			}

			pReceipt.setPreferredSize(new Dimension(500, 800 + plusSize));
			receipt.setSize(465, 700 + plusSize);
		}
	}

	public void updateReceipt(){ // 66 CHARACTERS PER LINE


		try{
			writeCenter("Apollo Music Shop");
			writeCenter("143 Lombard Street");
			writeCenter("De La Salle University - Dasmarinas");
			writeCenter("(046) 5123-187");
			receipt.append(" ---------------------------------------------------------------- \n");
			writeEndToEnd("Date:", strDate);
			writeEndToEnd("OR:", "0000000000000" + randomNumbers(1000000));
			writeEndToEnd("Staff: H. Mungus", "Trans: " + randomNumbers(100000));
			receipt.append(" ---------------------------------------------------------------- \n");
			receipt.append("  Description                          Price  Qty         Amount\n");
			receipt.append(" ---------------------------------------------------------------- \n");

			for(j = 0; j < pID.size(); j++){ //using 'j' as iterator because 'i' conflicts with the method call containing it already
				amountDoub = pQuantity.get(j) * pPrice.get(j);

				receipt.append("  ");
				dynamicReceiptGen(pDesc.get(j), money.format(pPrice.get(j)).toString(), pQuantity.get(j).toString(), String.valueOf(money.format(amountDoub)));
				receipt.append("\n");
			}

			vat = total * .12;
			totalExcl = total - vat;

			p("total: " + total + " cash: " + cash + " change: " + change);

			receipt.append(" ---------------------------------------------------------------- \n");
			writeEndToEnd("Gross Sales", String.valueOf(money.format(total)));
			writeEndToEnd("Price Excl. of VAT", String.valueOf(money.format(totalExcl)));
			writeEndToEnd("Add: VAT (12%)", String.valueOf(money.format(vat)));
			writeEndToEnd("TOTAL AMOUNT", String.valueOf(money.format(total)));
			writeEndToEnd("Amount Tendered Cash", String.valueOf(money.format(cash)));
			writeEndToEnd("C H A N G E", String.valueOf(money.format(change)));
			receipt.append(" ---------------------------------------------------------------- \n");
			writeThreeRows("", "Net Amount", "VAT");
			writeThreeRows("VATable Sales", String.valueOf(money.format(totalExcl)), String.valueOf(money.format(vat)));
			writeThreeRows("VAT Exempt Sales", "0.00", "0.00"); // 0 because VAT exempt sales are only for necessities
			writeThreeRows("Zero-Rated Sales", "0.00", "0.00"); // 0 because this is for exports or something
			receipt.append(" ---------------------------------------------------------------- \n");
			writeCenter("This serves as your OFFICIAL RECEIPT.");
			receipt.append(" ---------------------------------------------------------------- \n");
			receipt.append("  POS Provider:\n");
			receipt.append("  Bariso, Brylle Anthony\n");
			receipt.append("  Bartolata, John Carlo\n");
			receipt.append("  Esquivias, Peter Paul\n");
			receipt.append("  Julag-ay, Sean Austin\n");
			receipt.append("  BCS 11\n");
			receipt.append(" ---------------------------------------------------------------- \n");
			writeCenter("THIS RECEIPT SHALL BE VALID FOR FIVE (5) YEARS");
			writeCenter("FROM THE DATE OF THE PERMIT TO USE.");
			receipt.append(" ---------------------------------------------------------------- \n");

			//receipt.append();
		} catch (Exception e){
			p(e);
		}
	}

	public int randomNumbers(int max){
		Random randomizer = new Random();
		int randNum = randomizer.nextInt(max);

		return randNum;
	}

	// everything working here below is zero-based (especially in for-loops)
	// !!! THERE ARE 66 CHARACTERS (or 67 idk) IN ONE LINE !!!
	// wow manual positioning turbo c ka gh0rl

	public void writeCenter(String mString){
		int mStrLength;
		int whitespaceLength; // whitespace to the left (presumably equal to right)

		// centering formula: (width / 2) - (componentsize / 2)

		mStrLength = mString.length();
		whitespaceLength = 33 - (mStrLength / 2);

		if(mStrLength% 2 != 0){
			whitespaceLength -= 1;
			p("length is odd");
		}

		for(i = 0; i < whitespaceLength; i++){
			receipt.append(" ");
		}

		receipt.append(mString);
		receipt.append("\n");
	}

	public void writeThreeRows(String lString, String mString, String rString){
		int lStrLength;
		int mStrLength;
		int rStrLength;
		int lWhitespaceLength;
		int rWhitespaceLength;

		lStrLength = lString.length();
		mStrLength = mString.length();
		rStrLength = rString.length();
		lWhitespaceLength = 45 - (lStrLength + mStrLength);

		receipt.append("  ");
		receipt.append(lString);

		//middle text
		for(i = 0; i < 45 - (lStrLength + mStrLength); i++){ // 45 - last character position for middle text
			receipt.append(" ");
		}

		receipt.append(mString);

		for(i = 0; i < 17 - rStrLength; i++){ // 16 - length from mString last char to rString last char
			receipt.append(" ");
		}

		receipt.append(rString);
		receipt.append("\n");
	}

	public void writeEndToEnd(String lString, String rString){
		int lStrLength;
		int rStrLength;
		int whitespaceLength;

		lStrLength = lString.length();
		rStrLength = rString.length();

		whitespaceLength = 66 - (lStrLength + rStrLength); // 66 - characters per line

		receipt.append("  ");
		receipt.append(lString);
		for(i = 0; i < whitespaceLength - 4; i++){
			receipt.append(" ");
		}
		receipt.append(rString);
		receipt.append("\n");
	}

	public void dynamicReceiptGen(String description, String price, String quantity, String amount){
		int remainingChars;

		descHolder.clear();

		/* The individual letters of the details are stored in an arraylist so we can
		 * control the output. When the description gets too long we can append a '...' a the end */

		for(i = 0; i < description.length(); i++){
			descHolder.add(description.charAt(i));
		}
		for(i = 0; i < price.length(); i++){
			priceHolder.add(price.charAt(i));
		}
		for(i = 0; i < quantity.length(); i++){
			quantityHolder.add(quantity.charAt(i));
		}

		// PRODUCT NAME
		if(description.length() > 31){ // 31 = max chars for product desc, 34 total characters
			for(i = 0; i < 28; i++){
				receipt.append(descHolder.get(i).toString());
			}
			receipt.append("...");
		} else { // if description length is shorter than 32, add extra spaces until it does
			for(i = 0; i < description.length(); i++){
				receipt.append(descHolder.get(i).toString());
			}

			remainingChars = 31 - description.length();

			for(i = 0; i < remainingChars; i++){
				receipt.append(" ");
			}

		}

		// PRODUCT PRICE
		if(price.length() == 10){
			receipt.append(" ");
		} else if(price.length() == 9){
			receipt.append("  ");
		} else if(price.length() == 8){
			receipt.append("   ");
		}

		receipt.append(price);

		// PRODUCT QUANTITY
		if(quantity.length() == 1){
			receipt.append("    ");
		} else if(quantity.length() == 2){
			receipt.append("   ");
		} else if(quantity.length() == 3){
			receipt.append("  ");
		}

		receipt.append(quantity);

		// PRODUCT AMOUNT
		if(amount.length() == 13){
			receipt.append("  ");
		} else if(amount.length() == 12){
			receipt.append("   ");
		} else if(amount.length() == 11){
			receipt.append("    ");
		} else if(amount.length() == 10){
			receipt.append("     ");
		} else if(amount.length() == 9){
			receipt.append("      ");
		} else if(amount.length() == 8){
			receipt.append("       ");
		} // hard-coded na lang pagod na aq mag-isip xori

		receipt.append(amount);

	} // dynamically layouts details to receipt

	public void testValues(){
		total = 69420;

		pID.add("1001");
		pID.add("1002");
		pID.add("2004");
		pPrice.add(129000.0);
		pPrice.add(32400.0);
		pPrice.add(95000.0);
		pQuantity.add(4);
		pQuantity.add(16);
		pQuantity.add(109);
		pDesc.add("Epiphone Les Paul Standard");
		pDesc.add("Fender Vintera '60s Telecaster Modified");
		pDesc.add("PDP Player 5-Piece");
	}

	public static void main(String[] args){
		new Receipt();
	}

}