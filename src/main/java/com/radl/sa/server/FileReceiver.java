package com.radl.sa.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReceiver {

	private DataInputStream dis;

	public FileReceiver(DataInputStream dis) {
		this.dis = dis;
	}

	public File receive() throws IOException {

		// receive filename
		String filename = dis.readUTF();

		// construct file
		Path tmpFolder = Files.createTempDirectory("tmp");
		System.out.println(tmpFolder.toString());
		Path tmpFile = tmpFolder.resolve(filename);
		File f = tmpFile.toFile();
		
		try (FileOutputStream fos = new FileOutputStream(f)) {
			// receive filesize
			Long length = dis.readLong();
			Long bytes_remaining = length;

			// buffer
			byte[] buffer = new byte[4096];

			// receive in packets
			int bytes_read;
			while (true) {
				// read bytes into buffer and record how many
				// check if remaining bytes are less than buffer size
				bytes_read = dis.read(buffer, 0, (int) Math.min(buffer.length, bytes_remaining));
				// check if no bytes were read (eof)
				if (bytes_read == 0) {
					break;
				}
				// write buffer content into file stream
				fos.write(buffer);
				// now fewer bytes remain to be read
				bytes_remaining -= bytes_read;
			}
		}
		
		// return file only if nothing went wrong,
		// otherwise throw IOException
		return f;
	}

}
