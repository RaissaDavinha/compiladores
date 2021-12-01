//Imports do java
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

@SuppressWarnings("serial") //Tira os warnings de quando usa serial

public class Main extends JFrame{
	//Variaveis da Main
	private static String actualArchive = FileSystemView.getFileSystemView().getHomeDirectory().toString();
	DefaultListModel<Integer> model = new DefaultListModel<Integer>();
	private JList<Integer> lineNumber = new JList<Integer>();
	private static JTextPane lblConsole = new JTextPane();
	private static JTextPane  codeArea = new JTextPane();
	private static StyledDocument doc = codeArea.getStyledDocument();
	private JMenuBar menuBar = new JMenuBar();
	private JPanel mainWindow;
	String archive = null;
	private int index = 0;

	/**
	 * Procedimento inicial. Inicia o programa quando chamado pelo usuario.
	 * Chama o procedimento Main().
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main(); //Cria uma janela de acordo com os parametros em Main()
					frame.setVisible(true);	 //Deixa a janela visivel.
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Cria a aplicação. 
	 * É chamado pelo procedimento main(). Chama procedimento initialize().
	 */
	public Main() {
		initialize();
	}

	/**
	 * Inicia os componentes da interface.
	 * Procedimento chamado pelo procedimento Main().
	 * Chama as funcoes dos botoes da interface: BtnNew(), BtnSave(), BtnImport(), 
	 * 		BtnExit(), BtnCompilar()
	 */
	private void initialize() {
		//Definicao da janela principal.
		mainWindow = new JPanel();
		mainWindow.setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setBounds(100, 100, 700, 650);	
		setContentPane(mainWindow);
		mainWindow.setBorder(new EmptyBorder(5, 5, 5, 5));
		setResizable(false);
		setTitle("IDE");
		
		//Definicao da barra superior de menu
		menuBar.setBounds(12, 5, 65, 21);
		mainWindow.add(menuBar);
		
		//Cria o botao Menu no canto esquerdo superior
		JMenu menu = new JMenu("Menu");
		menuBar.add(menu);
		
		//Adiciona o botao "Novo" nas opcoes do Menu
		JMenuItem menuNovo = new JMenuItem("Novo");
		menu.add(menuNovo);
		BtnNew btnNovo = new BtnNew(); //Chama a classe do funcionamento do botao "Novo"
		menuNovo.addActionListener(btnNovo);
		
		//Adiciona o botao "Salvar" nas opções do Menu
		JMenuItem mntmSalvar = new JMenuItem("Salvar");
		menu.add(mntmSalvar);
		BtnSave btnSalvar = new BtnSave();	 //Chama a classe do funcionamento do botao "Salvar"
		mntmSalvar.addActionListener(btnSalvar);
		
		//Adiciona o botao "Importar" nas opcoes do Menu
		JMenuItem mntmImportar = new JMenuItem("Importar");
		menu.add(mntmImportar);
		BtnImport btnImportar = new BtnImport();	//Chama a classe do funcionamento do botao "Importar"
		mntmImportar.addActionListener(btnImportar);
		
		//Adiciona o botao "Sair" nas opcoes do Menu
		JMenuItem mntmSair = new JMenuItem("Sair");
		menu.add(mntmSair);
		BtnExit btnSair = new BtnExit();	 //Chama a classe do funcionamento do botao "Sair"
		mntmSair.addActionListener(btnSair);
		
		//Adiciona o botao "Compilar" na barra superior
		JButton btnCompilar = new JButton("Compilar");
		btnCompilar.setBounds(87, 5, 101, 21);
		mainWindow.add(btnCompilar);
		BtnCompile botaoCompilar = new BtnCompile();	//Chama a classe do funcionamento do botao "Compilar"
		btnCompilar.addActionListener(botaoCompilar);
				
		JPanel pContainer = new JPanel();
		pContainer.setLayout(new BorderLayout(0, 0));
			
		//Numero das linhas do codigo a ser compilado
		lineNumber.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lineNumber.setBorder(new LineBorder(new Color(0, 0, 0)));
		lineNumber.setFixedCellHeight(20);
		lineNumber.setFont(new Font("Dialog", Font.PLAIN, 15));
		pContainer.add(lineNumber, BorderLayout.WEST);
		model.addElement(index+1);
		lineNumber.setModel(model);
				
		//Area de escrever o codigo
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
		
		//Barra de rolagem do lado direito da area de escreve o codigo
		JScrollPane scrollPanel = new JScrollPane(pContainer);
		scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPanel.setBounds(12, 33, 660, 400);
		mainWindow.add(scrollPanel);
			    
		//Area da console, parte de baixo da interface
		JScrollPane consolePanel = new JScrollPane();
		consolePanel.setBounds(12, 435, 660, 160);
		mainWindow.add(consolePanel);
		lblConsole.setText("Console");
		lblConsole.setToolTipText("");
		lblConsole.setForeground(new Color(255, 0, 0));
		lblConsole.setFont(new Font("Arial", Font.BOLD, 12));
		consolePanel.setViewportView(lblConsole);
	}
	/**
	 * Classe que implementa o funcionamento da opcao "Novo" no menu.
	 * Abre um File Explorer na Home do usuario, para que o usuario escolha uma pasta e digite um nome novo de arquivo que
	 *  sera criado na pasta
	 * Chamada pela função initialize(). Usa funcao showFile().
	 */
	private class BtnNew implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int r = j.showOpenDialog(null);
			 
			 if (r == JFileChooser.APPROVE_OPTION) {
				 if(!j.getSelectedFile().exists()) {	//Se o nome digitado nao existir na pasta
					 createFile(j.getSelectedFile().getAbsolutePath());
					 actualArchive = j.getSelectedFile().getAbsolutePath();
					 JOptionPane.showMessageDialog(mainWindow, "Arquivo criado com sucesso!");
					 showFile(j.getSelectedFile().getAbsolutePath());
				 }
			 } else {
				 //Se o arquivo ja existe
				 JOptionPane.showMessageDialog(mainWindow, "Arquivo com esse nome ja existente");
			 }
			
		}
	}
	/**
	 * Classe que implementa o funcionamento da opcao "Salvar" no menu.
	 * Abre um File Explorer, para que o usuario escolha um arquivo aonde salvar o codigo escrito no compilador.
	 * Chamada pela função initialize().
	 */
	private class BtnSave implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			if (actualArchive != null) {
				j = new JFileChooser(actualArchive);
			}
            int r = j.showSaveDialog(null);
            //Se nao tiver erro, salvar codigo no arquivo
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
                //Mensagem de sucesso
            	JOptionPane.showMessageDialog(mainWindow, "Arquivo salvo com sucesso!");
            } else {
            	//Mensagem de erro ao tentar salvar um arquivo
            	JOptionPane.showMessageDialog(null, "Algum erro ocorreu ao tentar salvar", "Erro", JOptionPane.ERROR_MESSAGE);
            }
		}
	}
	
	/**
	 * Classe que implementa o funcionamento da opcao "Importar" no menu.
	 * Abre um File Explorer, para que o usuario escolha um arquivo para importar seu conteudo para o compilador.
	 * Chamada pela função initialize(). Usa funcao showFile().
	 */
	private class BtnImport implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			if (actualArchive != null) {
				j = new JFileChooser(actualArchive);
			}
			int r = j.showOpenDialog(null);
			 
			if (r == JFileChooser.APPROVE_OPTION) {
				actualArchive = j.getSelectedFile().getAbsolutePath();
				
				//Se o arquivo escolhido existe, mostrar conteudo do arquivo
				if(j.getSelectedFile().exists()) {
					showFile(actualArchive);
				} else {
					//Se não, criar um arquivo novo e mostrar seu conteudo.
					createFile(actualArchive);
					showFile(actualArchive);
				}   
	        } 
		}
	}
	
	/**
	 * Classe que implementa o funcionamento da opcao "Sair" no menu.
	 * Fecha o programa. Chamada pela função initialize().
	 */
	private class BtnExit implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			System.exit(EXIT_ON_CLOSE);
		}
	}
	
	/**
	 * Classe que implementa o funcionamento do botao "Compilar"
	 * Compila o programa digitado ou importado. Precisa estar salvo em algum arquivo.
	 * Chamada pela função initialize(). Chama a função sintaticMain() no arquivo SintaticMain.java
	 */
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
				} catch (SemanticException e) {
					e.printStackTrace();
				}
			} else {
				//Mostrar erro caso contrario 
				JOptionPane.showMessageDialog(null, "Arquivo nao encontrado ou vazio", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 * Procedimento que mostra o conteudo de um arquivo escolhido, ou criado, na area de codigo da inteface.
	 * Procedimento chamado pelo ActionListener do BtnNew e BtnImport
	 * Parametros:
	 * String archive = string com do caminho absoluto do arquivo
	 */
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
	/**
	 * Procedimento para criar um novo arquivo
	 * Procedimento chamado pelo ActionListener do BtnNew, BtnSave e BtnImport
	 * Parametros:
	 * String name = nome do arquivo que sera criado
	 * 		Se nao especificar no nome o tipo do arquivo (por exemplo .txt) sera criado um arquivo do tipo "File"
	 */
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
	
	/**
	 * Procedimento que manda texto para a console. 
	 * Usado normalmente para as mensagens de erro, ou para a mensagem de sucesso.
	 * Usado em:
	 * 	LexicException.java, SemanticException.java, SintaticException.java,
	 * 	SintaticMain.java
	 * Parametros:
	 * String str = string que será impressa na console do compilador
	 */
	public static void sendToConsole(String str) {
		lblConsole.setText(lblConsole.getText() + "\n" + str);
	}
	
	/**
	 * Procedimento que coloca em underline a linha do codigo onde tem erro de compilacao
	 * Usado em:
	 * 	LexicException.java, SemanticException.java, SintaticException.java,
	 * Parametros:
	 * int str = linha onde ocorreu o erro
	 * int colunm = coluna onde ocorreu o erro
	 */
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
