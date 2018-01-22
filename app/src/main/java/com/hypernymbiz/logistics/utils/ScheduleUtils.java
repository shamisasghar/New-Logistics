package com.hypernymbiz.logistics.utils;

import android.content.Context;

public class ScheduleUtils {

//    public static void saveCart(Context context, List<Cart> items) {
//        if (items != null) {
//            PrefUtils.persistString(context, Constants.CART, GsonUtils.toJson(items));
//        }
//    }
//
//    public static List<Cart> getCart(Context context) {
//        String json = PrefUtils.getString(context, Constants.CART);
//        if (TextUtils.isEmpty(json)) {
//            return null;
//        }
//        Type listType = new TypeToken<ArrayList<Cart>>() {
//        }.getType();
//        return new Gson().fromJson(PrefUtils.getString(context, Constants.CART), listType);
//    }
//
//    public static void removeCart(Context context) {
//        PrefUtils.remove(context, Constants.CART);
//    }
//
//
//    public static void saveOrders(Context context, List<Order> items) {
//        if (items != null) {
//            PrefUtils.persistString(context, Constants.ORDERS, GsonUtils.toJson(items));
//        }
//    }
//
//    public static List<Order> getOrders(Context context) {
//        String json = PrefUtils.getString(context, Constants.ORDERS);
//        if (TextUtils.isEmpty(json)) {
//            return new ArrayList<>();
//        }
//        return new Gson().fromJson(PrefUtils.getString(context, Constants.ORDERS), new TypeToken<List<Order>>() {
//        }.getType());
//    }
//
//    public static void removeOrders(Context context) {
//        PrefUtils.remove(context, Constants.ORDERS);
//    }
//

    public static void saveUserOneSignalId(Context context, String id) {
        PrefUtils.persistString(context, Constants.USER_ONE_SIGNAL_ID, id);
    }

    public static String getUserOneSignalId(Context context) {
        return PrefUtils.getString(context, Constants.USER_ONE_SIGNAL_ID);
    }

//    public static void saveUserLastLocation(Context context, LastLocation lastLocation) {
//        if (lastLocation != null) {
//            PrefUtils.persistString(context, Constants.LAST_LOCATION, GsonUtils.toJson(lastLocation));
//        }
//    }
//
//    public static LastLocation getUserLastLocation(Context context) {
////        String json = PrefUtils.getString(context, Constants.LAST_LOCATION);
////        if (TextUtils.isEmpty(json)) {
////            return null;
////        }
//        return new Gson().fromJson(PrefUtils.getString(context, Constants.LAST_LOCATION), LastLocation.class);
//    }
//
//    public static String getPaymentGateway(Context context) {
//        return ConfigFileHelper.getConfigValue(context, Constants.PAYMENT_GATEWAY);
//    }
//
//    public static void saveBranch(Context context, Branch branch) {
//        if (branch != null) {
//            PrefUtils.persistString(context, Constants.BRANCH, GsonUtils.toJson(branch));
//        }
//    }
//
//    public static Branch getBranch(Context context) {
//        String json = PrefUtils.getString(context, Constants.BRANCH);
//        if (TextUtils.isEmpty(json)) {
//            return null;
//        }
//        return new Gson().fromJson(PrefUtils.getString(context, Constants.BRANCH), Branch.class);
//    }
//
//    public static void saveBranches(Context context, List<Branch> items) {
//        if (items != null) {
//            PrefUtils.persistString(context, Constants.BRANCHES, GsonUtils.toJson(items));
//        }
//    }
//
//    public static List<Branch> getBranches(Context context) {
//        String json = PrefUtils.getString(context, Constants.BRANCHES);
//        if (TextUtils.isEmpty(json)) {
//            return new ArrayList<>();
//        }
//        return new Gson().fromJson(PrefUtils.getString(context, Constants.BRANCHES), new TypeToken<List<Branch>>() {
//        }.getType());
//    }

    public static void persistAuthToken(Context context, String token) {
        if (context != null && token != null) {
            PrefUtils.persistString(context, Constants.AUTH_TOKEN, Constants.AUTH_TOKEN + " " + token);
        }
    }

    public static String getAuthToken(Context context) {
        String token = PrefUtils.getString(context, Constants.AUTH_TOKEN);
        return (token == null) ? null : token;
    }

    public static void removeAuthToken(Context context) {
        PrefUtils.remove(context, Constants.AUTH_TOKEN);
    }

    public static boolean isCacheCleared(Context context) {
        return PrefUtils.getBoolean(context, Constants.CLEAR_CACHE, false);
    }

    public static void cacheCleared(Context context) {
        PrefUtils.persistBoolean(context, Constants.CLEAR_CACHE, true);
    }

//    public static int getShopId(Context context) {
//        return Integer.parseInt(ConfigFileHelper.getConfigValue(context, Constants.SHOP_ID));
//    }
//
//    public static Priority getImagePriority(int index, int length) {
//        int value = 0;
//        try {
//            value = index / (length / 3);
//        } catch (Exception e) {
//
//        }
//
//        switch (value) {
//            case 0:
//                return Priority.IMMEDIATE;
//            case 1:
//                return Priority.HIGH;
//            case 2:
//                return Priority.NORMAL;
//            case 3:
//                return Priority.LOW;
//        }
//        return Priority.LOW;
//    }
}
