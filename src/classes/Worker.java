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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Worker {

	private static JFrame resultFrame;
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
	
	public static void main(String[] args) {
		resultWindow();
		
		System.out.println();
		System.out.println();
		
//		if (false) { //something bad with user input happens
//			System.exit(0);
//		}
		
		populateDictionaries();
		populateGrammars();
		String bandname = generateBandName();

		resultString = bandname;
		resultText.setText(resultString);
		resultFrame.setVisible(true);
		resultFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		System.out.println();

	}
	
	public static String generateBandName(){
		long seed1 = System.currentTimeMillis();
		long seed2 = System.currentTimeMillis()+123456;
		
		String grammarString = grammars.get(randInt(0,grammars.size()-1,seed1));
		
		String nounString1 = nounsList.get(randInt(0,nounsList.size()-1,seed1));
		String adjString1 = adjsList.get(randInt(0,adjsList.size()-1,seed1));
		String nounString2 = nounsList.get(randInt(0,nounsList.size()-1,seed2));
		String adjString2 = adjsList.get(randInt(0,adjsList.size()-1,seed2));
		
		
		String lastNameString = lastNamesList.get(randInt(0,lastNamesList.size()-1,seed1));
		String firstMNameString = firstMNamesList.get(randInt(0,firstMNamesList.size()-1,seed1));
		String firstFNameString = firstFNamesList.get(randInt(0,firstFNamesList.size()-1,seed1));
		
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
	public static int randInt(int min, int max, long seed) {

	    // Usually this can be a field rather than a method variable
	    Random rand = new Random(seed);

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
	
	public synchronized static void resultWindow() {
		resultFrame = new JFrame("Results");
		File resultPath = new File(System.getProperty("user.dir"));
		fc = new JFileChooser(resultPath);

		FileFilter filter = new FileNameExtensionFilter("Text file (*.txt)", "txt");
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension((int) (screenSize.width / 2), (int) (screenSize.height / 2));
		int x = (int) (frameSize.width / 2);
		int y = (int) (frameSize.height / 2);
		resultFrame.setBounds(x, y, 600, frameSize.height);

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
		JPanel controls = new JPanel();
		controls.setLayout(new FlowLayout());
		controls.add(saveFile);

		resultFrame.getContentPane().add(new JScrollPane(resultText), BorderLayout.CENTER);
		resultFrame.getContentPane().add(controls, BorderLayout.SOUTH);
	}

}
