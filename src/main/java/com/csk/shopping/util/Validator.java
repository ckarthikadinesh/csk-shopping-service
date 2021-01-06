package com.csk.shopping.util;

import com.csk.shopping.model.Address;
import com.csk.shopping.model.User;

public class Validator {
    public static boolean isValidEmail(String input) {
        if (input != null && input != "") {
            if (input.matches("^[a-zA-Z0-9._]*@[a-zA-Z0-9.-]*$")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUserEmpty(User user) {
        if (user.getAge() == 0) {
            return true;
        }
        if (user.getPassword() == null || user.getPassword() == "") {
            return true;
        }
        if (user.getEmail() == null || user.getEmail() == "") {
            return true;
        }
        if (user.getUsername() == null || user.getUsername() == "") {
            return true;
        }
        return false;
    }

    public static boolean isAddressEmpty(Address address) {
        if (address.getAddress() == null || address.getAddress() == "") {
            return true;
        }
        if (address.getCity() == null || address.getCity() == "") {
            return true;
        }
        if (address.getState() == null || address.getState() == "") {
            return true;
        }
        if (address.getCountry() == null || address.getCountry() == "") {
            return true;
        }
        if (address.getPhonenumber() == null || address.getPhonenumber() == "") {
            return true;
        }
        if (address.getZipcode() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isStringEmpty(String input) {
        if (input == null || input.equals("")) {
            return true;
        }
        return false;
    }
}
