package com.samaylabs.optimus.Dao.ExcelInput;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * 
 * @author Shabab
 *
 */
public class ExcelCon {

	Workbook wb = null;
	
	/**
	 * @param path of the xl sheet
	 * @return Workbook instance to extract data from excel sheet
	 */
	public Workbook getExcel(String path){

		String FilePath = path;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(FilePath);
			wb = Workbook.getWorkbook(fs);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return wb;
	}
}




