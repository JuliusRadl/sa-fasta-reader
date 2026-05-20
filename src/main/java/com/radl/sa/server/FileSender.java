package com.radl.sa.server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileSender {

	private File f;
	private DataOutputStream dos;

	public FileSender(File f, DataOutputStream dos) {
		this.f = f;
		this.dos = dos;
	}

	public void send() throws IOException {

		try (FileInputStream fis = new FileInputStream(f)) {
			// send filename
			String filename = f.getName();
			dos.writeUTF(filename);

			// send file size in bytes
			long length = f.length();
			dos.writeLong(length);

			// send in packets
			byte[] buffer = new byte[4096];
			int bytes_read;
			while(true) {
				bytes_read = fis.read(buffer);
				if (bytes_read == -1) {
					break;
				}
				dos.write(buffer, 0, bytes_read);
			}
		}
	}
}
