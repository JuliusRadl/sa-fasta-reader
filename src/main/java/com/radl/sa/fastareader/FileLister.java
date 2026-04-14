package com.radl.sa.fastareader;
import java.io.File;

public class FileLister {

	public static void listRec(File directory, int depth) {
		String[] list = directory.list();
		
		for (String fileName : list) {
			
			for (int i = 0; i < depth; i++) {
				System.out.print("\t");
			}
			
			System.out.println(fileName);
			File child = new File(directory, fileName);
			
			if (child.isDirectory()) {
				listRec(child, depth + 1);
			}
		}
	}
}
