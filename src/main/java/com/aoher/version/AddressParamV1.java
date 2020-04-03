package com.aoher.version;

import javax.validation.constraints.Pattern;

import static com.aoher.util.Constants.ADDRESS_PATTERN;

public class AddressParamV1 {

    private String address;

    @Pattern(regexp = ADDRESS_PATTERN)
    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }
}
