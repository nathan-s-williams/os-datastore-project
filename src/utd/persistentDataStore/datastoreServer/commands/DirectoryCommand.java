package utd.persistentDataStore.datastoreServer.commands;

//Imports
import java.io.IOException;
import utd.persistentDataStore.utils.*;
import java.util.List;

//Class used to return the current directory
public class DirectoryCommand extends ServerCommand{
	
	//Declare directory list and msgSize
	List<String> dir;
	String msgSize = "";
	
	//Override run() in abstract class ServerCommand
	@Override
	public void run() throws IOException, ServerException {
		try {
			//Get current directory
			dir = FileUtil.directory();
			//Get size of directory
			msgSize = String.valueOf(dir.size()) + "\n";
		} catch(ServerException e) {
			e.printStackTrace();
			throw new ServerException("Unable to retrieve directory.");
		}
		
		try {
			//Send OK on success and write back size of directory and each data in directory
			super.sendOK();
			StreamUtil.writeLine(msgSize, super.outputStream);
			for(String str : dir) {
				StreamUtil.writeLine(str, super.outputStream);
			}
		} catch(IOException e) {
			e.printStackTrace();
			throw new ServerException("Error sending data to client.");
		}
	}

}
