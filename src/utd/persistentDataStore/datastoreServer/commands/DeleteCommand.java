package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import utd.persistentDataStore.utils.*;

public class DeleteCommand extends ServerCommand{

	@Override
	public void run() throws IOException, ServerException {
		String name = "";
		try {
			name = StreamUtil.readLine(super.inputStream);
		} catch(IOException e) {
			e.printStackTrace();
			throw new ServerException("Error reading input.");
		}
		
		try {
			FileUtil.deleteData(name);
		} catch(ServerException e) {
			e.printStackTrace();
			throw e;
		}
		
		super.sendOK();
		
	}

}
