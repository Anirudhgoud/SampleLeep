package com.goleep.driverapp.helpers.uihelpers;

import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.ngx.BluetoothPrinter;
import com.ngx.BtpCommands;

import java.util.List;

/**
 * Created by vishalm on 14/04/18.
 */
public class PrinterHelper {
    public void printInvoice(DeliveryOrderEntity deliveryOrder,
                             List<OrderItemEntity> products, List<Product> returnedProducts, BluetoothPrinter printer){
        printer.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
        printer.setPrintFontSize(BtpCommands.FONT_SIZE_NORMAL);
        printer.printText(trim(deliveryOrder.getCustomerName(), 40));
        printer.printText(trim("DO No. "+deliveryOrder.getDoNumber(), 40));
        printer.setPrintFontStyle(BtpCommands.FONT_SIZE_NORMAL);
        printer.printText(StringUtils.getAddress(deliveryOrder.getDestinationAddressLine1(), deliveryOrder.getDestinationAddressLine2()));
        printer.printText(deliveryOrder.getCity()+" ,"+deliveryOrder.getState()+" - "+deliveryOrder.getPincode());
        printer.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
        printer.printText(DateTimeUtils.currentTimeToDisplay()+" "+DateTimeUtils.currentDateToDisplay());
        printer.printText("Current Sale");
        printer.printText("----------------------------------------");
        printer.printText(String.format("%-19s", "Items")+"   "+String.format("%-5s", "Units")+"   "+String.format("%-10s", "Value"));
        printer.setPrintFontStyle(BtpCommands.FONT_SIZE_NORMAL);
        for(OrderItemEntity orderItemEntity : products){
            printer.printText(String.format("%-19s", orderItemEntity.getProduct().getName())+"   "+
                    String.format("%-5s", orderItemEntity.getQuantity())+"   "+
                    String.format("%-10s", String.valueOf(orderItemEntity.getQuantity() * orderItemEntity.getPrice())));
            printer.printText("("+orderItemEntity.getProduct().getWeight()+orderItemEntity.getProduct().getWeightUnit()+")");
        }

        printer.printText("----------------------------------------");
        printer.printText("Total");
        printer.printText("----------------------------------------");
        if(returnedProducts != null) {
            printer.printText("Returns");
            printer.printText("----------------------------------------");
            printer.printText(String.format("%-19s", "Items") + "   " + String.format("%-5s", "Units") + "   " + String.format("%-10s", "Value"));
            printer.printText("----------------------------------------");
            printer.printText("Total");
            printer.printText("----------------------------------------");
            printer.printText("Grand Total");
            printer.printText("Returned");
            printer.printText("----------------------------------------");
        }
        printer.setPrintFontStyle(BtpCommands.FONT_STYLE_BOLD);
        printer.printText("Payment Collected");
        printer.setPrintFontStyle(BtpCommands.FONT_SIZE_NORMAL);
        printer.printText("----------------------------------------");
    }

    private String trim(String string, int maxLength) {
        if(string.length() <= maxLength)
            return string;
        return string.substring(0, maxLength);
    }


}
