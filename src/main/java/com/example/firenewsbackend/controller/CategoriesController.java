package com.example.firenewsbackend.controller;

import com.example.firenewsbackend.common.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

    @RequestMapping("/getAllCategories")
    public BaseResponse<List<String>> getAllCategories(){
        return null;
    }
}
