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
import com.goleep.driverapp.leep.pickup.returns.ReturnsFinalConfirmationActivity;
import com.goleep.driverapp.services.printer.PrinterService;
import com.goleep.driverapp.services.room.entities.DeliveryOrderEntity;
import com.goleep.driverapp.services.room.entities.OrderItemEntity;
import com.goleep.driverapp.services.room.entities.ReturnOrderEntity;
import com.goleep.driverapp.utils.DateTimeUtils;
import com.goleep.driverapp.utils.StringUtils;
import com.ngx.BluetoothPrinter;
import com.ngx.PrinterWidth;

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
    private Context context;

    public PrinterHelper(Context context) {
        this.context = context;
        printer = PrinterService.sharedInstance().getPrinter();
        printer.initService(context);
        initPrinter();
        initResources();
    }

    public BluetoothPrinter getPrinter() {
        return printer;
    }

    private void initPrinter(){
        printer.setPrinterWidth(PrinterWidth.PRINT_WIDTH_48MM);
        printer.addText("\n");
        resources = context.getResources();

    }

    private String trim(String string, int maxLength) {
        if(string.length() <= maxLength)
            return string;
        return string.substring(0, maxLength);
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
                                                               String currencySymbol, double paymentCollected){
        List<PrintableLine> printableLines = new ArrayList<>();
        printableLines.add(new PrintableLine(customer.getName(), Layout.Alignment.ALIGN_NORMAL, boldTextPaint));
        if(doNumber != null && !doNumber.isEmpty())
            printableLines.add(new PrintableLine(resources.getString(R.string.do_number)+" "+
                    doNumber, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
        if(roNumber != null && !roNumber.isEmpty())
            printableLines.add(new PrintableLine(resources.getString(R.string.ro_number)+" "+
                    roNumber, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));

        printableLines.add(new PrintableLine(customer.getArea(), Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
        String dateTime = DateTimeUtils.currentTimeToDisplay()+" "+DateTimeUtils.currentDateToDisplay();
        printableLines.add(new PrintableLine(dateTime, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));

        List<PrintableLine> cashSalesPrintableLines = generateSalePrintable(products, currencySymbol, normalTextPaint);
        if(cashSalesPrintableLines.size() > 0){
            printableLines.add(new PrintableLine(resources.getString(R.string.current_sale),
                    Layout.Alignment.ALIGN_OPPOSITE, normalTextPaint));
            printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
            printableLines.add(new PrintableLine(itemsHeader, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
            printableLines.addAll(cashSalesPrintableLines);
            printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
            printableLines.add(new PrintableLine(resources.getString(R.string.total)+" "+cashSalesTotal+"       ",
                    Layout.Alignment.ALIGN_OPPOSITE, boldTextPaint));
            printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
        }

        List<PrintableLine> returnsPrintableLines = generateReturnPrintable(products, currencySymbol, normalTextPaint);
        if(returnsPrintableLines.size() >0){
            printableLines.add(new PrintableLine(resources.getString(R.string.returns),
                    Layout.Alignment.ALIGN_OPPOSITE, normalTextPaint));
            printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
            printableLines.add(new PrintableLine(itemsHeader, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
            printableLines.addAll(returnsPrintableLines);
            printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
            printableLines.add(new PrintableLine(resources.getString(R.string.total)+" "+returnsTotal+"       ",
                    Layout.Alignment.ALIGN_OPPOSITE, boldTextPaint));
            printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
        }

        printableLines.add(new PrintableLine(resources.getString(R.string.grand_total)+" "+(cashSalesTotal-returnsTotal),
                Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
        printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
        printableLines.add(new PrintableLine(resources.getString(R.string.payment_collected) + " "
                + paymentCollected, Layout.Alignment.ALIGN_NORMAL, boldTextPaint));
        printableLines.add(new PrintableLine(separator + "\n\n", Layout.Alignment.ALIGN_CENTER, normalTextPaint));

        return printableLines;
    }

    private List<PrintableLine> generateSalePrintable(List<Product> products,
                                                      String currencySymbol, TextPaint textPaint) {
        List<PrintableLine> printableLines = new ArrayList<>();
        for(Product product : products){
            if(product.getQuantity() > 0) {
                cashSalesTotal += product.getQuantity() * product.getPrice();
                String productName = getProductName(product.getProductName());
                printableLines.add(new PrintableLine("\n" + productName +
                        String.format("%-15s", product.getQuantity()) +
                        String.format("%-10s", currencySymbol + String.valueOf(
                                product.getQuantity() * product.getPrice())),
                        Layout.Alignment.ALIGN_NORMAL, textPaint));
                printableLines.add(new PrintableLine("(" + product.getWeight() +
                        product.getWeightUnit() + ")", Layout.Alignment.ALIGN_NORMAL, textPaint));
            }
        }
        return printableLines;
    }

    private List<PrintableLine> generateReturnPrintable(List<Product> products,
                                                      String currencySymbol, TextPaint textPaint) {
        List<PrintableLine> printableLines = new ArrayList<>();
        for(Product product : products){
            if(product.getReturnQuantity() > 0) {
                returnsTotal += product.getReturnQuantity() * product.getPrice();
                String productName = getProductName(product.getProductName());
                printableLines.add(new PrintableLine("\n" + productName +
                        String.format("%-15s", product.getReturnQuantity()) +
                        String.format("%-10s", currencySymbol + String.valueOf(
                                product.getReturnQuantity() * product.getPrice())),
                        Layout.Alignment.ALIGN_NORMAL, textPaint));
                printableLines.add(new PrintableLine("(" + product.getWeight() +
                        product.getWeightUnit() + ")", Layout.Alignment.ALIGN_NORMAL, textPaint));
            }
        }
        return printableLines;
    }

    public List<PrintableLine> generateReturnOrderPrintableLines(ReturnOrderEntity returnOrderEntity,
                                                                 List<ReturnOrderItem> products,
                                                                 String currencySymbol){
        List<PrintableLine> printableLines = new ArrayList<>();
        String type = returnOrderEntity.getType();
        String locationName = "";
        String address = "";
        String dateTime = null;
        switch (type){
            case "driver":
                locationName = returnOrderEntity.getDestinationLocationName();
                dateTime =  DateTimeUtils.convertdDate(returnOrderEntity.getActualReturnAt(),
                        RAILS_TIMESTAMP_FORMAT, ORDER_DISPLAY_DATE_FORMAT)+" "+
                        DateTimeUtils.convertdDate(returnOrderEntity.getActualReturnAt(),
                                RAILS_TIMESTAMP_FORMAT, TWELVE_HOUR_TIME_FORMAT);
                address = StringUtils.getAddress(returnOrderEntity.getDestinationAddressLine1(), returnOrderEntity.getDestinationAddressLine2());
                break;
            case "customer":
                locationName = returnOrderEntity.getSourceLocationName();
                dateTime =  DateTimeUtils.convertdDate(returnOrderEntity.getActualAcceptedAt(),
                        RAILS_TIMESTAMP_FORMAT, ORDER_DISPLAY_DATE_FORMAT)+" "+
                        DateTimeUtils.convertdDate(returnOrderEntity.getActualAcceptedAt(),
                                RAILS_TIMESTAMP_FORMAT, TWELVE_HOUR_TIME_FORMAT);
                address = StringUtils.getAddress(returnOrderEntity.getSourceAddressLine1(), returnOrderEntity.getSourceAddressLine2());
                break;
        }


        printableLines.add(new PrintableLine(locationName,Layout.Alignment.ALIGN_NORMAL, boldTextPaint));
        printableLines.add(new PrintableLine(resources.getString(R.string.ro_number)+" "+
                returnOrderEntity.getRoNumber(), Layout.Alignment.ALIGN_NORMAL, normalTextPaint));


        printableLines.add(new PrintableLine(address, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));

        printableLines.add(new PrintableLine(dateTime, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));

        printableLines.add(new PrintableLine(resources.getString(R.string.returns), Layout.Alignment.ALIGN_OPPOSITE, normalTextPaint));
        printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
        printableLines.add(new PrintableLine(itemsHeader, Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
        printableLines.addAll(generateReturnsPrintableLines(products, currencySymbol, normalTextPaint));
        printableLines.add(new PrintableLine(separator, Layout.Alignment.ALIGN_CENTER, normalTextPaint));
        printableLines.add(new PrintableLine(resources.getString(R.string.total)+" "+returnsTotal+"       ",
                Layout.Alignment.ALIGN_OPPOSITE, boldTextPaint));
        printableLines.add(new PrintableLine("\n\n", Layout.Alignment.ALIGN_NORMAL, normalTextPaint));
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
                                                                   double paymentCollected, boolean fromHistory) {
        List<PrintableLine> printableLines = new ArrayList<>();
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
        if(!fromHistory)
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
        printableLines.addAll(generateCashSalesPrintableItems(doItems, currencySymbol, normalTextPaint));
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

    private List<PrintableLine> generateCashSalesPrintableItems(List<OrderItemEntity> products,
                                                                String currencySymbol, TextPaint textPaint) {
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

    private void initResources() {
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

    public void print(List<PrintableLine> printableLines) {
        printer.addText("\n");
        for(PrintableLine printableLine : printableLines)
            printer.addText(printableLine.getText(), printableLine.getAlignment(), printableLine.getTextPaint());
        printer.print();
    }
}
