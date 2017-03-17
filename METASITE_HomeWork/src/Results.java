import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Results {

	private JFrame frame;
	private static Results window;
	public static Launcher1 l;
	public static ReadThread1 r;
	public static WriteThread1 w;

	private DefaultListModel<String> model;
	private DefaultListModel<String> model2;
	private JList<String> listRead;
	private JList<String> listWrite;

	private File folder = new File(
			System.getProperty("user.dir") + File.separator + "src" + File.separator + "ReadTXT");
	private File[] listOfFiles = folder.listFiles();

	private File folder2 = new File(
			System.getProperty("user.dir") + File.separator + "src" + File.separator + "WriteTXT");
	private File[] listOfFiles2 = folder2.listFiles(new FilenameFilter() {
		@Override
		public boolean accept(File folder2, String filename) {
			return filename.toLowerCase().contains("words");
		}
	});

	private File[] uploadedFiles; // existing .txt files + new uploaded files
	private File file;

	private int counter = 0;
	public int wordsCount = 0;

	private JPanel contentPane;
	private JFileChooser fileChooser;
	private JButton btnAddFile;
	private JButton btnStart;
	private JButton btnOpenRead;
	private JButton btnRemove;
	private JButton btnRead;
	private JButton btnOpenWrite;
	private JButton btnRestart;
	private JButton btnExit;
	private JRadioButton rdbtnReadExistingFiles;
	private JRadioButton rdbtnUploadNewFiles;
	private JLabel lblWritingFiles;
	private JLabel lblCounter;
	private JTextField textPath;
	private Desktop desktop = Desktop.getDesktop();
	private JScrollPane scrollPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Results();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Results() {
		initialize();
	}

	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 994, 588);
		frame.setTitle("Reading & Writing .txt files exercise");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

		// Radio buttons:
		rdbtnReadExistingFiles = new JRadioButton("Read existing files");
		rdbtnReadExistingFiles.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		rdbtnReadExistingFiles.setBounds(264, 19, 177, 23);
		contentPane.add(rdbtnReadExistingFiles);

		rdbtnUploadNewFiles = new JRadioButton("Upload new files and read");
		rdbtnUploadNewFiles.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		rdbtnUploadNewFiles.setBounds(435, 19, 226, 23);
		contentPane.add(rdbtnUploadNewFiles);

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnReadExistingFiles);
		group.add(rdbtnUploadNewFiles);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(264, 70, 692, 165);
		contentPane.add(scrollPane);

		// JLists (2)
		listRead = new JList<>(model = new DefaultListModel<String>());
		scrollPane.setViewportView(listRead);
		listRead.setBorder(new LineBorder(Color.GRAY));
		listRead.setBackground(Color.WHITE);

		listWrite = new JList<>(model2 = new DefaultListModel<String>());
		listWrite.setBorder(new LineBorder(Color.LIGHT_GRAY));
		listWrite.setBackground(Color.WHITE);
		listWrite.setBounds(264, 385, 692, 122);
		contentPane.add(listWrite);

		btnStart = new JButton("Start");
		btnStart.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnStart.setEnabled(true);
		btnStart.setBounds(673, 20, 117, 23);
		contentPane.add(btnStart);

		// All buttons:
		btnOpenRead = new JButton("  Open file");
		btnOpenRead.setHorizontalAlignment(SwingConstants.LEFT);
		btnOpenRead.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnOpenRead.setIcon(new ImageIcon(Results.class.getResource("/Icons/Button-Blank-Green-icon.png")));
		btnOpenRead.setBounds(16, 103, 236, 23);
		contentPane.add(btnOpenRead);
		btnOpenRead.setBorderPainted(false);
		btnOpenRead.setOpaque(true);
		btnOpenRead.setEnabled(false);

		btnRemove = new JButton("  Remove file");
		btnRemove.setHorizontalAlignment(SwingConstants.LEFT);
		btnRemove.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnRemove.setIcon(new ImageIcon(Results.class.getResource("/Icons/Button-Blank-Green-icon.png")));
		btnRemove.setBounds(16, 138, 236, 23);
		contentPane.add(btnRemove);
		btnRemove.setBorderPainted(false);
		btnRemove.setOpaque(true);
		btnRemove.setEnabled(false);

		btnRead = new JButton("  Read Files");
		btnRead.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnRead.setHorizontalAlignment(SwingConstants.LEFT);
		btnRead.setIcon(new ImageIcon(Results.class.getResource("/Icons/Button-Blank-Green-icon.png")));
		btnRead.setEnabled(false);
		btnRead.setBounds(16, 173, 236, 23);
		contentPane.add(btnRead);
		btnRead.setBorderPainted(false);
		btnRead.setOpaque(true);

		btnAddFile = new JButton("Add file");
		btnAddFile.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnAddFile.setHorizontalAlignment(SwingConstants.LEFT);
		btnAddFile.setBounds(16, 259, 236, 23);
		contentPane.add(btnAddFile);
		btnAddFile.setEnabled(false);

		btnOpenWrite = new JButton("  Open file");
		btnOpenWrite.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnOpenWrite.setIcon(new ImageIcon(Results.class.getResource("/Icons/Button-Blank-Green-icon.png")));
		btnOpenWrite.setHorizontalAlignment(SwingConstants.LEFT);
		btnOpenWrite.setBounds(16, 385, 236, 23);
		btnOpenWrite.setBorderPainted(false);
		btnOpenWrite.setOpaque(true);
		btnOpenWrite.setEnabled(false);
		contentPane.add(btnOpenWrite);

		btnRestart = new JButton("  Read again (reastart the program)");
		btnRestart.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnRestart.setIcon(new ImageIcon(Results.class.getResource("/Icons/Button-Blank-Green-icon.png")));
		btnRestart.setHorizontalAlignment(SwingConstants.LEFT);
		btnRestart.setBounds(16, 419, 236, 23);
		contentPane.add(btnRestart);
		btnRestart.setEnabled(false);
		btnRestart.setBorderPainted(false);
		btnRestart.setOpaque(true);

		btnExit = new JButton("  Exit program");
		btnExit.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnExit.setHorizontalAlignment(SwingConstants.LEFT);
		btnExit.setIcon(new ImageIcon(Results.class.getResource("/Icons/Button-Blank-Red-icon.png")));
		btnExit.setBounds(16, 454, 236, 23);
		contentPane.add(btnExit);
		btnExit.setBorderPainted(false);
		btnExit.setOpaque(true);

		// File chooser:
		fileChooser = new JFileChooser();
		fileChooser.setVisible(true);
		disableNewFolderButton(fileChooser);
		fileChooser.setCurrentDirectory(new File("user.home"));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("TEXT Documents", "txt"));
		fileChooser.setAcceptAllFileFilterUsed(true);

		lblCounter = new JLabel("");
		lblCounter.setForeground(Color.GRAY);
		lblCounter.setFont(new Font("Lucida Grande", Font.ITALIC, 12));
		lblCounter.setBounds(264, 236, 89, 27);
		contentPane.add(lblCounter);

		lblWritingFiles = new JLabel("");
		lblWritingFiles.setBorder(new TitledBorder(
				new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Results MENIU",
						TitledBorder.LEADING, TitledBorder.TOP, null, new Color(128, 128, 128)),
				"Results MENIU", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		lblWritingFiles.setBounds(6, 309, 972, 239);
		lblWritingFiles.setForeground(Color.GRAY);
		lblWritingFiles.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		contentPane.add(lblWritingFiles);

		// JText
		textPath = new JTextField();
		textPath.setText("...");
		textPath.setBounds(264, 259, 692, 26);
		contentPane.add(textPath);
		textPath.setColumns(10);

		JTextPane textPaneSummary = new JTextPane();
		textPaneSummary.setForeground(Color.GRAY);
		textPaneSummary.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		textPaneSummary.setBackground(UIManager.getColor("CheckBox.background"));
		textPaneSummary.setEditable(false);
		textPaneSummary.setBounds(265, 334, 691, 45);
		contentPane.add(textPaneSummary);

		// ---------------------ButtonActions----------------------------

		// START button action
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnReadExistingFiles.isSelected()) {
					btnStart.setEnabled(false);
					rdbtnReadExistingFiles.setEnabled(false);
					rdbtnUploadNewFiles.setEnabled(false);
					btnOpenRead.setEnabled(true);
					btnRemove.setEnabled(true);
					btnRead.setEnabled(true);

					for (int i = 0; i < listOfFiles.length; i++) {
						model.addElement(listOfFiles[i].toString());
						counter++;
						lblCounter.setText(Integer.toString(counter) + " files to read");
					}
					textPath.setEnabled(false);
				}
				if (rdbtnUploadNewFiles.isSelected()) {
					btnStart.setEnabled(false);
					rdbtnReadExistingFiles.setEnabled(false);
					rdbtnUploadNewFiles.setEnabled(false);
					btnAddFile.setEnabled(true);
					btnOpenRead.setEnabled(true);
					btnRemove.setEnabled(true);
					btnRead.setEnabled(true);

				}
			}
		});

		// OPEN (readable file) button action
		btnOpenRead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (model.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "File list is empty!\nPlease select a file!");
				} else {
					openFile(listRead, model);
				}
			}
		});

		// REMOVE file button (from readable files list) action
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (model.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "File list is empty!\nPlease select a file!");
				} else {
					removeFile(listRead, model);
					counter--;
					lblCounter.setText(Integer.toString(counter) + " files selected");
				}
			}
		});

		// ADD file (from FileChooser) button action
		btnAddFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				showOpenFileDialog();

				uploadedFiles = new File[model.size()];
				for (int i = 0; i < model.size(); i++) {
					uploadedFiles[i] = new File(model.getElementAt(i).substring(0));
				}
			}
		});

		// READ button action
		btnRead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (rdbtnReadExistingFiles.isSelected()) {
					if (model.isEmpty()) {
						// jei model tuscias, programa neleidzia skaityti
						JOptionPane.showMessageDialog(frame, "File list is empty! \nPlease select a file!");
					} else {

						// model elementai verciami i String
						String[] inputFile = new String[model.size()];
						for (int i = 0; i < inputFile.length; i++) {
							inputFile[i] = model.getElementAt(i).toString();
						}

						// siunciamas failas skaitymui
						readAndRun(inputFile);

						// mygtuku aktyvavimas
						btnRead.setEnabled(false);
						btnRemove.setEnabled(false);
						btnOpenWrite.setEnabled(true);
						btnRestart.setEnabled(true);
					}
				}
				if (rdbtnUploadNewFiles.isSelected()) {
					if (model.isEmpty()) {
						// jei model tuscias, programa neleidzia skaityti
						JOptionPane.showMessageDialog(frame, "File list is empty! \nPlease select a file!");
					} else {
						// model elementai verciami i String
						String[] inputFile = new String[model.size()];
						for (int i = 0; i < inputFile.length; i++) {
							inputFile[i] = model.getElementAt(i).toString();
						}
						// siunciamas filas skaitymui
						readAndRun(inputFile);

						// mygtuku aktyvavimas
						btnAddFile.setEnabled(false);
						btnRead.setEnabled(false);
						btnRemove.setEnabled(false);
						btnOpenWrite.setEnabled(true);
						btnRestart.setEnabled(true);
					}
				}

				if (!model.isEmpty()) {

					wordsCount = l.getwordsCount();
					textPaneSummary
							.setText("Number of Scanned documents: " + counter + "\nNumber of words: " + wordsCount);

					// model2 - rodomi rezultatai
					for (int i = 0; i < listOfFiles2.length; i++) {
						String format = "%-20s %-15s %n";
						model2.addElement(
								String.format(format, listOfFiles2[i].getName(), listOfFiles2[i].getAbsolutePath()));
					}
				}
			}
		});

		// OPEN written files action
		btnOpenWrite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(listWrite, listOfFiles2);
			}
		});

		// RESTART button action + (delete content)
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String message = "Please notice that after restart all content of the Written files will be deleted\nDo You want to continue?";
				String title = "Message";
				int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					try {
						deleteContent(listOfFiles2);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					frame.dispose();
					main(new String[0]);
				}
			}
		});

		// EXIT button action
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});

	}
	// ---------------METHODS-----------------------------------------------------

	public void openFile(JList<String> list, DefaultListModel<String> modelOpen) {
		File[] mf = new File[modelOpen.size()];
		for (int i = 0; i < modelOpen.size(); i++) {
			mf[i] = new File(modelOpen.getElementAt(i).substring(0));
		}

		int index = list.getSelectedIndex();

		for (int i = 0; i < modelOpen.size(); i++) {
			if (index >= 0) {
				if (index == i)
					try {
						desktop.open(mf[i]);
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}

	public void openFile(JList<String> list, File[] file) {
		int index = list.getSelectedIndex();

		for (int i = 0; i < file.length; i++) {
			if (index >= 0) {
				if (index == i)
					try {
						desktop.open(file[i]);
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}

	public void removeFile(JList<String> list, DefaultListModel<String> model) {

		int index = list.getSelectedIndex();
		if (index >= 0) { // Remove only if a particular item is
							// selected
			model.removeElementAt(index);
		}
	}

	private void showOpenFileDialog() {

		int result = fileChooser.showOpenDialog(frame);

		if (result == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			if (model.contains(file.getAbsolutePath())) {
				JOptionPane.showMessageDialog(frame, file.getName() + " already exists !\nChoose another file");
			} else {
				counter++; // skaiciuoja, kiek is viso ikelta failu
				lblCounter.setText(Integer.toString(counter) + " files are selected");
				model.addElement(file.getAbsolutePath());
				textPath.setText(file.getAbsolutePath());
				JOptionPane.showMessageDialog(frame, file.getName());
			}

		} else {
			JOptionPane.showMessageDialog(frame, "No file chosen");
		}
	}

	private static void disableNewFolderButton(Container c) {
		int len = c.getComponentCount();
		for (int i = 0; i < len; i++) {
			Component comp = c.getComponent(i);
			if (comp instanceof JButton) {
				JButton b = (JButton) comp;
				Icon icon = b.getIcon();
				if (icon != null && icon == UIManager.getIcon("FileChooser.newFolderIcon"))
					b.setEnabled(false);
			} else if (comp instanceof Container) {
				disableNewFolderButton((Container) comp);
			}
		}
	}

	private static void readAndRun(String[] inputFile) {
		l = new Launcher1(inputFile);
		r = new ReadThread1(l);
		w = new WriteThread1(l);

		r.start();
		w.start();

		try {
			r.join();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		try {
			w.join();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	private void deleteContent(File[] files) throws IOException {
		BufferedWriter writer1;

		for (int i = 0; i < files.length; i++) {
			writer1 = new BufferedWriter(new FileWriter(files[i]));
			try {
				writer1.write("");
				writer1.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (writer1 != null)
						writer1.close();

				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
