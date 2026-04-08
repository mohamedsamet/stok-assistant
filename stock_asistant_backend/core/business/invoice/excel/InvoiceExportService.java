package com.yesmind.stok.core.business.invoice.excel;

import com.yesmind.stok.application.exception.InvoiceGenerationException;
import com.yesmind.stok.application.exception.TemplateNotFoundException;
import com.yesmind.stok.core.domain.data.InvoiceDto;
import com.yesmind.stok.core.domain.data.ProductInvoiceDto;
import com.yesmind.stok.core.port.in.invoice.IInvoiceExportService;
import com.yesmind.stok.core.port.in.invoice.IInvoiceFactory;
import com.yesmind.stok.core.port.in.invoice.IInvoiceSearch;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceExportService implements IInvoiceExportService {

    @Value("${excel.invoice.file-path}")
    private String excelFilePath;

    @Value("${excel.bl.file-path}")
    private String excelBlFilePath;

    private final ResourceLoader resourceLoader;

    private final IInvoiceSearch invoiceSearch;
    private final IInvoiceFactory invoiceFactory;

    @Override
    @Transactional
    public byte[] exportBl(UUID publicId) throws TemplateNotFoundException {
        Resource resource = resourceLoader.getResource(excelBlFilePath);

        InvoiceDto invoiceDto = invoiceSearch.findByUuid(publicId);
        String reference = invoiceFactory.incrementBlReference(invoiceDto.getPublicId());

        if (!resource.exists()) {
            throw new TemplateNotFoundException(excelBlFilePath);
        }

        return generate(resource, invoiceDto, reference, true);

    }

    @Override
    @Transactional
    public byte[] export(UUID publicId) throws TemplateNotFoundException {
        Resource resource = resourceLoader.getResource(excelFilePath);

        InvoiceDto invoiceDto = invoiceSearch.findByUuid(publicId);
        String reference = invoiceFactory.incrementInvoiceReference(invoiceDto.getPublicId());

        if (!resource.exists()) {
            throw new TemplateNotFoundException(excelFilePath);
        }

        return generate(resource, invoiceDto, reference, false);
    }

    private static byte[] generate(Resource resource, InvoiceDto invoiceDto, String reference, boolean isBl) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        symbols.setDecimalSeparator('.');

        String pattern = "#,##0.000";

        DecimalFormat df = new DecimalFormat(pattern, symbols);

        try (InputStream is = resource.getInputStream();

             Workbook workbook = WorkbookFactory.create(is);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            List<ProductInvoiceDto> products = invoiceDto.getProductInvoices();
            int productsPerSheet = 13;
            int totalSheets = (int) Math.ceil((double) products.size() / productsPerSheet);

            for (int i = 1; i < totalSheets; i++) {
                workbook.cloneSheet(0);
            }

            for (int sheetIndex = 0; sheetIndex < totalSheets; sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                workbook.setSheetName(sheetIndex, "Page " + (sheetIndex + 1));
                fillCommonData(sheet, invoiceDto, df, reference, isBl);

                int start = sheetIndex * productsPerSheet;
                int end = Math.min(start + productsPerSheet, products.size());
                List<ProductInvoiceDto> subList = products.subList(start, end);
                fillProductRows(sheet, subList, df, isBl);
                Cell cellPage = getCell(sheet, isBl ? 35 : 36, isBl ? 6:7);
                cellPage.setCellValue("Page " + Integer.sum(1, sheetIndex) + "/" + totalSheets);
            }

            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception exception) {
            throw new InvoiceGenerationException(exception);
        }
    }

    private static void fillProductRows(Sheet sheet, List<ProductInvoiceDto> sublist, DecimalFormat df, boolean isBl) {
        short rowIndex = 13;

        for (ProductInvoiceDto product: sublist) {

            Cell cellPRef = getCell(sheet, rowIndex, 1);
            cellPRef.setCellValue(product.getProductReference());

            Cell cellPDes = getCell(sheet, rowIndex, 2);
            cellPDes.setCellValue(product.getProductName());

            Cell cellPUnit = getCell(sheet, rowIndex, 3);
            cellPUnit.setCellValue(product.getUnit());

            Cell cellPQuantity = getCell(sheet, rowIndex, 4);
            cellPQuantity.setCellValue(product.getQuantity() == null ? "" : df.format(product.getQuantity()));

            if (!isBl) {
                Cell cellPrice = getCell(sheet, rowIndex, 5);
                cellPrice.setCellValue(product.getUnitPrice() == null ? "" : df.format(product.getUnitPrice()));

                Cell cellDiscount = getCell(sheet, rowIndex, 6);
                cellDiscount.setCellValue(product.getDiscount() == null ? "" : product.getDiscount().toString() + '%');

                Cell cellPriceHt = getCell(sheet, rowIndex, 7);
                cellPriceHt.setCellValue(product.getTotalPrice() == null ? "" : df.format(product.getTotalPrice()));
                cellPriceHt.getCellStyle().setAlignment(HorizontalAlignment.RIGHT);

                Cell cellTva = getCell(sheet, rowIndex, 8);
                cellTva.setCellValue(product.getTva() == null ? "" : product.getTva().toString() + '%');
                cellTva.getCellStyle().setAlignment(HorizontalAlignment.RIGHT);
            } else {
                BigDecimal unitPriceWithoutTva = product.getUnitPrice()
                        .subtract((product.getUnitPrice()
                                .multiply(product.getDiscount() == null ? BigDecimal.ZERO : BigDecimal.valueOf(product.getDiscount()))
                                .setScale(10, RoundingMode.FLOOR)
                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)));

                BigDecimal unitPriceWithTva = unitPriceWithoutTva
                        .add(unitPriceWithoutTva
                                .multiply(product.getTva() == null ? BigDecimal.ZERO : BigDecimal.valueOf(product.getTva()))
                                .setScale(10, RoundingMode.FLOOR)
                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));

                Cell cellPriceUnit = getCell(sheet, rowIndex, 5);
                cellPriceUnit.setCellValue(df.format(unitPriceWithTva));
                cellPriceUnit.getCellStyle().setAlignment(HorizontalAlignment.RIGHT);

                BigDecimal total = unitPriceWithTva
                        .multiply(product.getQuantity());

                Cell cellPriceTotalWithTva = getCell(sheet, rowIndex, 6);
                cellPriceTotalWithTva.setCellValue(df.format(total));
                cellPriceTotalWithTva.getCellStyle().setAlignment(HorizontalAlignment.RIGHT);

            }

            rowIndex++;
        }
    }

    private static void fillCommonData(Sheet sheet, InvoiceDto invoiceDto, DecimalFormat df, String reference, boolean isBl) {
        int clientColPos = isBl ? 5:6;
        Cell cellClientName = getCell(sheet, 2, clientColPos);
        cellClientName.setCellValue(invoiceDto.getClient().getName());

        Cell cellClientAddress = getCell(sheet, 5, clientColPos);
        cellClientAddress.setCellValue(invoiceDto.getClient().getAddress());

        Cell cellClientTel = getCell(sheet, 7, clientColPos);
        cellClientTel.setCellValue(invoiceDto.getClient().getTel());

        Cell cellClientFiscalId = getCell(sheet, 8, clientColPos);
        cellClientFiscalId.setCellValue(invoiceDto.getClient().getFiscalId());

        Cell cellNum = getCell(sheet, 11, isBl ? 3:4);
        cellNum.setCellValue("N°: " + reference);

        Cell cellDate = getCell(sheet, 11, isBl ? 5:6);
        cellDate.setCellValue(invoiceDto.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));

        if (!isBl) {
            Cell cellPriceText = getCell(sheet, 32, 1);
            cellPriceText.setCellValue(cellPriceText.getStringCellValue() + ' ' + invoiceDto.getPriceAsText());
            cellPriceText.getCellStyle().setWrapText(true);

            Cell cellTotalHt = getCell(sheet, 27, 7);
            cellTotalHt.setCellValue(invoiceDto.getTotalBrut() == null ? "" : df.format(invoiceDto.getTotalBrut()));

            Cell cellTotalTva = getCell(sheet, 28, 7);
            cellTotalTva.setCellValue(invoiceDto.getTotalTva() == null ? "" : df.format(invoiceDto.getTotalTva()));

            Cell cellTimbre = getCell(sheet, 29, 7);
            cellTimbre.setCellValue(invoiceDto.getTimbre() == null ? "" : invoiceDto.getTimbre().toString());

            Cell cellTotal = getCell(sheet, 30, 7);
            cellTotal.setCellValue(invoiceDto.getTotalNet() == null ? "" : df.format(invoiceDto.getTotalNet()));
        } else {
            Cell cellTotal = getCell(sheet, 27, 6);
            cellTotal.setCellValue(invoiceDto.getTotalNet() == null ? "" : df.format(invoiceDto.getTotalNet()));
        }

        Cell cellIntRef = getCell(sheet, isBl ? 35:36, 1);
        cellIntRef.setCellValue("ref:" + invoiceDto.getReference());
    }

    private static Cell getCell(Sheet sheet, int row, int col) {
        return sheet.getRow(row).getCell(col);
    }

}
