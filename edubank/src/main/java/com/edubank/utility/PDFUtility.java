package com.edubank.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This class contains functionality to create PDF with a specified content.
 * 
 * @author ETA_JAVA
 *
 */
public class PDFUtility {
	static Logger logger = LogManager.getLogger(PDFUtility.class);
	
	/**
	 * This method creates a PDF file with the specified content
	 * 
	 * @param content
	 * @param fieldId
	 * It is the same as customerId
	 * 
	 * @return Created file reference
	 * @throws IOException 
	 */
	public static File createPDF(String content, String fileId) throws IOException {
		String fileName = "EduBank-Details-" + fileId + ".pdf";
		String filePath = getServerFilePath() + fileName;
		File file = null;
		FileOutputStream fos = null;
		try {
			file = new File(filePath);
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			Document document = new Document();
			PdfWriter.getInstance(document, fos);
			document.open();
			
			// Adding image
//			Image img = Image.getInstance(PDFUtility.class.getResource("/com/edubank/resources/EduBank_Logo.png").getPath());
//			document.add(img);
			Font font1 = FontFactory.getFont(FontFactory.COURIER, 36, BaseColor.ORANGE);
			Chunk chunk1 = new Chunk("EDUBank", font1);
			document.add(chunk1);
			document.add(new Phrase("\n--------------------------------------------------------------"
					+ "-----------------------------------------------------------------"));
			// Adding content
			Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
			Chunk chunk = new Chunk(content, font);
			 
			document.add(new Phrase("\n"));
			document.add(chunk);
			document.add(new Phrase("\n--------------------------------------------------------------"
					+ "-----------------------------------------------------------------"));
			document.close();
			fos.close();
		}
		catch(DocumentException | IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		finally{
			fos.close();
		}
		return file;
	}
	
	/**
	 * This method is used to delete a PDF file from the server after the first login of a customer
	 * 
	 * @param fieldId
	 * It is the same as customerId
	 * 
	 * @return true if file was found and deleted, else false
	 */
	public static boolean deletePDF(String fileId) {
		String fileName = "EduBank-Details-" + fileId + ".pdf";
		String filePath = getServerFilePath() + fileName;
		File file = new File(filePath);
		return file.delete();
	}
	
	/**
	 * This method helps in getting the server path to the 'files' directory.
	 * 
	 * @return Server path to the 'file' directory
	 */
	public static String getServerFilePath() {
		return "C:\\Java\\";
	}
}
