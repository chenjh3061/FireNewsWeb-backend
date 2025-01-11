package com.example.firenewsbackend.controller;

import com.example.firenewsbackend.common.BaseResponse;
import com.example.firenewsbackend.common.ResultUtils;
import com.example.firenewsbackend.mapper.CategoriesMapper;
import com.example.firenewsbackend.model.entity.Categories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

    @Resource
    private CategoriesMapper  categoriesMapper;

    @RequestMapping("/getAllCategories")
    public BaseResponse<List<Categories>> getAllCategories(){
        return ResultUtils.success(categoriesMapper.selectList(null));
    }

    @RequestMapping("/addCategory")
    public BaseResponse<Categories> addCategory(Categories categories){
        categoriesMapper.insert(categories);
        return ResultUtils.success(categories);
    }

    @RequestMapping("/updateCategory")
    public BaseResponse<Categories> updateCategory(Categories categories){
        categoriesMapper.updateById(categories);
        return ResultUtils.success(categories);
    }

    @RequestMapping("/deleteCategory")
    public BaseResponse<Categories> deleteCategory(Integer id){
        categoriesMapper.deleteById(id);
        return ResultUtils.success(null);
    }
}
