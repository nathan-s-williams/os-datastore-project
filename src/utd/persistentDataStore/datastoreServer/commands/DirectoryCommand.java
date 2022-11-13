package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import utd.persistentDataStore.utils.*;
import java.util.List;

public class DirectoryCommand extends ServerCommand{

	@Override
	public void run() throws IOException, ServerException {
		List<String> dir = FileUtil.directory();
		super.sendOK();
		String msgSize = String.valueOf(dir.size()) + "\n";
		StreamUtil.writeLine(msgSize, super.outputStream);
		for(String str : dir) {
			StreamUtil.writeLine(str, super.outputStream);
		}
	}

}
