package com.aoher.controller;

import static org.hamcrest.Matchers.is;

import com.aoher.model.Address;
import com.aoher.service.AddressService;
import com.aoher.version.ValidVersion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AddressIT {

    private static final String GET_ADDRESS_URL = "/apiurl/%s/address";
    private static final String GET_HEADER_URL = "/apiheader/address";
    private static final String GET_ADDRESS_ACCEPT_URL = "/apiaccept/address";

    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_ZIP = "zip";
    private static final String PARAM_TOWN = "town";

    private static final String JSON_PATH_ADDRESS = "$.address";
    private static final String JSON_PATH_ZIP = "$.zip";
    private static final String JSON_PATH_TOWN = "$.town";

    private static final String APPLICATION_ACCEPT_VND_AND_JSON = "application/vnd.company.app-%s+json";

    private static final String API_VERSION = "X-API-Version";
    private static final String UNSUPPORTED_VERSION = "V13";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressService addressService;

    private Address address;

    @Before
    public void init() {
        address = new Address("12043", "Berlin");
        addressService.save(address);
    }

    @Test
    public void should_respond_with_address_json_on_v1_url() throws Exception {
        mockMvc.perform(
                get(format(GET_ADDRESS_URL, ValidVersion.V1))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_ADDRESS, is(getAddress(address))));
    }

    @Test
    public void should_respond_with_address_json_on_v2_url() throws Exception {
        mockMvc.perform(
                get(format(GET_ADDRESS_URL, ValidVersion.V2))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_ZIP, is(address.getZip())))
                .andExpect(jsonPath(JSON_PATH_TOWN, is(address.getTown())));
    }

    @Test
    public void should_respond_with_404_on_v3_url() throws Exception {
        mockMvc.perform(
                get(format(GET_ADDRESS_URL, UNSUPPORTED_VERSION))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_respond_with_created_on_v1_url() throws Exception {
        mockMvc.perform(
                post(format(GET_ADDRESS_URL, ValidVersion.V1))
                        .param(PARAM_ADDRESS, getAddress(address)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void should_respond_with_created_on_v2_url() throws Exception {
        mockMvc.perform(
                post(format(GET_ADDRESS_URL, ValidVersion.V2))
                        .param(PARAM_ZIP, address.getZip())
                        .param(PARAM_TOWN, address.getTown()))
                .andExpect(status().isAccepted());
    }

    /* ------------------------------------------------------------------- */

    @Test
    public void should_respond_with_hello_json_with_v1_header() throws Exception {
        mockMvc.perform(
                get(GET_HEADER_URL)
                        .accept(APPLICATION_JSON)
                        .header(API_VERSION, ValidVersion.V1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_ADDRESS, is(getAddress(address))));
    }

    @Test
    public void should_respond_with_hello_json_with_v2_header() throws Exception {
        mockMvc.perform(
                get(GET_HEADER_URL)
                        .accept(APPLICATION_JSON)
                        .header(API_VERSION, ValidVersion.V2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_ZIP, is(address.getZip())))
                .andExpect(jsonPath(JSON_PATH_TOWN, is(address.getTown())));
    }

    @Test
    public void should_respond_with_bad_request_with_v3_header() throws Exception {
        mockMvc.perform(
                get(GET_HEADER_URL)
                        .accept(APPLICATION_JSON)
                        .header(API_VERSION, UNSUPPORTED_VERSION))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_respond_with_bad_request_without_version_header() throws Exception {
        mockMvc.perform(
                get(GET_HEADER_URL)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_respond_with_created_with_v1_header() throws Exception {
        mockMvc.perform(
                post(GET_HEADER_URL)
                        .header(API_VERSION, ValidVersion.V1)
                        .param(PARAM_ADDRESS, getAddress(address)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void should_respond_with_created_with_v2_header() throws Exception {
        mockMvc.perform(
                post(GET_HEADER_URL)
                        .header(API_VERSION, ValidVersion.V2)
                        .param(PARAM_ZIP, address.getZip())
                        .param(PARAM_TOWN, address.getTown()))
                .andExpect(status().isAccepted());
    }

    /* ------------------------------------------------------------------- */

    @Test
    public void should_respond_with_hello_json_with_v1_accept() throws Exception {
        mockMvc.perform(
                get(GET_ADDRESS_ACCEPT_URL)
                        .accept(format(APPLICATION_ACCEPT_VND_AND_JSON, ValidVersion.V1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(format(APPLICATION_ACCEPT_VND_AND_JSON, ValidVersion.V1)))
                .andExpect(jsonPath(JSON_PATH_ADDRESS, is(getAddress(address))));
    }

    @Test
    public void should_respond_with_hello_json_with_v2_accept() throws Exception {
        mockMvc.perform(
                get(GET_ADDRESS_ACCEPT_URL)
                        .accept(format(APPLICATION_ACCEPT_VND_AND_JSON, ValidVersion.V2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(format(APPLICATION_ACCEPT_VND_AND_JSON, ValidVersion.V2)))
                .andExpect(jsonPath(JSON_PATH_ZIP, is(address.getZip())))
                .andExpect(jsonPath(JSON_PATH_TOWN, is(address.getTown())));
    }

    @Test
    public void should_respond_with_not_acceptable_with_v3_accept() throws Exception {
        mockMvc.perform(
                get(GET_ADDRESS_ACCEPT_URL)
                        .accept(format(APPLICATION_ACCEPT_VND_AND_JSON, UNSUPPORTED_VERSION)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void should_respond_with_created_with_v1_accept() throws Exception {
        mockMvc.perform(
                post(GET_ADDRESS_ACCEPT_URL)
                        .accept(format(APPLICATION_ACCEPT_VND_AND_JSON, ValidVersion.V1))
                        .param(PARAM_ADDRESS, getAddress(address)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void should_respond_with_created_with_v2_accept() throws Exception {
        mockMvc.perform(
                post(GET_ADDRESS_ACCEPT_URL)
                        .accept(format(APPLICATION_ACCEPT_VND_AND_JSON, ValidVersion.V2))
                        .param(PARAM_ZIP, address.getZip())
                        .param(PARAM_TOWN, address.getTown()))
                .andExpect(status().isAccepted());
    }

    private String getAddress(Address address) {
        return format("%s %s", address.getZip(), address.getTown());
    }
}
