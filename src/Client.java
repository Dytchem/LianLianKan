
/**
 * �ͻ���
 */

import java.awt.Button;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 
 */
public class Client extends ClientParent {
	private static final long serialVersionUID = 1L;
	Socket s;

	Client(String host, int port) {
		super();
		setTitle("�ͻ���" + " host=" + host + " port=" + port);
		try {
			s = new Socket(host, port);
			sync();

		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}

		Button b4 = new Button("ͬ��");
		b4.setFont(new Font("����", Font.PLAIN, 20));
		b4.setSize(getWidth() / 5, getHeight() / 15);
		b4.setLocation((int) ((getWidth() - L.getWidth()) * 2.7), (int) (getHeight() * 0.9));
		b4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DataOutputStream output0;
				try {
					output0 = new DataOutputStream(s.getOutputStream());
					output0.writeInt(-1);
					output0.flush();
				} catch (IOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}

				sync();
			}
		});
		add(b4);

		System.out.println("Client OK!");
	}

	@Override
	void sync() {
		try {
			L.removeAll();
			ObjectInputStream input = new ObjectInputStream(s.getInputStream());
			String send = (String) input.readObject();
			String[] ss = send.split("%");
			for (String s : ss) {
				if (s == "")
					continue;
				addFile(s);
			}

		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}

	@Override
	void uploadFile() {
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
				DataStorage d = (DataStorage) input.readObject();
				// L.add("���" + d.W + " �߶�" + d.H + " ��ɫ��" + d.N + " ʱ��" + d.date);

				DataOutputStream output0 = new DataOutputStream(s.getOutputStream());
				output0.writeInt(1);
				output0.flush();

				ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
				output.writeObject(d);
				output.flush();
				System.out.println("�ͻ������ϴ���");
				input.close();
			} catch (Exception e) {
				System.out.println("����ʧ�ܣ�");
			}

		}
		sync();
	}

	@Override
	void addFile(String s) {
		if (s == "")
			return;
		L.add(s);
	}

	@Override
	void openFile(int index) {
		if (index == -1)
			return;
		try {
			DataOutputStream output0 = new DataOutputStream(s.getOutputStream());
			output0.writeInt(0);
			output0.flush();

			OutputStream output = s.getOutputStream();
			output.write(index);

			ObjectInputStream input = new ObjectInputStream(s.getInputStream());
			new Game((DataStorage) input.readObject());
			System.err.println("openFile");
		} catch (IOException | ClassNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}

	@Override
	void deleteFile(int index) {
		if (index == -1)
			return;
		try {
			DataOutputStream output0 = new DataOutputStream(s.getOutputStream());
			output0.writeInt(2);
			output0.flush();

			OutputStream output = s.getOutputStream();
			output.write(index);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		sync();
	}
}
