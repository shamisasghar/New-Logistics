package com.hypernymbiz.logistics.utils;


/**
 * Created by SOBAN on 03-Mar-16.
 */
public class PriceCalculator {

//    public static double getPrice(Cart item) {
////        if(item.is_available){
////            return 0;
////        }
//        double totalPrice = 0;
//        double variantPrice = 0;
//        double optionsPrice = 0;
//        for (Variant variant : item.product.variants) {
//            if (variant.id == item.selectedVariantId) {
//                variantPrice = variant.price;
//            }
//        }
//        totalPrice = variantPrice * item.quantity;
//        for (int i = 0; i < item.selectedOptions.size(); i++) {
//            if (item.selectedOptions.get(i).isChecked) {
//                optionsPrice += item.product.options.get(i).price;
//            }
//        }
//        totalPrice += optionsPrice * item.quantity;
//        return totalPrice;
//    }
//
//    public static double getItemPriceIncludingOptions(Cart item) {
////        if(item.is_available){
////            return 0;
////        }
//        double totalPrice = 0;
//        double variantPrice = 0;
//        double optionsPrice = 0;
//        for (Variant variant : item.product.variants) {
//            if (variant.id == item.selectedVariantId) {
//                variantPrice = variant.price;
//            }
//        }
//        totalPrice = variantPrice;
//        for (int i = 0; i < item.selectedOptions.size(); i++) {
//            if (item.selectedOptions.get(i).isChecked) {
//                optionsPrice += item.product.options.get(i).price;
//            }
//        }
//        totalPrice += optionsPrice;
//        return totalPrice;
//    }
//
//    public static double getItemPrice(Cart item) {
////        if(item.is_available){
////            return 0;
////        }
//        double variantPrice = 0;
//        for (Variant variant : item.product.variants) {
//            if (variant.id == item.selectedVariantId) {
//                variantPrice = variant.price;
//            }
//        }
//        return variantPrice;
//    }
//
//    public static double getTotalPayable(List<Cart> items) {
//        double totalPayable = 0;
//        for (Cart item : items) {
//            totalPayable += getPrice(item);
//        }
//        return totalPayable;
//    }
}
