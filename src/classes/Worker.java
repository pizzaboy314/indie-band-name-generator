package classes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Worker extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame resultFrame;
	private static int resultFrameBoundX;
	private static int resultFrameBoundY;
	private static int resultFrameBoundWidth;
	private static int resultFrameBoundHeight;
	
	private static JTextArea resultText;
	private static JFileChooser fc;
	private static File resultFile;
	private static String resultString;
	
	private static List<String> nounsList;
	private static List<String> adjsList;
	private static List<String> lastNamesList;
	private static List<String> firstMNamesList;
	private static List<String> firstFNamesList;
	
	private static List<String> grammars;
	
	private static Integer numNames;
	private static Integer numNamesInit = 10;
	
	private static Random rand;
	
	public static void main(String[] args) {
		resultWindow();
		
		rand = new Random();
		
//		String numString = (String) JOptionPane.showInputDialog(null,
//				"Enter number of band names to generate: ", "enter number of band names to generate",
//				JOptionPane.PLAIN_MESSAGE, null, null, null);
//
//		if (numString == null || numString.equals("")) {
//			System.exit(0);
//		} else {
//			numNames = Integer.parseInt(numString);
//		}
		
		numNames = numNamesInit;
		
		populateDictionaries();
		populateGrammars();
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < numNames.intValue(); i++){
			sb.append(generateBandName(i) + "\n");
		}
		
		resultString = sb.toString();
		resultText.setText(resultString);
		resultFrame.setVisible(true);
		resultFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		System.out.println();

	}
	
	public static String generateBandName(long seedInt){
		String grammarString = grammars.get(randInt(0,grammars.size()-1));
		
		String nounString1 = nounsList.get(randInt(0,nounsList.size()-1));
		String adjString1 = adjsList.get(randInt(0,adjsList.size()-1));
		String nounString2 = nounsList.get(randInt(0,nounsList.size()-1));
		String adjString2 = adjsList.get(randInt(0,adjsList.size()-1));
		
		
		String lastNameString = lastNamesList.get(randInt(0,lastNamesList.size()-1));
		String firstMNameString = firstMNamesList.get(randInt(0,firstMNamesList.size()-1));
		String firstFNameString = firstFNamesList.get(randInt(0,firstFNamesList.size()-1));
		
		String name = "";
		
		if(grammarString.equals("AdjNoun")){
			name = adjString1 + " " + nounString1;
		} else if(grammarString.equals("Adj Noun")){
			name = adjString1 + " " + nounString1;
		} else if(grammarString.equals("The Adj Nouns")){
			name = "The " + adjString1 + " " + nounString1 + "s";
		} else if(grammarString.equals("Noun Noun")){
			name = nounString1 + " " + nounString2;
		} else if(grammarString.equals("Male Last and the Adj Nouns")){
			name = firstMNameString + " " + lastNameString + " and the " + adjString1 + " " + nounString1 + "s";
		} else if(grammarString.equals("Female Last and the Adj Nouns")){
			name = firstFNameString + " " + lastNameString + " and the " + adjString1 + " " + nounString1 + "s";
		} else if(grammarString.equals("Adj Noun and the Adj Nouns")){
			name = adjString1 + " " + nounString1 + " and the " + adjString2 + " " + nounString2 + "s";
		} else if(grammarString.equals("Adj Last")){
			name = adjString1 + " " + lastNameString;
		}
		
		return name;
	}
	
	public static void populateGrammars(){
		grammars = new ArrayList<String>();
		
		grammars.add("AdjNoun");
		grammars.add("Adj Noun");
		grammars.add("The Adj Nouns");
		grammars.add("Noun Noun");
		grammars.add("Male Last and the Adj Nouns");
		grammars.add("Female Last and the Adj Nouns");
		grammars.add("Adj Noun and the Adj Nouns");
		grammars.add("Adj Last");
	}
	
	// https://stackoverflow.com/a/20389922
	public static int randInt(int min, int max) {

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	public static void populateDictionaries(){
		String nounsURL = "https://raw.githubusercontent.com/pizzaboy314/indie-band-name-generator/master/dictionary%20files/nounslist.txt";
		String adjsURL = "https://raw.githubusercontent.com/pizzaboy314/indie-band-name-generator/master/dictionary%20files/adjectiveslist.txt";
		
		String lastNamesURL = "https://raw.githubusercontent.com/pizzaboy314/indie-band-name-generator/master/dictionary%20files/lastnames.txt";
		String firstMNamesURL = "https://raw.githubusercontent.com/pizzaboy314/indie-band-name-generator/master/dictionary%20files/firstmalenames.txt";
		String firstFNamesURL = "https://raw.githubusercontent.com/pizzaboy314/indie-band-name-generator/master/dictionary%20files/firstfemalenames.txt";
		
		nounsList = parseWords(nounsURL);
		adjsList = parseWords(adjsURL);
		lastNamesList = parseWords(lastNamesURL);
		firstMNamesList = parseWords(firstMNamesURL);
		firstFNamesList = parseWords(firstFNamesURL);
		
	}
	
	public static List<String> parseWords(String url){
		List<String> list = new ArrayList<String>();
		
		
		try {
			URL source = null;
			boolean valid = true;
			try {
				source = new URL(url);
			} catch (MalformedURLException e) {
				valid = false;
			}
			while (valid == false) {
				valid = true;
				url = (String) JOptionPane.showInputDialog(null, "Malformed URL format. Are you sure you copied the entire URL?\n" + "Try again:",
						"Provide URL", JOptionPane.PLAIN_MESSAGE, null, null, null);
				try {
					source = new URL(url);
				} catch (MalformedURLException e) {
					valid = false;
				}
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(source.openStream()));

			String inputLine = in.readLine();
			while (inputLine != null) {
				String[] split = inputLine.trim().split("\\s|$");
				String s = split[0].toLowerCase();
				s = s.substring(0, 1).toUpperCase() + s.substring(1);

				list.add(s);
				inputLine = in.readLine();
			}

			
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
    public void actionPerformed(ActionEvent e) {
        // do things
    }
	
	public synchronized static void resultWindow() {
		resultFrame = new JFrame("Indie Band Name Generator");
		File resultPath = new File(System.getProperty("user.dir"));
		fc = new JFileChooser(resultPath);

		FileFilter filter = new FileNameExtensionFilter("Text file (*.txt)", "txt");
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension((int) (screenSize.width / 2), (int) (screenSize.height / 2));
		resultFrameBoundX = (int) (frameSize.width / 2);
		resultFrameBoundY = (int) (frameSize.height / 2);
		resultFrameBoundWidth = 450;
		resultFrameBoundHeight = frameSize.height;
		resultFrame.setBounds(resultFrameBoundX, resultFrameBoundY, resultFrameBoundWidth, resultFrameBoundHeight);

		resultText = new JTextArea();
		resultText.setText("");
		resultText.setEditable(false);

		JButton saveFile = new JButton("Save Results");
		saveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					String filepath = f.getAbsolutePath();
					String filename = f.getName();

					if (!filename.contains(".txt")) {
						resultFile = new File(filepath + ".txt");
					} else {
						resultFile = f;
					}

					try {
						Files.write(Paths.get(resultFile.getAbsolutePath()), resultString.getBytes());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		JButton gbutton = new JButton("Generate Band Name");
		gbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringBuilder sb = new StringBuilder();
				for(int i=0; i < numNames.intValue(); i++){
					sb.append(generateBandName(i) + "\n");
				}

				resultString = sb.toString();
				resultText.setText(resultString);
			}
		});
		
		JTextField numNamesField = new JTextField(10);
		numNamesField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateNum();
			}

			public void removeUpdate(DocumentEvent e) {
				updateNum();
			}

			public void insertUpdate(DocumentEvent e) {
				updateNum();
			}
			
			public void updateNum() {
				Runnable doTextUpdate = new Runnable() {
			        @Override
			        public void run() {
						String s = numNamesField.getText();

						try {
							numNames = Integer.parseInt(s);
						} catch (NumberFormatException nfe){
							numNames = numNamesInit;
							numNamesField.setText(numNames.toString());
						}
			        }
			    };       
			    SwingUtilities.invokeLater(doTextUpdate);
			}
		});
		numNamesField.setText(numNamesInit.toString());
		numNamesField.setMinimumSize(new Dimension(12, 12));
		
		JPanel generateControls = new JPanel();
		generateControls.setLayout(new FlowLayout());
		generateControls.add(gbutton);
		generateControls.add(new JLabel("  Number of Names: "));
		generateControls.add(numNamesField);
		
		JPanel saveControls = new JPanel();
		saveControls.setLayout(new FlowLayout());
		saveControls.add(saveFile);

		resultFrame.getContentPane().add(new JScrollPane(resultText), BorderLayout.CENTER);
		resultFrame.getContentPane().add(saveControls, BorderLayout.SOUTH);
		resultFrame.getContentPane().add(generateControls, BorderLayout.NORTH);
	}

}
