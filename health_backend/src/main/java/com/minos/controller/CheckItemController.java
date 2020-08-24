package com.minos.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.minos.constant.MessageConstant;
import com.minos.entity.Result;
import com.minos.pojo.CheckItem;
import com.minos.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 检查项管理
 */
@RestController // 等于 @Controller + @ResponseBody
@RequestMapping("/checkitem")
public class CheckItemController {
    // 包： com.alibaba.dubbo.config.annotation.Reference
    // @Reference 通过duboo到zookeeper服务中心查找CheckItem服务
    @Reference
    private CheckItemService checkItemService;

    //新增检查项
    @RequestMapping("/add")
    // 默认接受到的是json数据 要加@RequestBody才能解析封装
    //注意，这里传入封装的checkItem便是从前端获得数据
    public Result add(@RequestBody CheckItem checkItem){
        //直接返回添加成功代码能通
//        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        try {
            checkItemService.add(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

}
