package com.minos.dao;

import com.github.pagehelper.Page;
import com.minos.pojo.Order;

import java.util.Map;

public interface OrderSettingListDao {
    // 条件查询
    public Page<Map> findByCondition(String queryString);

    // 修改预约的状态
    public void updateStatusByOrderId(int orderId);

    // 根据id查询某一条预约信息
    public Order findByOrderId(int orderId);

    // 删除一条信息
    public void deleteByOrderId(int orderId);
}
