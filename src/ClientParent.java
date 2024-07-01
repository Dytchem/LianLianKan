
/**
 * 客户端和服务端的基类
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 
 */
public class ClientParent extends Frame {
	private static final long serialVersionUID = 1L;
	List L;

	ClientParent() {
		super();
		setSize(800, 600);
		setResizable(false);
		{ // 位置居中
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screenSize = toolkit.getScreenSize();
			int x = (screenSize.width - getWidth()) / 2;
			int y = (screenSize.height - getHeight()) / 2;
			setLocation(x, y);
		}
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		setLayout(null);

		L = new List();
		L.setFont(new Font("宋体", Font.PLAIN, 20));
		L.setSize(getWidth() / 4 * 3, getHeight() / 10 * 7);
		L.setLocation((int) ((getWidth() - L.getWidth()) * 0.5), (int) (getHeight() * 0.185));
		L.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile(L.getSelectedIndex());
			}
		});
		add(L);

		Button b1 = new Button("打开");
		b1.setFont(new Font("宋体", Font.PLAIN, 20));
		b1.setSize(getWidth() / 5, getHeight() / 15);
		b1.setLocation((int) ((getWidth() - L.getWidth()) * 0.5), (int) (getHeight() * 0.1));
		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile(L.getSelectedIndex());
			}
		});
		add(b1);

		Button b2 = new Button("上传");
		b2.setFont(new Font("宋体", Font.PLAIN, 20));
		b2.setSize(getWidth() / 5, getHeight() / 15);
		b2.setLocation((int) ((getWidth() - L.getWidth()) * 1.6), (int) (getHeight() * 0.1));
		b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				uploadFile();
			}
		});
		add(b2);

		Button b3 = new Button("删除");
		b3.setFont(new Font("宋体", Font.PLAIN, 20));
		b3.setSize(getWidth() / 5, getHeight() / 15);
		b3.setLocation((int) ((getWidth() - L.getWidth()) * 2.7), (int) (getHeight() * 0.1));
		b3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteFile(L.getSelectedIndex());
			}
		});
		add(b3);

		setVisible(true);
	}

	synchronized void uploadFile() {
	}

	synchronized void addFile(String s) {
	}

	synchronized void openFile(int index) {
	}

	synchronized void deleteFile(int index) {
	}
	
	synchronized void sync() {
	}
}
