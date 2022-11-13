/* NOTICE: All materials provided by this project, and materials derived 
 * from the project, are the property of the University of Texas. 
 * Project materials, or those derived from the materials, cannot be placed 
 * into publicly accessible locations on the web. Project materials cannot 
 * be shared with other project teams. Making project materials publicly 
 * accessible, or sharing with other project teams will result in the 
 * failure of the team responsible and any team that uses the shared materials. 
 * Sharing project materials or using shared materials will also result 
 * in the reporting of all team members for academic dishonesty. 
 */ 
 
package utd.persistentDataStore.datastoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import utd.persistentDataStore.utils.StreamUtil;

public class DatastoreClientImpl implements DatastoreClient
{
	private InetAddress address;
	private int port;

	public DatastoreClientImpl(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#write(java.lang.String, byte[])
	 */
	@Override
    public void write(String name, byte data[]) throws ClientException, ConnectionException
	{
		try {
			//System.out.println("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			
			StreamUtil.writeLine("write\n", outputStream);
			StreamUtil.writeLine(name, outputStream);
			StreamUtil.writeLine(data.length + "\n", outputStream);
			StreamUtil.writeData(data, outputStream);
			
			String response = StreamUtil.readLine(inputStream);
			if(response.contentEquals("OK"))
			{
				System.out.println(response);
				outputStream.close();
				inputStream.close();
				socket.close();
			}
			else
			{
				socket.close();
				throw new IOException(response);
			}
			
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#read(java.lang.String)
	 */
	@Override
    public byte[] read(String name) throws ClientException, ConnectionException
	{
		try {
			//System.out.println("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			//System.out.println("Writing Message");
			StreamUtil.writeLine("read\n", outputStream);
			StreamUtil.writeLine(name, outputStream);
			
			String response = StreamUtil.readLine(inputStream);
			if (!"ok".equalsIgnoreCase(response.trim())) 
			{
				socket.close();
				
				throw new ClientException(response);				
			}
			else 
			{
				int size = Integer.parseInt(StreamUtil.readLine(inputStream));
				
				byte[] data = new byte[size];
				
				data = StreamUtil.readData(size, inputStream);
				
				System.out.print(response + "\n");
				System.out.print(size + "\n");

				for(int runner = 0; runner < size; runner++)
				{
					System.out.print(data[runner] + " ");
				}
				System.out.print("\n");
				
				outputStream.close();
				inputStream.close();
				socket.close();
				
				return data;
				
			}
			
			
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#delete(java.lang.String)
	 */
	@Override
    public void delete(String name) throws ClientException, ConnectionException
	{
		try {
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			
			StreamUtil.writeLine("delete\n", outputStream);
			StreamUtil.writeLine(name, outputStream);
			
			String response = StreamUtil.readLine(inputStream);
			if(response.contentEquals("OK"))
			{
				System.out.println(response);
				outputStream.close();
				inputStream.close();
				socket.close();
			}
			else
			{
				socket.close();
				throw new IOException(response);
			}
				
			
			
			
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
	}
	
	

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#directory()
	 */
	@Override
    public List<String> directory() throws ClientException, ConnectionException
	{
		try {
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			
			StreamUtil.writeLine("directory\n", outputStream);
			
			
			String response = StreamUtil.readLine(inputStream);
			if(response.contentEquals("OK"))
			{
				System.out.print(response + "\n");
				int size = Integer.parseInt(StreamUtil.readLine(inputStream));
				System.out.print(size + "\n");
				
				List<String> names = new ArrayList<String>();
				
				for(int runner = 0; runner < size; runner++)
				{
					names.add(StreamUtil.readLine(inputStream));
					System.out.print(names.get(runner) + "\n");
				}
				
				
				outputStream.close();
				inputStream.close();
				socket.close();
				
				return names;
			}
			else
			{
				socket.close();
				throw new IOException(response);
			}
				
			
			
			
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
	}

}
