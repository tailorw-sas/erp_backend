package com.kynsoft.finamer.payment.infrastructure.services.http.helper;

import com.kynsof.share.core.domain.http.entity.BookingHttp;
import com.kynsoft.finamer.payment.domain.dto.*;
import com.kynsoft.finamer.payment.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.payment.domain.services.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class BookingImportAutomaticeHelperServiceImpl {

    private final IManageInvoiceService invoiceService;
    private final IManageBookingService bookingService;
    private final IManageHotelService hotelService;
    private final IManageAgencyService manageAgencyService;
    private final IManageInvoiceStatusService manageInvoiceStatusService;

    public BookingImportAutomaticeHelperServiceImpl(IManageInvoiceService invoiceService,
                                                    IManageBookingService bookingService,
                                                    IManageHotelService hotelService,
                                                    IManageAgencyService manageAgencyService,
                                                    IManageInvoiceStatusService manageInvoiceStatusService) {
        this.invoiceService = invoiceService;
        this.bookingService = bookingService;
        this.hotelService = hotelService;
        this.manageAgencyService = manageAgencyService;
        this.manageInvoiceStatusService = manageInvoiceStatusService;
    }

    public void createInvoice(BookingHttp bookingHttp) {
        ManageHotelDto hotelDto = this.hotelService.findById(bookingHttp.getInvoice().getHotel());
        ManageAgencyDto agencyDto = this.manageAgencyService.findById(bookingHttp.getInvoice().getAgency());
        List<ManageBookingDto> bookingDtos = new ArrayList<>();
        create(bookingHttp, bookingDtos, hotelDto, agencyDto);
    }

    private void create(BookingHttp bookingHttp, List<ManageBookingDto> bookingDtos, ManageHotelDto hotelDto, ManageAgencyDto agencyDto) {
        List<UUID> invoiceStatusIds = bookingDtos.stream()
                .filter(bookingDto -> Objects.nonNull(bookingDto.getInvoice()) && Objects.nonNull(bookingDto.getInvoice().getStatus()))
                .map(bookingDto -> bookingDto.getInvoice().getStatus().getId())
                .collect(Collectors.toList());

        Map<UUID, ManageInvoiceStatusDto> invoiceStatusDtoMap = this.getInvoiceStatusMap(invoiceStatusIds);

        createBookingList(bookingHttp.getInvoice().getBookings(), bookingDtos);
        ManageInvoiceDto invoiceDto = new ManageInvoiceDto(
                bookingHttp.getInvoice().getId(),
                bookingHttp.getInvoice().getInvoiceId(),
                bookingHttp.getInvoice().getInvoiceNo(),
                deleteHotelInfo(bookingHttp.getInvoice().getInvoiceNumber()),
                EInvoiceType.valueOf(bookingHttp.getInvoice().getInvoiceType()),
                bookingHttp.getInvoice().getInvoiceAmount(),
                bookingHttp.getInvoice().getInvoiceBalance(),
                bookingDtos,
                bookingHttp.getInvoice().getHasAttachment(), //!= null ? objKafka.getHasAttachment() : false
                bookingHttp.getInvoice().getInvoiceParent() != null ? this.invoiceService.findById(bookingHttp.getInvoice().getInvoiceParent()) : null,
                LocalDateTime.parse(bookingHttp.getInvoice().getInvoiceDate()),
                hotelDto,
                agencyDto,
                bookingHttp.getInvoice().getAutoRec(),
                this.getInvoiceStatusFromMap(invoiceStatusDtoMap, bookingHttp.getInvoice().getInvoiceStatus())
        );

        this.invoiceService.create(invoiceDto);

    }

    private void createBookingList(List<BookingHttp> bookings, List<ManageBookingDto> bookingDtos) {
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
                    null,
                    booking.getBookingParent() != null ? this.bookingService.findById(booking.getBookingParent()) : null,
                    LocalDateTime.parse(booking.getBookingDate())
            ));
        }
    }

    private Map<UUID, ManageInvoiceStatusDto> getInvoiceStatusMap(List<UUID> invoiceStatusIds){
        if(Objects.nonNull(invoiceStatusIds) || !invoiceStatusIds.isEmpty()){
            return this.manageInvoiceStatusService.findByIds(invoiceStatusIds).stream()
                    .collect(Collectors.toMap(ManageInvoiceStatusDto::getId, manageInvoiceStatusDto -> manageInvoiceStatusDto));
        }
        return null;
    }

    private ManageInvoiceStatusDto getInvoiceStatusFromMap(Map<UUID, ManageInvoiceStatusDto> map, UUID id){
        if(Objects.nonNull(map) && Objects.nonNull(id)){
            return map.get(id);
        }

        return null;
    }

    private String deleteHotelInfo(String input) {
        return input.replaceAll("-(.*?)-", "-");
    }

}
