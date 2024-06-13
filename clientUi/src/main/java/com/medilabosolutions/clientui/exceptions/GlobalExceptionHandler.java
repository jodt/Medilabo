package com.medilabosolutions.clientui.exceptions;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public String handleFeignException(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", "We encoured a problem");
        return "error/500";
    }

}
