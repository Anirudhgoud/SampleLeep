package com.goleep.driverapp.helpers.uihelpers;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;

import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.helpers.uimodels.ReturnOrderItem;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ReturnOrderEntity;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.ngx.BluetoothPrinter;
import com.ngx.PrinterWidth;

import java.util.List;

/**
 * Created by vishalm on 14/04/18.
 */
public class PrinterHelper {
    private String seperator = "-------------------------------------------------------------";
    private float cashSalesTotal = 0;
    private float returnsTotal = 0;
    public void printInvoice(DeliveryOrderEntity deliveryOrder,
                             List<OrderItemEntity> products, List<Product> returnedProducts,
                             BluetoothPrinter printer, String currencySymbol, double paymentCollected){
        printer.setPrinterWidth(PrinterWidth.PRINT_WIDTH_48MM);
        TextPaint tp = new TextPaint();
        tp.setColor(Color.BLACK);
        tp.setTextSize(18);
        tp.setTypeface(Typeface.DEFAULT_BOLD);
        printer.addText(trim(deliveryOrder.getCustomerName(), 40)+"\n", Layout.Alignment.ALIGN_NORMAL, tp);
        tp.setTypeface(Typeface.DEFAULT);
        printer.addText(generateString(deliveryOrder, products, currencySymbol), Layout.Alignment.ALIGN_NORMAL, tp);
        printer.addText(seperator+"\n");
        tp.setTypeface(Typeface.DEFAULT_BOLD);
        printer.addText("Total"+cashSalesTotal+"\n", Layout.Alignment.ALIGN_OPPOSITE, tp);
        tp.setTypeface(Typeface.DEFAULT);
        printer.addText(seperator+"\n\n");
        if(returnedProducts != null) {
            printer.addText(generateReturnProductsString(returnedProducts, currencySymbol));
            printer.addText(seperator + "\n");
            tp.setTypeface(Typeface.DEFAULT_BOLD);
            printer.addText("Total" + returnsTotal + "\n", Layout.Alignment.ALIGN_OPPOSITE, tp);
            tp.setTypeface(Typeface.DEFAULT);
            printer.addText(seperator + "\n\n");
        }
        printer.addText("Payment Collected "+paymentCollected+"\n");
        printer.addText(seperator+"\n\n");
        printer.print();
    }

    private String trim(String string, int maxLength) {
        if(string.length() <= maxLength)
            return string;
        return string.substring(0, maxLength);
    }

    private String generateString(DeliveryOrderEntity deliveryOrder, List<OrderItemEntity> products,
                                  String currencySymbol){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(trim("DO No. "+deliveryOrder.getDoNumber(), 40)+"\n");
        stringBuilder.append(StringUtils.getAddress(deliveryOrder.getDestinationAddressLine1(), deliveryOrder.getDestinationAddressLine2())+"\n");
        if(deliveryOrder.getCity() != null && deliveryOrder.getState() != null && deliveryOrder.getPincode() != null)
            stringBuilder.append(deliveryOrder.getCity()+" , "+deliveryOrder.getState()+" - "+deliveryOrder.getPincode()+"\n");
        stringBuilder.append(DateTimeUtils.currentTimeToDisplay()+" "+DateTimeUtils.currentDateToDisplay()+"\n");
        stringBuilder.append("\n"+"Current Sale"+"\n");
        stringBuilder.append(seperator+"\n");
        stringBuilder.append(String.format("%-45s", "Items")+"   "+String.format("%-15s", "Units")+"   "+String.format("%-10s", "Value")+"\n\n");
        for(OrderItemEntity orderItemEntity : products){
            cashSalesTotal += orderItemEntity.getQuantity() * orderItemEntity.getPrice();
            stringBuilder.append(String.format("%-25s", orderItemEntity.getProduct().getName())+"   "+
                    String.format("%-15s", orderItemEntity.getQuantity())+"   "+
                    String.format("%-10s", currencySymbol+String.valueOf(orderItemEntity.getQuantity() * orderItemEntity.getPrice()))+"\n");
            stringBuilder.append("("+orderItemEntity.getProduct().getWeight()+orderItemEntity.getProduct().getWeightUnit()+")"+"\n");
        }
        return stringBuilder.toString();
    }

    private String generateReturnProductsString(List<Product> returnedProducts, String currencySymbol){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n"+"Returns"+"\n");
        stringBuilder.append(seperator+"\n");
        stringBuilder.append(String.format("%-45s", "Items")+"   "+String.format("%-15s", "Units")+"   "+String.format("%-10s", "Value")+"\n\n");
        for(Product product : returnedProducts){
            returnsTotal += product.getQuantity() * product.getPrice();
            stringBuilder.append(String.format("%-25s", product.getProductName())+"   "+
                    String.format("%-15s", product.getQuantity())+"   "+
                    String.format("%-10s", currencySymbol+String.valueOf(product.getQuantity() * product.getPrice()))+"\n");
            stringBuilder.append("("+product.getWeight()+product.getWeightUnit()+")"+"\n");
        }
        return stringBuilder.toString();
    }
}
