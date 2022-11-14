package utd.persistentDataStore.datastoreServer.commands;

//Imports
import java.io.IOException;

import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.*;

//Class used to read back data in database
public class ReadCommand extends ServerCommand{

	//Override run() in abstract class ServerCommand
	@Override
	public void run() throws IOException, ServerException {
		//Declare name and byte array
		String name = "";
		byte[] data = null;
		
		try {
			//Get name of data from next line in request
			name = StreamUtil.readLine(super.inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServerException("Error reading input.");
		}
		
		try {
			//Read data matching the name of request
			data = FileUtil.readData(name);
		} catch(Exception e) {
			e.printStackTrace();
			throw new ServerException("Cannot locate data.");
		}
		
		try {
			//Send OK on success. Send size of data and the byte array of data in database
			super.sendOK();
			StreamUtil.writeLine(String.valueOf(data.length) + "\n", outputStream);
			StreamUtil.writeData(data, outputStream);
		} catch(IOException e) {
			e.printStackTrace();
			throw new ServerException("Error sending data to client.");
		}
		
	}

}
