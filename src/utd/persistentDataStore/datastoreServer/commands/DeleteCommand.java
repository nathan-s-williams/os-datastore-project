package utd.persistentDataStore.datastoreServer.commands;

//Imports
import java.io.IOException;
import utd.persistentDataStore.utils.*;

//Class used to handle delete command
public class DeleteCommand extends ServerCommand{

	//Override run() in abstract class ServerCommand
	@Override
	public void run() throws IOException, ServerException {
		
		//Declare name
		String name = "";
		try {
			//Read in name of data to be deleted
			name = StreamUtil.readLine(super.inputStream);
		} catch(IOException e) {
			e.printStackTrace();
			throw new ServerException("Error reading input.");
		}
		
		try {
			//Delete data
			FileUtil.deleteData(name);
		} catch(ServerException e) {
			e.printStackTrace();
			throw e;
		}
		
		try {
			//Send back OK to client API on success
			super.sendOK();
		} catch(IOException e) {
			e.printStackTrace();
			throw new ServerException("Error sending data to client.");
		}
		
	}

}
