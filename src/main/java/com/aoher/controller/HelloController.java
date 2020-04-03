package com.aoher.controller;

import com.aoher.model.Hello;
import com.aoher.version.ValidVersion;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class HelloController {

    @GetMapping(
            value = "/apiurl/{version}/hello",
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Hello sayHelloWorldUrl(@PathVariable final ValidVersion version) {
        return new Hello();
    }

    @GetMapping(
            value = "/apiheader/hello",
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Hello sayHelloWorldHeader(@RequestHeader("X-API-Version") final ValidVersion validVersion) {
        return new Hello();
    }

    @GetMapping(value = "/apiaccept/hello",
            produces = {
            "application/vnd.company.app-v1+json",
            "application/vnd.company.app-v2+json"
    })
    @ResponseBody
    public Hello sayHelloWorldAccept() {
        return new Hello();
    }
}
