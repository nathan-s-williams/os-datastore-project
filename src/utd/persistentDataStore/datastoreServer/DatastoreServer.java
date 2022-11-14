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
 
package utd.persistentDataStore.datastoreServer;

//Imports
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import utd.persistentDataStore.datastoreServer.commands.*;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

//Server class
//Opens a local port to 10023 and listens for incoming requests from client API
public class DatastoreServer
{
	static public final int port = 10023;

	public void startup() throws IOException
	{
		System.out.println("Starting Service at port " + port);
		//Initiate socket
		ServerSocket serverSocket = new ServerSocket(port);
		
		//Declare input and output streams
		InputStream inputStream = null;
		OutputStream outputStream = null;
		//Infinite loop used to listen for incoming requests.
		while (true) {
			try {
				System.out.println("Waiting for request");
				// The following accept() will block until a client connection 
				// request is received at the configured port number
				Socket clientSocket = serverSocket.accept();
				System.out.println("Request received");
				
				//Initiate input and output streams
				inputStream = clientSocket.getInputStream();
				outputStream = clientSocket.getOutputStream();
				
				//Initiate server command with incoming request from client API and run command
				ServerCommand command = dispatchCommand(inputStream);
				System.out.println("Processing Request: " + command);
				command.setInputStream(inputStream);
				command.setOutputStream(outputStream);
				command.run();
				
				//Close socket
				StreamUtil.closeSocket(inputStream);
			}
			catch (ServerException ex) {
				String msg = ex.getMessage();
				System.out.println("Exception while processing request. " + msg);
				StreamUtil.sendError(msg, outputStream);
				StreamUtil.closeSocket(inputStream);
			}
			catch (Exception ex) {
				System.out.println("Exception while processing request. " + ex.getMessage());
				ex.printStackTrace();
				StreamUtil.closeSocket(inputStream);
			}
		}
	}

	private ServerCommand dispatchCommand(InputStream inputStream) throws ServerException
	{
		//Declare command and serverCommand
		String command = null;
		ServerCommand serverCommand = null;
		
		//Read in command from first line of request.
		try {
			command = StreamUtil.readLine(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServerException("Unable to process command.");
		}
		
		//Instantiate serverCommand with matching request; otherwise throw error: Command not found
		if("write".equalsIgnoreCase(command)) {
			serverCommand = new WriteCommand();
		} 
		else if("read".equalsIgnoreCase(command)) {
			serverCommand = new ReadCommand();
		}
		else if("delete".equalsIgnoreCase(command)) {
			serverCommand = new DeleteCommand();
		}
		else if("directory".equalsIgnoreCase(command)) {
			serverCommand = new DirectoryCommand();
		}
		else {
			throw new ServerException("Command not found.");
		}
		
		return serverCommand;
	}

	public static void main(String args[])
	{
		//Instantiate and run server.
		DatastoreServer server = new DatastoreServer();
		try {
			server.startup();
		}
		catch (IOException ex) {
			System.out.println("Unable to start server. " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
