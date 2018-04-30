package com.goleep.driverapp.helpers.uihelpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.TextPaint;

import com.goleep.driverapp.R;
import com.goleep.driverapp.helpers.uimodels.Customer;
import com.goleep.driverapp.helpers.uimodels.Product;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.ngx.BluetoothPrinter;
import com.ngx.PrinterWidth;

import java.util.List;

/**
 * Created by vishalm on 14/04/18.
 */
public class PrinterHelper {
    private String separator = "-------------------------------------------------------------";
    private float cashSalesTotal = 0;
    private float returnsTotal = 0;
    private Resources resources;
    private String itemsHeader;
    private BluetoothPrinter printer;

    public void printInvoice(Context context, DeliveryOrderEntity deliveryOrder,
                             List<OrderItemEntity> products, List<Product> returnedProducts,
                             BluetoothPrinter printer, String currencySymbol, double paymentCollected){
        initPrinter(printer, context);
        TextPaint tp = new TextPaint();
        tp.setColor(Color.BLACK);
        tp.setTextSize(18);
        tp.setTypeface(Typeface.DEFAULT_BOLD);
        printer.addText(trim(deliveryOrder.getCustomerName(), 40)+"\n", Layout.Alignment.ALIGN_NORMAL, tp);
        tp.setTypeface(Typeface.DEFAULT);
        printer.addText(generateString(deliveryOrder, products, currencySymbol), Layout.Alignment.ALIGN_NORMAL, tp);
        printer.addText(separator +"\n",  Layout.Alignment.ALIGN_NORMAL, tp);
        tp.setTypeface(Typeface.DEFAULT_BOLD);
        printer.addText(resources.getString(R.string.total)+" "+cashSalesTotal+"  \n", Layout.Alignment.ALIGN_OPPOSITE, tp);
        tp.setTypeface(Typeface.DEFAULT);
        printer.addText(separator +"\n\n", Layout.Alignment.ALIGN_NORMAL, tp);
        if(returnedProducts != null) {
            printer.addText(generateReturnProductsString(returnedProducts, currencySymbol), Layout.Alignment.ALIGN_NORMAL, tp);
            printer.addText(separator + "\n", Layout.Alignment.ALIGN_NORMAL, tp);
            tp.setTypeface(Typeface.DEFAULT_BOLD);
            printer.addText(resources.getString(R.string.total)+" "+ returnsTotal + "\n", Layout.Alignment.ALIGN_OPPOSITE, tp);
            tp.setTypeface(Typeface.DEFAULT);
            printer.addText(separator + "\n\n", Layout.Alignment.ALIGN_NORMAL, tp);
        }
        tp.setTypeface(Typeface.DEFAULT);
        printer.addText(resources.getString(R.string.grand_total)+" "+(cashSalesTotal-returnsTotal)+"\n", Layout.Alignment.ALIGN_NORMAL, tp);
        printer.addText(separator +"\n",  Layout.Alignment.ALIGN_NORMAL, tp);
        tp.setTypeface(Typeface.DEFAULT_BOLD);
        printer.addText(resources.getString(R.string.payment_collected)+" "+paymentCollected+"\n", Layout.Alignment.ALIGN_NORMAL, tp);
        tp.setTypeface(Typeface.DEFAULT);
        printer.addText(separator +"\n\n", Layout.Alignment.ALIGN_NORMAL, tp);
        printer.print();
    }

    private void initPrinter(BluetoothPrinter printer, Context context){
        this.printer = printer;
        printer.setPrinterWidth(PrinterWidth.PRINT_WIDTH_48MM);
        resources = context.getResources();
        itemsHeader =  String.format("%-45s", resources.getString(R.string.items)+" ")+"   "+String.format("%-15s", resources.getString(R.string.units)+" ")+"   "+String.format("%-10s", resources.getString(R.string.value)+" ")+"\n\n";
    }

    private String trim(String string, int maxLength) {
        if(string.length() <= maxLength)
            return string;
        return string.substring(0, maxLength);
    }

    private String topInfoString(DeliveryOrderEntity deliveryOrder){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(trim(resources.getString(R.string.do_number)+" "+deliveryOrder.getDoNumber(), 40)+"\n");
        stringBuilder.append(StringUtils.getAddress(deliveryOrder.getDestinationAddressLine1(), deliveryOrder.getDestinationAddressLine2())+"\n");
        if(deliveryOrder.getCity() != null && deliveryOrder.getState() != null && deliveryOrder.getPincode() != null)
            stringBuilder.append(deliveryOrder.getCity()+" , "+deliveryOrder.getState()+" - "+deliveryOrder.getPincode()+"\n");
        stringBuilder.append(DateTimeUtils.currentTimeToDisplay()+" "+DateTimeUtils.currentDateToDisplay()+"\n");

        return  stringBuilder.toString();
    }

    private String topInfoString(Customer customer){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(customer.getArea()+"\n");
        stringBuilder.append(DateTimeUtils.currentTimeToDisplay()+" "+DateTimeUtils.currentDateToDisplay()+"\n");

        return  stringBuilder.toString();
    }

    private String generateString(DeliveryOrderEntity deliveryOrder, List<OrderItemEntity> products,
                                  String currencySymbol){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(topInfoString(deliveryOrder));
        stringBuilder.append("\n"+resources.getString(R.string.current_sale)+"\n");
        stringBuilder.append(separator +"\n");
        stringBuilder.append(itemsHeader);
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
        stringBuilder.append("\n"+resources.getString(R.string.returns)+"\n");
        stringBuilder.append(separator +"\n");
        stringBuilder.append(itemsHeader);
        for(Product product : returnedProducts){
            returnsTotal += product.getQuantity() * product.getPrice();
            stringBuilder.append(String.format("%-25s", product.getProductName())+"   "+
                    String.format("%-15s", product.getQuantity())+"   "+
                    String.format("%-10s", currencySymbol+String.valueOf(product.getQuantity() * product.getPrice()))+"\n");
            stringBuilder.append("("+product.getWeight()+product.getWeightUnit()+")"+"\n");
        }
        return stringBuilder.toString();
    }


    public void printInvoice(Context context, Customer customer, List<Product> products,
                             BluetoothPrinter printer, String currencySymbol, double paymentCollected){
        initPrinter(printer, context);
        TextPaint tp = new TextPaint();
        tp.setColor(Color.BLACK);
        tp.setTextSize(18);
        printer.addText(generateCashSalesItems(customer, products, currencySymbol));
        printer.addText(separator +"\n",  Layout.Alignment.ALIGN_NORMAL, tp);
        tp.setTypeface(Typeface.DEFAULT_BOLD);
        printer.addText(resources.getString(R.string.total)+" "+cashSalesTotal+"  \n", Layout.Alignment.ALIGN_OPPOSITE, tp);
        tp.setTypeface(Typeface.DEFAULT);
        printer.addText(separator +"\n\n", Layout.Alignment.ALIGN_NORMAL, tp);
        printer.addText(generateReturnProductsString(products, currencySymbol));
        printer.addText(separator + "\n", Layout.Alignment.ALIGN_NORMAL, tp);
        tp.setTypeface(Typeface.DEFAULT_BOLD);
        printer.addText(resources.getString(R.string.total)+" "+ returnsTotal + "\n", Layout.Alignment.ALIGN_OPPOSITE, tp);
        tp.setTypeface(Typeface.DEFAULT);
        printer.addText(separator + "\n\n", Layout.Alignment.ALIGN_NORMAL, tp);
        tp.setTypeface(Typeface.DEFAULT);
        printer.addText(resources.getString(R.string.grand_total)+" "+(cashSalesTotal-returnsTotal)+"\n", Layout.Alignment.ALIGN_NORMAL, tp);
        printer.addText(separator +"\n",  Layout.Alignment.ALIGN_NORMAL, tp);
        tp.setTypeface(Typeface.DEFAULT_BOLD);
        printer.addText(resources.getString(R.string.payment_collected)+" "+paymentCollected+"\n", Layout.Alignment.ALIGN_NORMAL, tp);
        tp.setTypeface(Typeface.DEFAULT);
        printer.addText(separator +"\n\n", Layout.Alignment.ALIGN_NORMAL, tp);
        printer.print();
    }

    private String generateCashSalesItems(Customer customer, List<Product> products, String currencySymbol) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(topInfoString(customer));
        stringBuilder.append("\n"+resources.getString(R.string.current_sale)+"\n");
        stringBuilder.append(separator +"\n");
        stringBuilder.append(itemsHeader);
        for(Product product : products){
            if(product.getReturnQuantity() < 1) {
                cashSalesTotal += product.getReturnQuantity() * product.getPrice();
                stringBuilder.append(String.format("%-25s", product.getProductName()) + "   " +
                        String.format("%-15s", product.getReturnQuantity()) + "   " +
                        String.format("%-10s", currencySymbol + String.valueOf(product.getQuantity() * product.getPrice())) + "\n");
                stringBuilder.append("(" + product.getWeight() + product.getWeightUnit() + ")" + "\n");
            }
        }
        return stringBuilder.toString();
    }
}
