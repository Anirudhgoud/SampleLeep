package com.goleep.driverapp.utils;

import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.ngx.BluetoothPrinter;
import com.ngx.BtpCommands;

import java.util.List;

/**
 * Created by vishalm on 14/04/18.
 */
public class PrinterUtils {
    public static void printInvoice(DeliveryOrderEntity deliveryOrder,
                                             List<OrderItemEntity> products, BluetoothPrinter printer){
        printer.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
        printer.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
        printer.printText(trim(deliveryOrder.getCustomerName(), 40));
        printer.printText(trim("DO No. "+deliveryOrder.getDoNumber(), 40));
        printer.setPrintFontStyle(BtpCommands.FONT_SIZE_NORMAL);
        printer.printText(StringUtils.getAddress(deliveryOrder.getDestinationAddressLine1(), deliveryOrder.getDestinationAddressLine2()));
        printer.printText(deliveryOrder.getCity()+" ,"+deliveryOrder.getState()+" - "+deliveryOrder.getPincode());
        printer.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
        //printer.printText(deliveryOrder.getT)
    }

    private static String trim(String string, int maxLength) {
        if(string.length() <= maxLength)
            return string;
        return string.substring(0, maxLength);
    }


}
