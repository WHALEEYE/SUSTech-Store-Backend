package tech.whaleeye.frontcontroller.controllers;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.whaleeye.service.SecondHandOrderService;

@Api("Second Hand Order Controller")
@RestController
@RequestMapping("/secondHandOrder")
public class SecondHandOrderController {

    @Autowired
    SecondHandOrderService secondHandOrderService;



}
