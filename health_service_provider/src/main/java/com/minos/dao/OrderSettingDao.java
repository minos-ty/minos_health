package com.minos.dao;

import com.minos.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    public void add(OrderSetting orderSetting);
    public void editNumberByOrderDate(OrderSetting orderSetting);
    public long findCountByOrderDate(Date orderDate);
    public List<OrderSetting> getOrderSettingByMonth(Map map);
    public OrderSetting findByOrderDate(String orderDate);
    public void editReservationsByOrderDate(OrderSetting orderSetting);
}
