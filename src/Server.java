
/**
 * 服务端
 */

import java.awt.FileDialog;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 */
public class Server extends ClientParent {
	private static final long serialVersionUID = 1L;
	String filepath;
	ArrayList<String> locs = new ArrayList<>();
	MyServerSocket ss;
	ArrayList<MySocket> sockets = new ArrayList<>();

	Server(int port, String filepath) {
		super();
		setTitle("服务端" + " port=" + port + " filepath=" + filepath);
		ss = new MyServerSocket(port, this);
		ss.start();
		this.filepath = filepath;
		try {
			Stream<Path> l = Files.list(Paths.get(filepath));
			String fileList = l.map(Path::toString).collect(Collectors.joining("\n"));
			// System.out.println(fileList);
			for (String path : fileList.split("\n")) {
				if (path.endsWith(".llk"))
					addFile(path);
			}
			l.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		System.out.println("Server OK!");
	}

	@Override
	void sync() {
		for (MySocket s : sockets) {
			s.download();
		}
	}

	@Override
	void uploadFile() {
		System.out.println("load...");
		FileDialog f = new FileDialog(this, "读取存档", FileDialog.LOAD);
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
				System.out.println(d.date);
				input.close();
				addFile(path);
			} catch (Exception e) {
				System.out.println("导入失败！");
			}

		}
	}

	@Override
	void addFile(String path) {
		if (path != null) {
			System.out.println(path);
			try {
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(path));
				DataStorage d = (DataStorage) input.readObject();
				System.out.println(d.date);
				input.close();
				L.add("宽度" + d.W + " 高度" + d.H + " 颜色数" + d.N + " 时间" + d.date);
				locs.add(path);
			} catch (Exception e) {
				System.out.println("导入失败！");
			}

		}
	}

	@Override
	void openFile(int index) {
		if (index == -1)
			return;
		String path = locs.get(index);
		if (path != null) {
			System.out.println(path);
			try {
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(path));
				new Game((DataStorage) input.readObject());
				input.close();
			} catch (Exception e) {
				System.out.println("导入失败！");
			}

		}
	}

	@Override
	void deleteFile(int index) {
		if (index == -1)
			return;
		locs.remove(index);
		L.remove(index);
	}

	void addSocket(Socket s) {
		MySocket m = new MySocket(s, this);
		m.download();
		m.start();
		sockets.add(m);
	}

	DataStorage getFile(int index) {
		if (index == -1)
			return null;
		String path = locs.get(index);
		if (path != null) {
			System.out.println(path);
			try {
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(path));
				DataStorage re = (DataStorage) input.readObject();
				input.close();
				return re;
			} catch (Exception e) {
				System.out.println("导入失败！");
			}

		}
		return null;
	}

	@Override
	public void dispose() {
		ss.interrupt();
		for (MySocket s : sockets)
			s.interrupt();
		super.dispose();
	}
}

class MyServerSocket extends Thread {
	ServerSocket ss;
	Server pa;

	MyServerSocket(int port, Server pa) {
		this.pa = pa;
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket s = ss.accept();
				pa.addSocket(s);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
}

class MySocket extends Thread {
	Socket s;
	Server pa;

	MySocket(Socket s, Server pa) {
		this.s = s;
		this.pa = pa;
	}

	@Override
	public void run() {
		try {
			while (true) {
				DataInputStream input0 = new DataInputStream(s.getInputStream());
				int op = input0.readInt();
				System.err.println("op = " + op);
				if (op == -1) {
					download();
				} else if (op == 0) {// 客户端请求打开文件
					InputStream input = s.getInputStream();
					int index = input.read();
					ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
					DataStorage d = pa.getFile(index);
					output.writeObject(d);
				} else if (op == 1) { // 客户端上传文件
					ObjectInputStream input = new ObjectInputStream(s.getInputStream());
					DataStorage d = (DataStorage) input.readObject();
					System.out.println("upload...");
					String newpath = pa.filepath + "\\" + d.date.replace(':', ' ') + ".llk";
					System.out.println("newpath: " + newpath);
					ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(newpath));
					output.writeObject(d);
					pa.addFile(newpath);
					output.close();
					download();
				} else if (op == 2) { // 客户端请求删除文件
					InputStream input = s.getInputStream();
					int index = input.read();
					pa.deleteFile(index);
					download();
				}
			}
		} catch (IOException | ClassNotFoundException e) {

		}
	}

	void download() {
		try {
			ObjectOutputStream output = new ObjectOutputStream(s.getOutputStream());
			String send = "";
			for (String s : pa.L.getItems()) {
				send = send + "%" + s;
			}
			output.writeObject(send);
			output.flush();
		} catch (Exception e) {

		}
	}
}