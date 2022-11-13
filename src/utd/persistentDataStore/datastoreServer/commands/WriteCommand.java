package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import utd.persistentDataStore.utils.*;

public class WriteCommand extends ServerCommand{

	@Override
	public void run() throws IOException, ServerException {
		String name = "";
		int size = 0;
		byte[] data;
		try {
			name = StreamUtil.readLine(super.inputStream);
			size = Integer.valueOf(StreamUtil.readLine(inputStream));
			data = StreamUtil.readData(size, super.inputStream);
		} catch(IOException e) {
			e.printStackTrace();
			throw new ServerException("Error reading input.");
		}
		
		try {
			FileUtil.writeData(name, data);
		} catch(IOException e) {
			e.printStackTrace();
			throw new ServerException("Error writing data.");
		}
		
		super.sendOK();
	}

}
