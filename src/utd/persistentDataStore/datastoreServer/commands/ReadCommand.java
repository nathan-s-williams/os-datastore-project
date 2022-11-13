package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.*;

public class ReadCommand extends ServerCommand{

	@Override
	public void run() throws IOException, ServerException {
		String name = "";
		byte[] data = null;
		try {
			name = StreamUtil.readLine(super.inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServerException("Error reading input.");
		}
		
		try {
			data = FileUtil.readData(name);
		} catch(Exception e) {
			e.printStackTrace();
			throw new ServerException("Cannot locate data.");
		}
		
		super.sendOK();
		StreamUtil.writeLine(String.valueOf(data.length) + "\n", outputStream);
		StreamUtil.writeData(data, outputStream);
		
		
	}

}
