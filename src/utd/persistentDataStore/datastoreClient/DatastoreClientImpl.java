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

//Imports
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

//Class to implement the client API
public class DatastoreClientImpl implements DatastoreClient
{
	//Declare InetAddress and port
	private InetAddress address;
	private int port;

	//Constructor
	//Instantiate address and port
	public DatastoreClientImpl(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#write(java.lang.String, byte[])
	 */
	//Override write function in DatastoreClient interface.
	@Override
    public void write(String name, byte data[]) throws ClientException, ConnectionException
	{
		try {
			//Initiate socket, SocketAddress, Input and Output streams
			//System.out.println("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			//Send write command with name, length and data.
			StreamUtil.writeLine("write\n", outputStream);
			StreamUtil.writeLine(name, outputStream);
			StreamUtil.writeLine(data.length + "\n", outputStream);
			StreamUtil.writeData(data, outputStream);
			
			//Read response from server. Return OK response if success; otherwise return error message.
			//Close streams and socket where appropriate.
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
				System.out.print(response +"\n");
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
	//Override read function in DatastoreClient interface.
	@Override
    public byte[] read(String name) throws ClientException, ConnectionException
	{
		try {
			//Initiate socket, SocketAddress, Input and Output streams
			//System.out.println("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			//Write read command and name of data to read
			//System.out.println("Writing Message");
			StreamUtil.writeLine("read\n", outputStream);
			StreamUtil.writeLine(name, outputStream);
			
			//Read response from server. Return OK response if success; otherwise return error message.
			//Close streams and socket where appropriate.
			String response = StreamUtil.readLine(inputStream);
			if (!response.contentEquals("OK")) 
			{
				socket.close();
				System.out.print(response +"\n");
				throw new ClientException(response);				
			}
			else 
			{
				//Use response parameters to read and return data
				int size = Integer.parseInt(StreamUtil.readLine(inputStream));
				
				byte[] data = new byte[size];
				
				data = StreamUtil.readData(size, inputStream);
				
				
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
	//Override delete function in DatastoreClient interface.
	@Override
    public void delete(String name) throws ClientException, ConnectionException
	{
		try {
			//Initiate socket, SocketAddress, Input and Output streams
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			//Send delete command and name of data to delete
			StreamUtil.writeLine("delete\n", outputStream);
			StreamUtil.writeLine(name, outputStream);
			
			//Read response from server. Return OK response if success; otherwise return error message.
			//Close streams and socket where appropriate.
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
				System.out.print(response +"\n");
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
	//Override directory function in DatastoreClient interface.
	@Override
    public List<String> directory() throws ClientException, ConnectionException
	{
		try {
			//Initiate socket, SocketAddress, Input and Output streams
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			//Send directory command
			StreamUtil.writeLine("directory\n", outputStream);
			
			//Read response from server. Return OK response if success; otherwise return error message.
			//Close streams and socket where appropriate.
			String response = StreamUtil.readLine(inputStream);
			if(response.contentEquals("OK"))
			{
				//Use response to load and return directory
				System.out.print(response + "\n");
				int size = Integer.parseInt(StreamUtil.readLine(inputStream));
				System.out.print(size + "\n");
				
				List<String> names = new ArrayList<String>();
				
				for(int runner = 0; runner < size; runner++)
				{
					names.add(StreamUtil.readLine(inputStream));
				}
				
				
				outputStream.close();
				inputStream.close();
				socket.close();
				
				return names;
			}
			else
			{
				System.out.print(response +"\n");
				socket.close();
				throw new IOException(response);
			}
				
			
			
			
		}
		catch (IOException ex) {
			throw new ClientException(ex.getMessage(), ex);
		}
	}

}
