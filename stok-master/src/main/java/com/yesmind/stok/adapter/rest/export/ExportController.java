package com.yesmind.stok.adapter.rest.export;

import com.yesmind.stok.application.exception.TemplateNotFoundException;
import com.yesmind.stok.core.port.in.invoice.IInvoiceExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ExportController implements IExportController {

    private final IInvoiceExportService invoiceExportService;

    @Override
    @GetMapping(value = "/export/invoice/{publicId}")
    public ResponseEntity<byte[]> exportInvoice(@PathVariable("publicId") UUID publicId) throws TemplateNotFoundException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(invoiceExportService.export(publicId));
    }

    @Override
    @GetMapping(value = "/export/bl/{publicId}")
    public ResponseEntity<byte[]> exportBl(@PathVariable("publicId") UUID publicId) throws TemplateNotFoundException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=export.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(invoiceExportService.exportBl(publicId));
    }

}
