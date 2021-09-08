import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.JTextPane;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class Main extends JFrame{
	private static String actualArchive = FileSystemView.getFileSystemView().getHomeDirectory().toString();
	DefaultListModel<Integer> model = new DefaultListModel<Integer>();
	private JList<Integer> lineNumber = new JList<Integer>();
	private static JTextPane lblConsole = new JTextPane();
	private JMenuItem menuNovo = new JMenuItem("Novo");
	private static JTextPane  codeArea = new JTextPane();
	private static StyledDocument doc = codeArea.getStyledDocument();
	private JMenuBar menuBar = new JMenuBar();
	private JPanel mainWindow;
	String archive = null;
	private int index = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainWindow = new JPanel();
		mainWindow.setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setBounds(100, 100, 700, 650);	
		setContentPane(mainWindow);
		mainWindow.setBorder(new EmptyBorder(5, 5, 5, 5));
		setResizable(false);
		setTitle("IDE");
		
		//menu
		menuBar.setBounds(12, 5, 65, 21);
		mainWindow.add(menuBar);
			
		JMenu menu = new JMenu("Menu");
		menuBar.add(menu);
				
		menu.add(menuNovo);
		BtnNew btnNovo = new BtnNew();
		menuNovo.addActionListener(btnNovo);
				
		JMenuItem mntmSalvar = new JMenuItem("Salvar");
		menu.add(mntmSalvar);
		BtnSave btnSalvar = new BtnSave();
		mntmSalvar.addActionListener(btnSalvar);
				
		JMenuItem mntmImportar = new JMenuItem("Importar");
		menu.add(mntmImportar);
		BtnImport btnImportar = new BtnImport();
		mntmImportar.addActionListener(btnImportar);
				
		JMenuItem mntmSair = new JMenuItem("Sair");
		menu.add(mntmSair);
		BtnExit btnSair = new BtnExit();
		mntmSair.addActionListener(btnSair);
				
		JButton btnCompilar = new JButton("Compilar");
		btnCompilar.setBounds(87, 5, 101, 21);
		mainWindow.add(btnCompilar);
		BtnCompile botaoCompilar = new BtnCompile();
		btnCompilar.addActionListener(botaoCompilar);
				
		JPanel pContainer = new JPanel();
		pContainer.setLayout(new BorderLayout(0, 0));
			
		//linhas do codigo
		lineNumber.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lineNumber.setBorder(new LineBorder(new Color(0, 0, 0)));
		lineNumber.setFixedCellHeight(20);
		lineNumber.setFont(new Font("Dialog", Font.PLAIN, 15));
		pContainer.add(lineNumber, BorderLayout.WEST);
		model.addElement(index+1);
		lineNumber.setModel(model);
				
		//area de escrever o codigo
		codeArea.setFont(new Font("Dialog", Font.PLAIN, 15));
		pContainer.add(codeArea, BorderLayout.CENTER);
		codeArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
		codeArea.addKeyListener((KeyListener) new KeyListener() {  
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			        index++;
			        model.addElement(index+1);
			       	lineNumber.setModel(model);
			    }
			}
					
			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
		});
				
		JScrollPane scrollPanel = new JScrollPane(pContainer);
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setBounds(12, 33, 660, 400);
		mainWindow.add(scrollPanel);
			    
		//console
		JScrollPane consolePanel = new JScrollPane();
		consolePanel.setBounds(12, 435, 660, 160);
		mainWindow.add(consolePanel);
		lblConsole.setText("Console");
		lblConsole.setToolTipText("");
		lblConsole.setForeground(new Color(255, 0, 0));
		lblConsole.setFont(new Font("Arial", Font.BOLD, 12));
		consolePanel.setViewportView(lblConsole);
	}
	
	private class BtnNew implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int r = j.showOpenDialog(null);
			 
			 if (r == JFileChooser.APPROVE_OPTION) {
				 if(!j.getSelectedFile().exists()) {
					 createFile(j.getSelectedFile().getAbsolutePath());
					 actualArchive = j.getSelectedFile().getAbsolutePath();
					 JOptionPane.showMessageDialog(mainWindow, "Arquivo criado com sucesso!");
					 showFile(j.getSelectedFile().getAbsolutePath());
				 }
			 } else {
				 //arquivo ja existe
				 JOptionPane.showMessageDialog(mainWindow, "Arquivo com esse nome ja existente");
			 }
			
		}
	}
	
	private class BtnSave implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			if (actualArchive != null) {
				j = new JFileChooser(actualArchive);
			}
            int r = j.showSaveDialog(null);
            if (r == JFileChooser.APPROVE_OPTION){ 
            	actualArchive = j.getSelectedFile().getAbsolutePath();
            	if(!j.getSelectedFile().exists()) {
            		createFile(actualArchive);
				} 
            	PrintWriter writer;
				try {
					writer = new PrintWriter(actualArchive);
					writer.println(codeArea.getText());
					writer.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
                //show success
            	JOptionPane.showMessageDialog(mainWindow, "Arquivo salvo com sucesso!");
            } else {
            	JOptionPane.showMessageDialog(null, "Algum erro ocorreu ao tentar salvar", "Erro", JOptionPane.ERROR_MESSAGE);
            }
		}
	}
	
	private class BtnImport implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			if (actualArchive != null) {
				j = new JFileChooser(actualArchive);
			}
			int r = j.showOpenDialog(null);
			 
			if (r == JFileChooser.APPROVE_OPTION) {
				actualArchive = j.getSelectedFile().getAbsolutePath();
				
				if(j.getSelectedFile().exists()) {
					showFile(actualArchive);
				} else {
					createFile(actualArchive);
					showFile(actualArchive);
				}   
	        } 
		}
	}
	
	private class BtnExit implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			System.exit(EXIT_ON_CLOSE);
		}
	}
	
	private class BtnCompile implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if(actualArchive != null) {
				try {
					PrintWriter writer;
					writer = new PrintWriter(actualArchive);
					writer.println(codeArea.getText());
					writer.close();
					SintaticMain.sintaticMain(actualArchive);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (LexicException e) {
					e.printStackTrace();
				} catch (SintaticException e) {
					e.printStackTrace();
				}
			} else {
				//mostrar erro caso contrario 
				JOptionPane.showMessageDialog(null, "Arquivo nao encontrado ou vazio", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void showFile(String archive) {
        System.out.println(archive);
        lineNumber.setModel(model);
        FileReader fileReader;
		try {
			fileReader = new FileReader(archive);
			BufferedReader reader = new BufferedReader(fileReader);
            String fileContent = "";
            int fileContentIndex = 0;
       		String auxContent = reader.readLine();
       		model.clear();
       		
       		while (auxContent != null) {
       			fileContent += auxContent;
       			fileContent += '\n';
       			auxContent = reader.readLine();
       			model.addElement(fileContentIndex+1);
       			lineNumber.setModel(model);
       			fileContentIndex++;
       		}
       		index = fileContentIndex;
       		fileReader.close();
       		codeArea.setText(fileContent);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	private void createFile(String name) {
		File file = new File(name);
	    try {
	    	if(file.createNewFile()){
	    		System.out.println("File Created");
			}else System.out.println("File already exists in the directory");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendToConsole(String str) {
		lblConsole.setText(lblConsole.getText() + "\n" + str);
	}

	public static void underlineError(int line, int colunm) {
		FileReader fileReader;
		BufferedReader reader;
		String fileContent = "";
		String auxContent;
		char controlCharacter;
		int offset = 0;
		int length = 1;
		
		try {
			fileReader = new FileReader(actualArchive);
			reader = new BufferedReader(fileReader);
			
			for(int i = 0; i < line-1; i++) {
				auxContent = reader.readLine();
				offset += auxContent.length();
				fileContent += auxContent;
			}
			
			fileContent += reader.readLine();
			offset += colunm;
			
			if (offset < fileContent.length()) {
				int fileContentIndex = offset;
				controlCharacter = fileContent.charAt(fileContentIndex);
				fileContentIndex++;
				
				while (controlCharacter != ' ' && controlCharacter != '\n') {
					if (fileContentIndex < fileContent.length()) {
						controlCharacter = fileContent.charAt(fileContentIndex);
						fileContentIndex++;
						length++;
					} else {
						break;
					}
				}
			}
			
			fileReader.close();

			SimpleAttributeSet keyWord = new SimpleAttributeSet();
			StyleConstants.setUnderline(keyWord, true);
			doc.setCharacterAttributes(offset, length, keyWord, false);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
