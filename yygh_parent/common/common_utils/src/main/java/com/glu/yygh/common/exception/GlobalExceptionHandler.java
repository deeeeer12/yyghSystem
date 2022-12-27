package com.glu.yygh.common.exception;

import com.glu.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody//因为咱都是RestController，添加这个注解可以以json的形式输出
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(YyghException.class)
    @ResponseBody//因为咱都是RestController，添加这个注解可以以json的形式输出
    public Result error(YyghException e){
        e.printStackTrace();
        return Result.fail();
    }

}
