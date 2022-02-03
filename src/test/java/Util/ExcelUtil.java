package Util;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;

import java.io.IOException;

public class ExcelUtil {

    /**
     * This class is a utility class and is used to Read Excel sheet
     */

    String path;
    FileInputStream fis;
    Workbook workbook;
    Sheet sheet;
    Row row;


    public void readSheet(String path,String sheetName) throws Exception {

        File file = new File(path);
        fis = new FileInputStream(file);
        //Below API can ready both xls and xlsx formats
        workbook = WorkbookFactory.create(fis);
        sheet = workbook.getSheet(sheetName);

    }

    public Map<String,String>  getDataFromExcel(String columnName) throws IOException {
        Map<String,String> testData = new HashMap<>();
        int dataColNum=0;
        for (int j=0; j<sheet.getRow(0).getPhysicalNumberOfCells();j++ ) {
            String Key= sheet.getRow(0).getCell(j).getStringCellValue().trim();
            if (sheet.getRow(0).getCell(j).getStringCellValue().equals(columnName)) {
                dataColNum = j;
                for (int i=0; i<sheet.getPhysicalNumberOfRows();i++ ) {
                    String Value = sheet.getRow(i).getCell(dataColNum).getStringCellValue().trim();
                    testData.put(Key,Value);
                }
                break;
            }

        }

        fis.close();
        return testData;
    }


    public boolean writeExcelFile(String excelFilePath, String sheetName, int rowNumber, String columnName,
                                  Object cellValue) {
        boolean status;
        try {
            String path = "src/test/resources/" + excelFilePath;
            File fileObj = new File(path);
            String dirPath = fileObj.getAbsolutePath();

            FileInputStream inputStream = new FileInputStream(new File(dirPath));
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rowNumber);
            int columnIndex = getColumnNames(sheet, columnName);

            Cell cell = row.getCell(columnIndex);
            if (cell == null) {
                cell = row.createCell(columnIndex, CellType.STRING);
            }

            if(cellValue instanceof Integer) {
                cell.setCellValue((int) cellValue);
            }else if(cellValue instanceof String) {
                cell.setCellValue((String) cellValue);
            }

            inputStream.close();

            FileOutputStream outputStream = new FileOutputStream(dirPath);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            status = Boolean.TRUE;
        } catch (Exception ex) {
            ex.printStackTrace();
            status = Boolean.FALSE;
        }
        return status;
    }

    public int getColumnNames(Sheet sheet, String colName) {
        try {
            Iterator<Row> rows = sheet.iterator();
            Row firstrow = rows.next();
            Iterator<Cell> ce = firstrow.cellIterator();
            while (ce.hasNext()) {
                Cell value = ce.next();

                if (value.getCellType() == CellType.STRING && colName.equalsIgnoreCase(value.getStringCellValue())) {
                    return value.getColumnIndex();
                }

            }
            return 0;
        } catch (Exception e) {
            System.out.println(e.getMessage() + e);
            return 0;
        }

    }

}
