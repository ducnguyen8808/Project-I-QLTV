
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class FileExcel {
    private CellStyle cellStyleFormatNumber = null;

    public Workbook getWorkBook(String path) {
        Workbook workbook = null;
        if (path.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (path.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("File not creat!");
        }
        return workbook;
    }

    public CellStyle creatStyle(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.WHITE.getIndex());

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }

    public void autoResizeColumn(Sheet sheet, int lastColumn) {
        for (int index = 0; index < lastColumn; index++)
            sheet.autoSizeColumn(index);
    }

    public void creatFile(Workbook workbook, String path) {
        try (OutputStream outputStream = new FileOutputStream(path)) {
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void exportExcel(JTable table, String path) throws FileNotFoundException {

        Workbook workbook = getWorkBook(path);

        Sheet sheet = workbook.createSheet();

        int numOfColumn = table.getColumnCount();
        int rowIndex = 0;
        while (table.getValueAt(rowIndex, 0) != null) {
            Row row = sheet.createRow(rowIndex);
            for (int i = 0; i < numOfColumn; i++) {

                Cell cell = row.createCell(i);
                String temp = String.valueOf(table.getValueAt(0, i));
                cell.setCellValue(temp);
            }
            rowIndex++;
        }
        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        autoResizeColumn(sheet, numberOfColumn);
        creatFile(workbook, path);
    }
}
