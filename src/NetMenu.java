
/**
 * 网络编程主菜单
 */

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 
 */
public class NetMenu extends Frame {
	private static final long serialVersionUID = 1L;

	NetMenu() {
		super("请选择类型");
		setSize(400, 300);
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

		Button b1 = new Button("客户端");
		b1.setFont(new Font("宋体", Font.PLAIN, 25));
		b1.setSize(getWidth() / 3, getHeight() / 5);
		b1.setLocation((getWidth() - b1.getWidth()) / 2, (int) (getHeight() * 0.25));
		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Client("127.0.0.1", 12345);
			}
		});
		add(b1);

		Button b2 = new Button("服务端");
		b2.setFont(new Font("宋体", Font.PLAIN, 25));
		b2.setSize(getWidth() / 3, getHeight() / 5);
		b2.setLocation((getWidth() - b2.getWidth()) / 2, (int) (getHeight() * 0.55));
		b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Server(12345, ".");
			}
		});
		add(b2);

		setVisible(true);
	}
}
