package t.socket4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class Server {
	public static void main(String args[]) {
		ServerSocket server = null;
		List<Socket> list = new Vector<>();	//� Ÿ���� ��Ƽ� ������ ���ΰ�
		//List<EchoThread> list2 = new Vector<>();	//Thread�� ��Ƽ� ������ ����
						
		try {
			server = new ServerSocket(9001);
			System.out.println("Ŭ���̾�Ʈ�� ������ �����");

			while (true) {
				Socket socket = server.accept();
				list.add(socket);	//Socket ������ list�� add
				new EchoThread(socket, list).start();
			}

		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();

		} finally {

		}

	}
}

class EchoThread extends Thread {
	List<Socket> list;
	Socket socket;

	public EchoThread() {	}

	public EchoThread(Socket socket, List<Socket> list) {
		this.socket = socket;
		this.list = list;
	}

	@Override
	public void run() {
		InetAddress address = socket.getInetAddress();
		System.out.println(address.getHostAddress() + " �κ��� �����߽��ϴ�.");
		try {
			InputStream in = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String message = null;
			while ((message = br.readLine()) != null) {
				System.out.println(message);
				broadcast(message);
			}
			br.close();
			

		} catch (Exception e) {
			System.out.println(e.getMessage());

		} finally {
		}
		try {
			list.remove(socket);
			System.out.println(socket + "��������");
			System.out.println(list);
			if(socket!=null) socket.close();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//run �޼ҵ�ʹ� ������ broadcast ���� �޼ҵ� ���� (EchoThread�� ������ �޼ҵ�)
	public synchronized void broadcast(String msg) throws IOException{	
		for(Socket socket : list){
			OutputStream out = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
			pw.println(msg);
			pw.flush();	
		}
	}
}
