package com.kynsoft.finamer.invoicing.infrastructure.excel.validators.reconcileauto;

import com.kynsof.share.core.application.excel.ExcelUtils;
import com.kynsof.share.core.domain.exception.ExcelException;
import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsoft.finamer.invoicing.domain.dto.ManageBookingDto;
import com.kynsoft.finamer.invoicing.domain.dto.ManageInvoiceDto;
import com.kynsoft.finamer.invoicing.domain.services.IManageNightTypeService;
import com.kynsoft.finamer.invoicing.infrastructure.utils.AgencyCouponFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ReconcileAutomaticInvoiceValidator {
    private Workbook workbook;
    private Sheet sheet;

    private final IManageNightTypeService nightTypeService;

    public ReconcileAutomaticInvoiceValidator(IManageNightTypeService nightTypeService) {
        this.nightTypeService = nightTypeService;
    }


    public void loadWorkbook(byte[] file) throws IOException {
        this.workbook = WorkbookFactory.create(new ByteArrayInputStream(file));
        this.sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
    }

    public void closeWorkbook() throws IOException {
        if (Objects.nonNull(workbook)){
            workbook.close();
        }
    }

    public boolean hasContent() {
        boolean empty = true;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            if (!ExcelUtils.isRowEmpty(sheet.getRow(i))) {
                empty = false;
                break;
            }
        }
        if (ExcelUtils.isSheetEmpty(sheet) || empty) {
            throw new ExcelException("There is no data to import.");
        }
        return empty;
    }

    protected boolean isDateSeparator(Row currentRow) {
        for (int cellNum = 1; cellNum < currentRow.getLastCellNum(); cellNum++) {
            if (currentRow.getCell(cellNum) != null ||
                    currentRow.getCell(cellNum).getCellType() != CellType.BLANK ||
                    StringUtils.isNotBlank(currentRow.getCell(cellNum).toString())) {
                return false;
            }
        }
        return true;
    }

    public List<ErrorField> validateInvoice(ManageInvoiceDto manageInvoiceDto) {
        List<ErrorField> errorFieldList = new ArrayList<>();
        List<ManageBookingDto> bookingDtos = manageInvoiceDto.getBookings();
        String couponFormat = manageInvoiceDto.getAgency().getBookingCouponFormat();
        for (ManageBookingDto booking : bookingDtos) {
            Optional<List<Row>> matchBookingInRow = searchBookingInTheFile(booking);
            List<Row> allRow = matchBookingInRow.get();
            if (allRow.isEmpty()) {
                errorFieldList.add(new ErrorField("Booking", "All bookings are not in the file"));
                return errorFieldList;
            } else {
                DataFormatter formatter = new DataFormatter();
                for (Row currentRow : allRow) {
                    errorFieldList.clear();
                    String couponNumber = formatter.formatCellValue(currentRow.getCell(4)) + formatter.formatCellValue(currentRow.getCell(11));
                    String nightType = formatter.formatCellValue(currentRow.getCell(13));
                    String price = formatter.formatCellValue(currentRow.getCell(39));
                    String contract = formatter.formatCellValue(currentRow.getCell(0));
                    String reservationNumber = currentRow.getCell(22).getStringCellValue();
                    if (validateCouponNumber(couponNumber, booking, errorFieldList
                    ) && validatePrice(price, booking, errorFieldList)
                            && validateNightType(nightType, booking, errorFieldList)
                            && validateReservationNumber(reservationNumber, couponFormat, errorFieldList)) {
                        booking.setContract(contract);
                        errorFieldList.clear();
                        break;
                    }

                }

            }
        }
        return errorFieldList;
    }

    private Optional<List<Row>> searchBookingInTheFile(ManageBookingDto manageBookingDto) {
        List<Row> allMatch = new ArrayList<>();
        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Row currentRow = sheet.getRow(i);
            if (ExcelUtils.isRowEmpty(currentRow) || this.isDateSeparator(currentRow)) {
                continue;
            }
            String reservationNumber = currentRow.getCell(22).getStringCellValue();
            if (reservationNumber.equals(manageBookingDto.getHotelBookingNumber())) {
                allMatch.add(currentRow);
            }
        }
        return Optional.of(allMatch);
    }


    private boolean validateCouponNumber(String couponNumber, ManageBookingDto manageBookingDto, List<ErrorField> errors) {
        if (!couponNumber.equals(manageBookingDto.getCouponNumber())) {
            errors.add(new ErrorField("CouponNumber", "The coupon number not match with the file"));
            return false;
        }
        return true;
    }

    private boolean validateNightType(String nightType, ManageBookingDto manageBookingDto, List<ErrorField> errors) {
        if (!nightTypeService.existNightTypeByCode(nightType)) {
            errors.add(new ErrorField("Night Type", "The night type not exist"));
            return false;
        }
        return true;
    }

    private boolean validatePrice(String price, ManageBookingDto manageBookingDto, List<ErrorField> errors) {
        String[] priceSplit = price.split("Price:");
        Double priceAmount = Double.parseDouble(priceSplit[1]);
        if (!priceAmount.equals(manageBookingDto.getDueAmount())) {
            errors.add(new ErrorField("Price", "The price not match with the file"));
            return false;
        }
        return true;
    }

    private boolean validateReservationNumber(String reservationNumber, String couponFormat, List<ErrorField> errors){
        if (!AgencyCouponFormatUtils.validateCode(reservationNumber, couponFormat)){
            errors.add(new ErrorField("reservationNumber", "The reservation number is not valid."));
            return false;
        }
        return true;
    }
}
