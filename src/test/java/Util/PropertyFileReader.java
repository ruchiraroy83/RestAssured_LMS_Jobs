package Util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.Reporter;

public class PropertyFileReader {

	public static Properties readPropertiesFile(String API) {
		FileInputStream fis = null;
		Properties prop = null;
		
		String FilePath;
		switch (API) {
		case "LMS":
			FilePath = "./src/test/resources/PropertyFiles/LMSconfig.properties";
			break;
		case "jobs":
        	FilePath="./src/test/resources/PropertyFiles/jobsConfig.properties";
			break;
		default:
			FilePath = null;
			Reporter.log(FilePath + ": File path not found");
			break;
		}

		try {

			fis = new FileInputStream(FilePath);
			prop = new Properties();
			prop.load(fis);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}
}
