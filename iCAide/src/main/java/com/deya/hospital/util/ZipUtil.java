package com.deya.hospital.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.zip.GZIPOutputStream;

public class ZipUtil {

	public static void compress(String filePath, String outPath){
		BufferedReader br;
		try {
			File file = new File(outPath);
			if(file.exists()){
				file.delete();
			}
			
			br = new BufferedReader(new FileReader(filePath));
			File f = new File(outPath);
			BufferedOutputStream bOs = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(f)));
			int c;
			while ((c=br.read())!=-1) {
				bOs.write(c);
			}
			br.close();
			bOs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
