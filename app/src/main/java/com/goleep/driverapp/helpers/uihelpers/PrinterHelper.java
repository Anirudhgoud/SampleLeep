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
import com.goleep.driverapp.helpers.uimodels.ReturnOrderItem;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ReturnOrderEntity;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.ngx.BluetoothPrinter;
import com.ngx.PrinterWidth;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.goleep.driverapp.utils.DateTimeUtils.ORDER_DISPLAY_DATE_FORMAT;
import static com.goleep.driverapp.utils.DateTimeUtils.RAILS_TIMESTAMP_FORMAT;
import static com.goleep.driverapp.utils.DateTimeUtils.TWELVE_HOUR_TIME_FORMAT;

/**
 * Created by vishalm on 14/04/18.
 */
public class PrinterHelper {
    private String separator = "----------------------------------------------------------------";
    private double cashSalesTotal = 0;
    private double returnsTotal = 0;
    private Resources resources;
    private String itemsHeader;
    private BluetoothPrinter printer;

    private TextPaint boldTextPaint;
    private TextPaint normalTextPaint;



    public void printInvoice(Context context, String doNumber, String roNumber, Customer customer, List<Product> products,
                             BluetoothPrinter bluetoothPrinter, String currencySymbol, double paymentCollected){
        initPrinter(bluetoothPrinter, context);
        TextPaint tp = new TextPaint();
        tp.setColor(Color.BLACK);
        tp.setTextSize(18);
        tp.setTypeface(Typeface.DEFAULT_BOLD);
        printer.addText(customer.getName()+"\n", Layout.Alignment.ALIGN_NORMAL, tp);
        tp.setTypeface(Typeface.DEFAULT);
        printer.addText(topInfoString(customer, doNumber, roNumber), Layout.Alignment.ALIGN_NORMAL, tp);
        printer.addText(generateCashSalesItems(products, currencySymbol), Layout.Alignment.ALIGN_NORMAL, tp);
        if(cashSalesTotal >0){
            printer.addText(separator +"\n",  Layout.Alignment.ALIGN_NORMAL, tp);
            tp.setTypeface(Typeface.DEFAULT_BOLD);
            printer.addText(resources.getString(R.string.total)+" "+cashSalesTotal+"  \n", Layout.Alignment.ALIGN_OPPOSITE, tp);
            tp.setTypeface(Typeface.DEFAULT);
            printer.addText(separator +"\n", Layout.Alignment.ALIGN_NORMAL, tp);
        }
        if(roNumber != null && !roNumber.isEmpty()) {
            printer.addText(generateReturnProductsString(products, currencySymbol), Layout.Alignment.ALIGN_NORMAL, tp);
            printer.addText(separator + "\n", Layout.Alignment.ALIGN_NORMAL, tp);
            tp.setTypeface(Typeface.DEFAULT_BOLD);
            printer.addText(resources.getString(R.string.total) + " " + returnsTotal + "\n", Layout.Alignment.ALIGN_OPPOSITE, tp);
            tp.setTypeface(Typeface.DEFAULT);
            printer.addText(separator + "\n", Layout.Alignment.ALIGN_NORMAL, tp);
        }
        tp.setTypeface(Typeface.DEFAULT);
        printer.addText(resources.getString(R.string.grand_total)+" "+(cashSalesTotal-returnsTotal)+"\n", Layout.Alignment.ALIGN_NORMAL, tp);
        if(returnsTotal > 0){
            printer.addText(resources.getString(R.string.returned)+" "+returnsTotal+"\n", Layout.Alignment.ALIGN_NORMAL, tp);
        }
        printer.addText(separator +"\n",  Layout.Alignment.ALIGN_NORMAL, tp);
        tp.setTypeface(Typeface.DEFAULT_BOLD);
        printer.addText(resources.getString(R.string.payment_collected)+" "+paymentCollected+"\n", Layout.Alignment.ALIGN_NORMAL, tp);
        tp.setTypeface(Typeface.DEFAULT);
        printer.addText(separator +"\n", Layout.Alignment.ALIGN_NORMAL, tp);
        printer.print();
    }

    private void initPrinter(BluetoothPrinter bluetoothPrinter, Context context){
        this.printer = bluetoothPrinter;
        printer.setPrinterWidth(PrinterWidth.PRINT_WIDTH_48MM);
        printer.addText("\n");
        resources = context.getResources();

    }

    private String trim(String string, int maxLength) {
        if(string.length() <= maxLength)
            return string;
        return string.substring(0, maxLength);
    }

    private String topInfoString(DeliveryOrderEntity deliveryOrder){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(resources.getString(R.string.do_number)+" "+
                deliveryOrder.getDoNumber()+"\n");
        stringBuilder.append(StringUtils.getAddress(deliveryOrder.getDestinationAddressLine1(),
                deliveryOrder.getDestinationAddressLine2())+"\n");
        if(deliveryOrder.getCity() != null && deliveryOrder.getState() != null && deliveryOrder.getPincode() != null)
            stringBuilder.append(deliveryOrder.getCity()+" , "+deliveryOrder.getState()+" - "+deliveryOrder.getPincode()+"\n");


        return  stringBuilder.toString();
    }

    private String topInfoString(ReturnOrderEntity returnOrderEntity){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(resources.getString(R.string.ro_number)+" "+
                returnOrderEntity.getRoNumber()+"\n");
        stringBuilder.append(StringUtils.getAddress(returnOrderEntity.getSourceAddressLine1(),
                returnOrderEntity.getSourceAddressLine2())+"\n");
        stringBuilder.append(stringBuilder.append(DateTimeUtils.convertdDate(returnOrderEntity.getActualReturnAt(),
                RAILS_TIMESTAMP_FORMAT, ORDER_DISPLAY_DATE_FORMAT)+" "+
                DateTimeUtils.convertdDate(returnOrderEntity.getActualReturnAt(),
                        RAILS_TIMESTAMP_FORMAT, TWELVE_HOUR_TIME_FORMAT))+"\n");
        return  stringBuilder.toString();
    }

    private String topInfoString(Customer customer, String doNumber, String roNumber){
        StringBuilder stringBuilder = new StringBuilder();
        if(doNumber != null)
            stringBuilder.append(resources.getString(R.string.do_number)+" "+ doNumber+"\n");
        if(roNumber != null)
            stringBuilder.append(resources.getString(R.string.ro_number)+" "+ roNumber+"\n");
        stringBuilder.append(customer.getArea()+"\n");
        stringBuilder.append(DateTimeUtils.currentTimeToDisplay()+" "+DateTimeUtils.currentDateToDisplay()+"\n");

        return  stringBuilder.toString();
    }

    private String generateSalesString(List<OrderItemEntity> products,
                                  String currencySymbol){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n"+resources.getString(R.string.current_sale)+"\n");
        stringBuilder.append(separator +"\n");
        stringBuilder.append(itemsHeader);
        for(OrderItemEntity orderItemEntity : products){
            cashSalesTotal += orderItemEntity.getQuantity() * orderItemEntity.getPrice();
            String productName = getProductName(orderItemEntity.getProduct().getName());
            stringBuilder.append(productName +
                    String.format("%-15s", orderItemEntity.getQuantity())+"   "+
                    String.format("%-10s", currencySymbol+String.valueOf(
                            orderItemEntity.getQuantity() * orderItemEntity.getPrice()))+"\n");
            stringBuilder.append("("+orderItemEntity.getProduct().getWeight() +
                    orderItemEntity.getProduct().getWeightUnit()+")"+"\n");
        }
        return stringBuilder.toString();
    }

    private String generateReturnsString(List<ReturnOrderItem> products, String currencySymbol){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n"+resources.getString(R.string.returns)+"\n");
        stringBuilder.append(separator +"\n");
        stringBuilder.append(itemsHeader);
        for(ReturnOrderItem orderItemEntity : products){
            returnsTotal += orderItemEntity.getQuantity() * orderItemEntity.getPrice();
            stringBuilder.append(getProductName(orderItemEntity.getProduct().getName()) +
                    String.format("%-15s", orderItemEntity.getQuantity())+"   "+
                    String.format("%-10s", currencySymbol+String.valueOf(
                            orderItemEntity.getQuantity() * orderItemEntity.getPrice()))+"\n");
            stringBuilder.append("("+orderItemEntity.getProduct().getWeight() +
                    orderItemEntity.getProduct().getWeightUnit()+")"+"\n");
        }
        return stringBuilder.toString();
    }

    private String generateReturnProductsString(List<Product> returnedProducts, String currencySymbol){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n"+resources.getString(R.string.returns)+"\n");
        stringBuilder.append(separator +"\n");
        stringBuilder.append(itemsHeader);
        for(Product product : returnedProducts){
            if(product.getReturnQuantity() > 0) {
                returnsTotal += product.getReturnQuantity() * product.getPrice();
                stringBuilder.append(getProductName(product.getProductName())+
                        String.format("%-15s", product.getReturnQuantity()) + "   " +
                        String.format("%-10s", currencySymbol + String.valueOf(
                                product.getReturnQuantity() * product.getPrice())) + "\n");
                stringBuilder.append("(" + product.getWeight() + product.getWeightUnit() + ")" + "\n");
            }

        }
        return stringBuilder.toString();
    }

    private String generateCashSalesItems(List<Product> products, String currencySymbol) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n"+resources.getString(R.string.current_sale)+"\n");
        stringBuilder.append(separator +"\n");
        stringBuilder.append(itemsHeader);
        for(Product product : products){
            if(product.getQuantity() > 0) {
                cashSalesTotal += product.getQuantity() * product.getPrice();
                stringBuilder.append(getProductName(product.getProductName())+
                        String.format("%-15s", product.getQuantity()) + "   " +
                        String.format("%-10s", currencySymbol + String.valueOf(
                                product.getQuantity() * product.getPrice())) + "\n");
                stringBuilder.append("(" + product.getWeight() + product.getWeightUnit() + ")" + "\n");
            }
        }
        if(cashSalesTotal >0 )
            return stringBuilder.toString();
        else return "";
    }

    private String getProductName(String productName){
        if(productName.length() < 10)
        return  String.format("%-40s", productName);
        else if(productName.length() < 15)
            return  String.format("%-30s", productName);
        else return  String.format("%-22s", trim(productName, 18));
    }




    public List<PrintableLine> generateCashSalesPrintableLines(String doNumber, String roNumber,
                                                               Customer customer, List<Product> products,
                                                               String currencySymbol, double paymentCollected,
                                                               Context context){
        List<PrintableLine> printableLines = new ArrayList<>();
        initResources(context);
        printableLines.add(new PrintableLine(customer.getName(), Layout.Alignment.ALIGN_NORMAL, boldTextPaint));
        return printableLines;
    }









    public List<PrintableLine> generateReturnOrderPrintableLines(ReturnOrderEntity returnOrderEntity,
                                                                 List<ReturnOrderItem> products,
                                                                 String currencySymbol, Context context){
        List<PrintableLine> printableLines = new ArrayList<>();
        initResources(context);

        printableLines.add(new PrintableLine(returnOrderEntity.getCustomerName(),Layout.Alignment.ALIGN_NORMAL, boldTextPaint));
        printableLines.add(new PrintableLine(resources.getString(R.string.ro_number)+" "+
                returnOrderEntity.getRoNumber(), Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
        String address = StringUtils.getAddress(returnOrderEntity.getSourceAddressLine1(),
                returnOrderEntity.getSourceAddressLine2());

        printableLines.add(new PrintableLine(address, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
        String dateTime;
        dateTime = DateTimeUtils.convertdDate(returnOrderEntity.getActualReturnAt(),
                    RAILS_TIMESTAMP_FORMAT, ORDER_DISPLAY_DATE_FORMAT)+" "+
                    DateTimeUtils.convertdDate(returnOrderEntity.getActualReturnAt(),
                            RAILS_TIMESTAMP_FORMAT, TWELVE_HOUR_TIME_FORMAT);
        printableLines.add(new PrintableLine(dateTime, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));

        printableLines.add(new PrintableLine(resources.getString(R.string.returns), Layout.Alignment.ALIGN_OPPOSITE, normalTextPaint));
        printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
        printableLines.add(new PrintableLine(itemsHeader, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
        printableLines.addAll(generateReturnsPrintableLines(products, currencySymbol, normalTextPaint));


        return printableLines;
    }

    private List<PrintableLine> generateReturnsPrintableLines(List<ReturnOrderItem> products, String currencySymbol, TextPaint textPaint){
        List<PrintableLine> printableLines = new ArrayList<>();
        for(ReturnOrderItem orderItemEntity : products){
            returnsTotal += orderItemEntity.getQuantity() * orderItemEntity.getPrice();
            printableLines.add(new PrintableLine(getProductName(orderItemEntity.getProduct().getName()) +
                    String.format("%-15s", orderItemEntity.getQuantity())+"   "+
                    String.format("%-10s", currencySymbol+String.valueOf(
                            orderItemEntity.getQuantity() * orderItemEntity.getPrice())), Layout.Alignment.ALIGN_NORMAL, textPaint));
            printableLines.add(new PrintableLine("("+orderItemEntity.getProduct().getWeight() +
                    orderItemEntity.getProduct().getWeightUnit()+")", Layout.Alignment.ALIGN_NORMAL, textPaint));
        }
        return printableLines;
    }









    public List<PrintableLine> generateDeliveryOrderPrintableLines(DeliveryOrderEntity deliveryOrder,
                                                                   List<OrderItemEntity> doItems, String currencySymbol,
                                                                   double paymentCollected, Context context, boolean fromHistory) {
        List<PrintableLine> printableLines = new ArrayList<>();
        initResources(context);
        printableLines.add(new PrintableLine(deliveryOrder.getCustomerName(),Layout.Alignment.ALIGN_NORMAL, boldTextPaint));
        printableLines.add(new PrintableLine(resources.getString(R.string.do_number)+" "+
                deliveryOrder.getDoNumber(), Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
        String address = StringUtils.getAddress(deliveryOrder.getDestinationAddressLine1(),
                deliveryOrder.getDestinationAddressLine2());
        if(deliveryOrder.getCity() != null)
            address += deliveryOrder.getCity();
        if(deliveryOrder.getState() != null)
            address += deliveryOrder.getState();
        if(deliveryOrder.getPincode() != null)
            address += deliveryOrder.getPincode();
        printableLines.add(new PrintableLine(address, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));

        String dateTime;
        if(deliveryOrder.getActualDeliveryDate() == null)
            dateTime = DateTimeUtils.currentTimeToDisplay()+" "+DateTimeUtils.currentDateToDisplay();
        else
            dateTime = DateTimeUtils.convertdDate(deliveryOrder.getActualDeliveryDate(),
                    RAILS_TIMESTAMP_FORMAT, ORDER_DISPLAY_DATE_FORMAT)+" "+
                    DateTimeUtils.convertdDate(deliveryOrder.getActualDeliveryDate(),
                            RAILS_TIMESTAMP_FORMAT, TWELVE_HOUR_TIME_FORMAT);

        printableLines.add(new PrintableLine(dateTime, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
        printableLines.add(new PrintableLine(resources.getString(R.string.current_sale), Layout.Alignment.ALIGN_OPPOSITE, normalTextPaint));
        printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
        printableLines.add(new PrintableLine(itemsHeader, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
        printableLines.addAll(generateCashSalesString(doItems, currencySymbol, normalTextPaint));
        printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
        printableLines.add(new PrintableLine(resources.getString(R.string.total)+" "+cashSalesTotal+"       ",
                Layout.Alignment.ALIGN_OPPOSITE, boldTextPaint));
        printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
        printableLines.add(new PrintableLine(resources.getString(R.string.grand_total)+" "+(cashSalesTotal-returnsTotal),
                Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
        printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
        if(!fromHistory) {
            printableLines.add(new PrintableLine(resources.getString(R.string.payment_collected) + " "
                    + paymentCollected, Layout.Alignment.ALIGN_NORMAL, boldTextPaint));
            printableLines.add(new PrintableLine(separator + "\n\n", Layout.Alignment.ALIGN_CENTER, normalTextPaint));
        } else {
            printableLines.add(new PrintableLine("\n\n", Layout.Alignment.ALIGN_CENTER, normalTextPaint));
        }

        return printableLines;
    }

    private List<PrintableLine> generateCashSalesString(List<OrderItemEntity> products, String currencySymbol, TextPaint textPaint) {
        List<PrintableLine> printableLines = new ArrayList<>();
        for(OrderItemEntity orderItemEntity : products){
            cashSalesTotal += orderItemEntity.getQuantity() * orderItemEntity.getPrice();
            String productName = getProductName(orderItemEntity.getProduct().getName());
            printableLines.add(new PrintableLine("\n"+productName +
                    String.format("%-15s", orderItemEntity.getQuantity())+"   "+
                    String.format("%-10s", currencySymbol+String.valueOf(
                            orderItemEntity.getQuantity() * orderItemEntity.getPrice())),
                    Layout.Alignment.ALIGN_NORMAL, textPaint));
            printableLines.add(new PrintableLine("("+orderItemEntity.getProduct().getWeight() +
                    orderItemEntity.getProduct().getWeightUnit()+")", Layout.Alignment.ALIGN_NORMAL, textPaint));
        }
        return printableLines;
    }

    private void initResources(Context context) {
        resources = context.getResources();
        itemsHeader =  String.format("%-35s", resources.getString(R.string.items_label)+" ")+"   "+
                String.format("%-15s", resources.getString(R.string.units)+" ")+"   "+
                String.format("%-10s", resources.getString(R.string.value)+" ");
        boldTextPaint = new TextPaint();
        boldTextPaint.setColor(Color.BLACK);
        boldTextPaint.setTextSize(18);
        boldTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        normalTextPaint = new TextPaint();
        normalTextPaint.setColor(Color.BLACK);
        normalTextPaint.setTextSize(18);
        normalTextPaint.setTypeface(Typeface.DEFAULT);
    }



    public void print(List<PrintableLine> printableLines, BluetoothPrinter bluetoothPrinter, Context context) {
        initPrinter(bluetoothPrinter, context);
        printer.addText("\n");
        for(PrintableLine printableLine : printableLines)
            printer.addText(printableLine.getText(), printableLine.getAlignment(), printableLine.getTextPaint());
        printer.print();
    }
}
