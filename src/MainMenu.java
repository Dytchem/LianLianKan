
/**
 * ���˵�
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.*;

/**
 * ���˵�
 */
public class MainMenu extends Frame {
	private static final long serialVersionUID = 1L;

	MainMenu() {
		super("���˵�");
		setSize(800, 600);
		setResizable(false);
		{ // λ�þ���
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screenSize = toolkit.getScreenSize();
			int x = (screenSize.width - getWidth()) / 2;
			int y = (screenSize.height - getHeight()) / 2;
			setLocation(x, y);
		}
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		setItem();
		setVisible(true);
	}

	void setItem() {
		setLayout(null);
		setHead();
		setButton();
		setOthers();
	}

	void setHead() {
		Label l = new Label("�� �� ��", Label.CENTER);
		l.setFont(new Font("����", Font.BOLD, 50));
		l.setSize(getWidth(), getHeight() / 10);
		l.setLocation((getWidth() - l.getWidth()) / 2, (int) (getHeight() * 0.15));
		add(l);
	}

	void setButton() {
		Button b1 = new Button("�½���Ϸ");
		b1.setFont(new Font("����", Font.PLAIN, 25));
		b1.setSize(getWidth() / 3, getHeight() / 10);
		b1.setLocation((getWidth() - b1.getWidth()) / 2, (int) (getHeight() * 0.35));
		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new CreateGame();
			}
		});
		add(b1);

		Button b2 = new Button("���ش浵");
		b2.setFont(new Font("����", Font.PLAIN, 25));
		b2.setSize(getWidth() / 3, getHeight() / 10);
		b2.setLocation((getWidth() - b2.getWidth()) / 2, (int) (getHeight() * 0.50));
		b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadLocal();
			}
		});
		add(b2);

		Button b3 = new Button("����浵");
		b3.setFont(new Font("����", Font.PLAIN, 25));
		b3.setSize(getWidth() / 3, getHeight() / 10);
		b3.setLocation((getWidth() - b3.getWidth()) / 2, (int) (getHeight() * 0.65));
		add(b3);
	}

	void setOthers() {
		Label l = new Label("LianLianKan 2024 v1.0.0", Label.CENTER);
		l.setFont(new Font("����", Font.ITALIC, 20));
		l.setSize(getWidth(), getHeight() / 10);
		l.setLocation((getWidth() - l.getWidth()) / 2, (int) (getHeight() * 0.85));
		add(l);

		MenuBar m = new MenuBar();
		Menu m1 = new Menu("����");
		setMenuBar(m);
		m.add(m1);
	}

	void loadLocal() {
		System.out.println("load...");
		FileDialog f = new FileDialog(this, "��ȡ�浵", FileDialog.LOAD);
		f.setFilenameFilter(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".llk") || new File(dir, name).isDirectory();
			}
		});
		f.setVisible(true);
		String path = f.getFile();
		if (path != null) {
			path = f.getDirectory() + path;
			System.out.println(path);
			try {
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(path));
				new Game((DataStorage) input.readObject());
				input.close();
			} catch (Exception e) {
				System.out.println("����ʧ�ܣ�");
			}

		}
	}
}
