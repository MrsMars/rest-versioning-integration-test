package com.aoher.controller;

import com.aoher.version.ValidVersion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {

    private static final String SAY_HELLO_WORLD_URL_PATH = "/apiurl/%s/hello";
    private static final String SAY_HELLO_WORLD_HEADER_PATH = "/apiheader/hello";
    private static final String SAY_HELLO_WORLD_ACCEPT_PATH = "/apiaccept/hello";

    private static final String JSON_PATH_HELLO = "$.hello";
    private static final String JSON_PATH_HELLO_VALUE = "world";

    private static final String API_VERSION = "X-API-Version";
    private static final String UNSUPPORTED_VERSION = "V13";

    private static final String APPLICATION_ACCEPT_VND_AND_JSON = "application/vnd.company.app-%s+json";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_respond_with_hello_json_on_v1_url() throws Exception {
        mockMvc.perform(
                get(format(SAY_HELLO_WORLD_URL_PATH, ValidVersion.V1))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_HELLO, is(JSON_PATH_HELLO_VALUE)));
    }

    @Test
    public void should_respond_with_hello_json_on_v2_url() throws Exception {
        mockMvc.perform(
                get(format(SAY_HELLO_WORLD_URL_PATH, ValidVersion.V2))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_HELLO, is(JSON_PATH_HELLO_VALUE)));
    }

    @Test
    public void should_respond_with_400_on_v3_url() throws Exception {
        mockMvc.perform(
                get(format(SAY_HELLO_WORLD_URL_PATH, UNSUPPORTED_VERSION))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /* ------------------------------------------------------------------- */

    @Test
    public void should_respond_with_hello_json_with_v1_header() throws Exception {
        mockMvc.perform(
                get(SAY_HELLO_WORLD_HEADER_PATH)
                        .accept(APPLICATION_JSON)
                        .header(API_VERSION, ValidVersion.V1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_HELLO, is(JSON_PATH_HELLO_VALUE)));
    }

    @Test
    public void should_respond_with_hello_json_with_v2_header() throws Exception {
        mockMvc.perform(
                get(SAY_HELLO_WORLD_HEADER_PATH)
                        .accept(APPLICATION_JSON)
                        .header(API_VERSION, ValidVersion.V2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath(JSON_PATH_HELLO, is(JSON_PATH_HELLO_VALUE)));
    }

    @Test
    public void should_respond_with_bad_request_with_v3_header() throws Exception {
        mockMvc.perform(
                get(SAY_HELLO_WORLD_HEADER_PATH)
                        .accept(APPLICATION_JSON)
                        .header(API_VERSION, UNSUPPORTED_VERSION))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_respond_with_bad_request_without_version_header() throws Exception {
        mockMvc.perform(
                get(SAY_HELLO_WORLD_HEADER_PATH)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /* ------------------------------------------------------------------- */

    @Test
    public void should_respond_with_hello_json_with_v1_accept() throws Exception {
        mockMvc.perform(
                get(SAY_HELLO_WORLD_ACCEPT_PATH)
                        .accept(format(APPLICATION_ACCEPT_VND_AND_JSON, ValidVersion.V1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(format(APPLICATION_ACCEPT_VND_AND_JSON, ValidVersion.V1)))
                .andExpect(jsonPath(JSON_PATH_HELLO, is(JSON_PATH_HELLO_VALUE)));
    }

    @Test
    public void should_respond_with_hello_json_with_v2_accept() throws Exception {
        mockMvc.perform(
                get(SAY_HELLO_WORLD_ACCEPT_PATH)
                        .accept(format(APPLICATION_ACCEPT_VND_AND_JSON, ValidVersion.V2)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(format(APPLICATION_ACCEPT_VND_AND_JSON, ValidVersion.V2)))
                .andExpect(jsonPath(JSON_PATH_HELLO, is(JSON_PATH_HELLO_VALUE)));
    }

    @Test
    public void should_respond_with_not_acceptable_with_v3_accept() throws Exception {
        mockMvc.perform(
                get(SAY_HELLO_WORLD_ACCEPT_PATH)
                        .accept(format(APPLICATION_ACCEPT_VND_AND_JSON, UNSUPPORTED_VERSION)))
                .andExpect(status().isNotAcceptable());
    }
}