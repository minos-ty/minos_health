package com.minos.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.minos.dao.OrderSettingDao;
import com.minos.dao.OrderSettingListDao;
import com.minos.entity.PageResult;
import com.minos.entity.QueryPageBean;
import com.minos.pojo.Order;
import com.minos.pojo.OrderSetting;
import com.minos.service.OrderSettingListService;
import com.minos.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service(interfaceClass = OrderSettingListService.class)
@Transactional
public class OrderSettingListServiceImpl implements OrderSettingListService {

    @Autowired
    private OrderSettingListDao orderSettingListDao;

    @Autowired
    private OrderSettingDao orderSettingDao;


    // 分页查询
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        // 分页助手
        PageHelper.startPage( queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        // 条件查询
        Page<Map> maps = orderSettingListDao.findByCondition(queryPageBean.getQueryString());

        // 返回一个 分页结果对象回去
        return new PageResult(maps.getTotal(), maps.getResult());
    }

    // 修改预约的状态
    @Override
    public void updateStatusByOrderId(int orderId) throws Exception {
        // 判断是否已经预约
        Order byOrderId = orderSettingListDao.findByOrderId(orderId);
        if(byOrderId != null && !Order.ORDERSTATUS_YES.equals(byOrderId.getOrderStatus())){
            // 更新预约状态
            orderSettingListDao.updateStatusByOrderId(orderId);
        }else{
            throw new Exception("抛出异常");        // 修改失败（不存在、已经预约）父方法捕获
        }
    }

    // 删除一条预约信息
    @Override
    public void delete(Map map) throws Exception {
        // 判断是否已经预约确珍过了
        if(Order.ORDERSTATUS_YES.equals(map.get("orderStatus"))){
            throw new Exception("抛出异常");        // 修改失败（不存在、已经预约）父方法捕获
        }

        // 修改预约日期的人数 -1
        System.out.println("============================"+DateUtils.parseString2Date((String)map.get("orderDate")));
        System.out.println("============================"+(String)map.get("orderDate"));

        String beforeSplitOrderDate = (String)map.get("orderDate");
        String[] afterSplit = beforeSplitOrderDate.split("\\s+");
        System.out.println("============================"+afterSplit[0]);

        OrderSetting orderSetting = orderSettingDao.findByOrderDate(afterSplit[0]);
        System.out.println("=========orderSetting"+orderSetting);

        orderSetting.setReservations(orderSetting.getReservations() - 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);                      // 将数据写回数据库

        orderSettingListDao.deleteByOrderId(Integer.parseInt(map.get("id").toString()));   // 删除预约信息
    }
}
