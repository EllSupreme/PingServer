package fr.ellsupreme.pingserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * This class allows to retrieve information from a server.
 * 
 * @author EllSupreme_
 *
 */
public class PingServer {
	
	private String host = null;
	private int port = 0;
	private Socket socket = new Socket();
	private String[] data = new String['§'];
	private boolean isOnline;

	/**
	 * Store all server info.
	 * 
	 * @param host
	 * @param port
	 */
	public PingServer(String host, int port)
	  {
	    this.host = host;
	    this.port = port;
	    try
	    {
	      try
	      {
	        this.socket.connect(new InetSocketAddress(host, port));
	      }
	      catch (ConnectException ex)
	      {
	        this.isOnline = false;
	        return;
	      }
	      this.isOnline = true;
	      OutputStream out = this.socket.getOutputStream();
	      InputStream in = this.socket.getInputStream();
	      out.write(254);
	      
	      StringBuffer str = new StringBuffer();
	      int b;
	      while ((b = in.read()) != -1)
	      {
	        if ((b != 0) && (b > 16) && (b != 255) && (b != 23) && (b != 24)) {
	          str.append((char)b);
	        }
	      }
	      this.data = str.toString().split("§");
	      this.data[0] = this.data[0].substring(1, this.data[0].length());
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	  }

	/**
	 * Recover the motd.
	 * 
	 * @return
	 */
	public String getMotd() {
		return this.data[0];
	}

	/**
	 * Retrieve the number of players connect.
	 * 
	 * @return
	 */
	public Integer getOnline() {
		try {
			return Integer.valueOf(Integer.parseInt(this.data[1]));
		} catch (Exception e) { return 0; }
	}

	/**
	 * Retrieve the maximum number of players.
	 * 
	 * @return
	 */
	public Integer getMax() {
		return Integer.valueOf(Integer.parseInt(this.data[2]));
	}

	/**
	 * Update server information
	 */
	public void update() {
		try {
			this.socket.close();
			this.socket = new Socket();
			try {
				this.socket.connect(new InetSocketAddress(this.host, this.port));
			} catch (ConnectException ex) {
				this.isOnline = false;
				return;
			}
			this.isOnline = true;

			OutputStream out = this.socket.getOutputStream();
			InputStream in = this.socket.getInputStream();
			out.write(254);

			StringBuffer str = new StringBuffer();
			int b;
			while ((b = in.read()) != -1) {
				if ((b != 0) && (b > 16) && (b != 255) && (b != 23) && (b != 24)) {
					str.append((char) b);
				}
			}
			this.data = str.toString().split("§");
			this.data[0] = this.data[0].substring(1, this.data[0].length());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check if the server is online.
	 * 
	 * @return
	 */
	public boolean isOnline() {
		return this.isOnline;
	}

	/**
	 * Check if the server is reboot.
	 * 
	 * @return
	 */
	public boolean isReboot() {
		return getMotd() == null;
	}

}
