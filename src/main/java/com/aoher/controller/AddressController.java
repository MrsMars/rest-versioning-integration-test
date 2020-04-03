package com.aoher.controller;

import com.aoher.model.Address;
import com.aoher.service.AddressService;
import com.aoher.version.AddressParamV1;
import com.aoher.version.AddressParamV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.aoher.converter.AddressParamConverter.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping(value = "/apiurl/V1/address", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public AddressParamV1 getAddressUrlV1() {
        Address address = addressService.load();
        return convertToV1(address);
    }

    @GetMapping(value = "/apiurl/V2/address", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public AddressParamV2 getAddressUrlV2() {
        Address address = addressService.load();
        return convertToV2(address);
    }

    @PostMapping("/apiurl/V1/address")
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public void saveAddressUrlV1(@Valid @ModelAttribute final AddressParamV1 addressParamV1) {
        Address address =convertFromV1(addressParamV1);
        addressService.save(address);
    }

    @PostMapping("/apiurl/V2/address")
    @ResponseStatus(ACCEPTED)
    public void saveAddressUrlV2(@Valid @ModelAttribute final AddressParamV2 addressParamV2) {
        Address address = convertFromV2(addressParamV2);
        addressService.save(address);
    }

    @GetMapping(
            value = "/apiheader/address",
            produces = APPLICATION_JSON_VALUE,
            headers = "X-API-Version=V1")
    @ResponseBody
    public AddressParamV1 getAddressHeaderV1() {
        Address address = addressService.load();
        return convertToV1(address);
    }

    @GetMapping(
            value = "/apiheader/address",
            produces = APPLICATION_JSON_VALUE,
            headers = "X-API-Version=V2")
    @ResponseBody
    public AddressParamV2 getAddressHeaderV2() {
        Address address = addressService.load();
        return convertToV2(address);
    }

    @PostMapping(
            value = "/apiheader/address",
            headers = "X-API-Version=V1")
    @ResponseStatus(ACCEPTED)
    public void saveAddressHeaderV1(@Valid @ModelAttribute final AddressParamV1 addressParamV1) {
        Address address = convertFromV1(addressParamV1);
        addressService.save(address);
    }

    @PostMapping(
            value = "/apiheader/address",
            headers = "X-API-Version=V2")
    @ResponseStatus(ACCEPTED)
    public void saveAddressHeaderV2(@Valid @ModelAttribute final AddressParamV2 addressParamV2) {
        Address address = convertFromV2(addressParamV2);
        addressService.save(address);
    }

    @GetMapping(
            value = "/apiaccept/address",
            produces = "application/vnd.company.app-V1+json")
    @ResponseBody
    public AddressParamV1 getAddressAcceptV1() {
        Address address = addressService.load();
        return convertToV1(address);
    }

    @GetMapping(
            value = "/apiaccept/address",
            produces = "application/vnd.company.app-V2+json")
    @ResponseBody
    public AddressParamV2 getAddressAcceptV2() {
        Address address = addressService.load();
        return convertToV2(address);
    }

    @PostMapping(
            value = "/apiaccept/address",
            headers = "Accept=application/vnd.company.app-V1+json")
    @ResponseStatus(ACCEPTED)
    public void saveAddressAcceptV1(@Valid @ModelAttribute final AddressParamV1 addressParamV1) {
        Address address = convertFromV1(addressParamV1);
        addressService.save(address);
    }

    @PostMapping(
            value = "/apiaccept/address",
            headers = "Accept=application/vnd.company.app-V2+json")
    @ResponseStatus(ACCEPTED)
    public void saveAddressAcceptV2(@ModelAttribute @Valid final AddressParamV2 addressParamV2) {
        Address address = convertFromV2(addressParamV2);
        addressService.save(address);
    }
}
