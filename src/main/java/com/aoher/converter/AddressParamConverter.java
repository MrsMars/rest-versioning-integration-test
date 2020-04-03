package com.aoher.converter;

import com.aoher.model.Address;
import com.aoher.version.AddressParamV1;
import com.aoher.version.AddressParamV2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.aoher.util.Constants.ADDRESS_PATTERN;

public class AddressParamConverter {

    public static AddressParamV1 convertToV1(final Address address) {
        AddressParamV1 addressParamV1 = new AddressParamV1();
        addressParamV1.setAddress(address.getZip() + ' ' + address.getTown());
        return addressParamV1;
    }

    public static AddressParamV2 convertToV2(final Address address) {
        AddressParamV2 addressParamV2 = new AddressParamV2();
        addressParamV2.setZip(address.getZip());
        addressParamV2.setTown(address.getTown());
        return addressParamV2;
    }

    public static Address convertFromV1(final  AddressParamV1 addressParamV1) {
        final String zip;
        final String town;

        Pattern pattern = Pattern.compile(ADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(addressParamV1.getAddress());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("unparsable address " + addressParamV1.getAddress());
        }

        zip = matcher.group(1);
        town = matcher.group(2);
        return new Address(zip, town);
    }

    public static Address convertFromV2(final AddressParamV2 addressParamV2) {
        return new Address(addressParamV2.getZip(), addressParamV2.getTown());
    }

    private AddressParamConverter() {
    }
}
