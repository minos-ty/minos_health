package com.minos.service;

import com.minos.entity.PageResult;
import com.minos.entity.QueryPageBean;

import java.util.Map;

public interface OrderSettingListService {
    // 分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean);

    // 修改预约的状态
    public void updateStatusByOrderId(int orderId) throws Exception;

    // 删除一条预约信息
    public void delete(Map map) throws Exception;
}
