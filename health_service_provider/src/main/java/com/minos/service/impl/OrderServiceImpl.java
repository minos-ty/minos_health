package com.minos.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.minos.constant.MessageConstant;
import com.minos.dao.MemberDao;
import com.minos.dao.OrderDao;
import com.minos.dao.OrderSettingDao;
import com.minos.entity.Result;
import com.minos.pojo.Member;
import com.minos.pojo.Order;
import com.minos.pojo.OrderSetting;
import com.minos.service.OrderService;
import com.minos.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 体检预约服务
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    //体检预约
    @Override
    public Result order(Map map) throws Exception{
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String) map.get("orderDate");//预约日期
//        System.out.println("orderDate================"+orderDate); // test 2021-03-17
//        System.out.println("parseString2Date================"+DateUtils.parseString2Date(orderDate));
//        System.out.println(orderSettingDao.findByOrderDate("2021-03-17"));
//        OrderSetting orderSetting= orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
        OrderSetting orderSetting= orderSettingDao.findByOrderDate(orderDate);
        System.out.println("orderSetting================"+orderSetting);
        if(orderSetting == null){
            //指定日期没有进行预约设置，无法完成体检预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if(reservations >= number){
            //已经约满，无法预约
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        String telephone = (String) map.get("telephone");//获取用户页面输入的手机号
        Member member = memberDao.findByTelephone(telephone);
        if(member != null){
            //判断是否在重复预约
            Integer memberId = member.getId();//会员ID
            Date order_Date = DateUtils.parseString2Date(orderDate);//预约日期
            String setmealId = (String) map.get("setmealId");//套餐ID
            Order order = new Order(memberId, order_Date, Integer.parseInt(setmealId));
            //根据条件进行查询
            List<Order> list = orderDao.findByCondition(order);
            if(list != null && list.size() > 0){
                //说明用户在重复预约，无法完成再次预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else{
            //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));

            Date date=new Date(); //取时间
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE,1); //把日期往后增加一天,整数  往后推,负数往前移动
            date=calendar.getTime(); //这个时间就是日期往后推一天的结果

            member.setRegTime(date);
            memberDao.add(member);//自动完成会员注册
        }

        //5、预约成功，更新当日的已预约人数
        Order order = new Order();
        order.setMemberId(member.getId());//设置会员ID
        order.setOrderDate(DateUtils.parseString2Date(orderDate));//预约日期
        order.setOrderType((String) map.get("orderType"));//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));//套餐ID
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations() + 1);//设置已预约人数+1
        System.out.println("orderSetting.getReservations()===============" + orderSetting.getReservations());
        orderSetting.setStrDate(DateUtils.parseDate2String(orderSetting.getOrderDate()));
        System.out.println("orderSetting========================="+orderSetting);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    //根据预约ID查询预约相关信息（体检人姓名、预约日期、套餐名称、预约类型）
    @Override
    public Map findById(Integer id) throws Exception{
        Map map = orderDao.findById4Detail(id);
        if(map != null){
            //处理日期格式
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
