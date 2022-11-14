package utd.persistentDataStore.datastoreServer.commands;

//Imports
import java.io.IOException;
import utd.persistentDataStore.utils.*;

//Class used to write data to the database
public class WriteCommand extends ServerCommand{

	//Override run() in abstract class ServerCommand
	@Override
	public void run() throws IOException, ServerException {
		//Declare name and size
		String name = "";
		int size = 0;
		byte[] data;
		try {
			//Read name, size and byte array of data
			name = StreamUtil.readLine(super.inputStream);
			size = Integer.valueOf(StreamUtil.readLine(inputStream));
			data = StreamUtil.readData(size, super.inputStream);
		} catch(IOException e) {
			e.printStackTrace();
			throw new ServerException("Error reading input.");
		}
		
		try {
			//Write data to the database
			FileUtil.writeData(name, data);
		} catch(IOException e) {
			e.printStackTrace();
			throw new ServerException("Error writing data.");
		}
		try {
			//Send OK on success
			super.sendOK();
		} catch(IOException e) {
			e.printStackTrace();
			throw new ServerException("Error sending data to client.");
		}
		
	}

}
