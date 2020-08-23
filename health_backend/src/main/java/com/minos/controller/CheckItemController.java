package com.minos.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import minos.constant.MessageConstant;
import minos.entity.Result;
import minos.pojo.CheckItem;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.CheckItemService;

/**
 * 检查项管理
 */
@RestController  //相当于 @Controller + @ResponseBody
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference //查找服务 包：com.alibaba.dubbo.config.annotation.Reference
    private CheckItemService checkItemService;

    //新增检查项
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }
}























