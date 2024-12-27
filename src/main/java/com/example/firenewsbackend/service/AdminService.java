package com.example.firenewsbackend.service;

import com.example.firenewsbackend.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;


}
