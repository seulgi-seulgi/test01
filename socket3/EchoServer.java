package t.socket3;

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

public class EchoServer {
	public static void main(String args[]) {
		ServerSocket server = null;
		try {
			server = new ServerSocket(9001);
			System.out.println("클라이언트의 접속을 대기중");

			while (true) {
				Socket socket = server.accept();
				new EchoThread(socket).start();
			}

		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();

		} finally {

		}

	}
}

class EchoThread extends Thread {

	Socket socket;

	public EchoThread() {
	}

	public EchoThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		InetAddress address = socket.getInetAddress();
		System.out.println(address.getHostAddress() + " 로부터 접속했습니다.");
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

			String message = null;
			while ((message = br.readLine()) != null) {
				System.out.println(message);
				pw.println(message);
				pw.flush();
			}
			br.close();
			pw.close();
			socket.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());

		} finally {
		}

	}
}
