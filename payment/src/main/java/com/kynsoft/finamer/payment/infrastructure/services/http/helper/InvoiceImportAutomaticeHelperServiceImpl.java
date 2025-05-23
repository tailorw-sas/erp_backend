package com.kynsoft.finamer.payment.infrastructure.services.http.helper;

import com.kynsof.share.core.domain.http.entity.BookingHttp;
import com.kynsof.share.core.domain.http.entity.InvoiceHttp;
import com.kynsoft.finamer.payment.domain.dto.ManageAgencyDto;
import com.kynsoft.finamer.payment.domain.dto.ManageBookingDto;
import com.kynsoft.finamer.payment.domain.dto.ManageHotelDto;
import com.kynsoft.finamer.payment.domain.dto.ManageInvoiceDto;
import com.kynsoft.finamer.payment.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.payment.domain.services.IManageAgencyService;
import com.kynsoft.finamer.payment.domain.services.IManageBookingService;
import com.kynsoft.finamer.payment.domain.services.IManageHotelService;
import com.kynsoft.finamer.payment.domain.services.IManageInvoiceService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceImportAutomaticeHelperServiceImpl {

    private final IManageInvoiceService invoiceService;
    private final IManageBookingService bookingService;
    private final IManageHotelService hotelService;
    private final IManageAgencyService manageAgencyService;

    public InvoiceImportAutomaticeHelperServiceImpl(IManageInvoiceService invoiceService,
            IManageBookingService bookingService,
            IManageHotelService hotelService,
            IManageAgencyService manageAgencyService) {
        this.invoiceService = invoiceService;
        this.bookingService = bookingService;
        this.hotelService = hotelService;
        this.manageAgencyService = manageAgencyService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createInvoice(InvoiceHttp invoiceHttp) {
        ManageHotelDto hotelDto = this.hotelService.findById(invoiceHttp.getHotel());
        ManageAgencyDto agencyDto = this.manageAgencyService.findById(invoiceHttp.getAgency());
        ManageInvoiceDto invoice = this.getInvoice(invoiceHttp, hotelDto, agencyDto);
        List<ManageBookingDto> bookingDtos = new ArrayList<>();
        createBookingList(invoiceHttp.getBookings(), bookingDtos, invoice);
        create(bookingDtos, invoice);
    }

    private ManageInvoiceDto getInvoice(InvoiceHttp invoiceHttp, ManageHotelDto hotelDto, ManageAgencyDto agencyDto){
        return new ManageInvoiceDto(
                invoiceHttp.getId(),
                invoiceHttp.getInvoiceId(),
                invoiceHttp.getInvoiceNo(),
                deleteHotelInfo(invoiceHttp.getInvoiceNumber()),
                EInvoiceType.valueOf(invoiceHttp.getInvoiceType()),
                invoiceHttp.getInvoiceAmount(),
                new ArrayList<>(),
                invoiceHttp.getHasAttachment(), //!= null ? objKafka.getHasAttachment() : false
                invoiceHttp.getInvoiceParent() != null ? this.invoiceService.findById(invoiceHttp.getInvoiceParent()) : null,
                LocalDateTime.parse(invoiceHttp.getInvoiceDate()),
                hotelDto,
                agencyDto,
                invoiceHttp.getAutoRec()
        );
    }

    private void create(List<ManageBookingDto> bookingDtos, ManageInvoiceDto invoice) {
        this.invoiceService.create(invoice);
        this.bookingService.createAll(bookingDtos);
    }

    private void createBookingList(List<BookingHttp> bookings, List<ManageBookingDto> bookingDtos, ManageInvoiceDto invoice) {
        for (BookingHttp booking : bookings) {
            bookingDtos.add(new ManageBookingDto(
                    booking.getId(),
                    booking.getBookingId(),
                    booking.getReservationNumber(),
                    LocalDateTime.parse(booking.getCheckIn()),
                    LocalDateTime.parse(booking.getCheckOut()),
                    booking.getFullName(),
                    booking.getFirstName(),
                    booking.getLastName(),
                    booking.getInvoiceAmount(),
                    booking.getAmountBalance(),
                    booking.getCouponNumber(),
                    booking.getAdults(),
                    booking.getChildren(),
                    invoice,
                    booking.getBookingParent() != null ? this.bookingService.findById(booking.getBookingParent()) : null,
                    LocalDateTime.parse(booking.getBookingDate())
            ));
        }
    }

    private String deleteHotelInfo(String input) {
        return input.replaceAll("-(.*?)-", "-");
    }

}
