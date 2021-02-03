package com.minos.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.minos.constant.MessageConstant;
import com.minos.entity.PageResult;
import com.minos.entity.QueryPageBean;
import com.minos.entity.Result;
import com.minos.pojo.CheckItem;
import com.minos.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        try {
            checkItemService.add(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //检查项分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.pageQuery(queryPageBean);
     return pageResult;
    }

    //删除检查项
    @RequestMapping("/delete")
    public Result delete(Integer id){

        try {
            checkItemService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //修改检查项
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    //回显修改检查项的数据 good
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
           CheckItem checkItem = checkItemService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckItem> list = checkItemService.fndAll();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
