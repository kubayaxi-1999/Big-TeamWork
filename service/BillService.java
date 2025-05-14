package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.AcTypeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Ac;
import com.example.entity.Account;
import com.example.entity.Bill;
import com.example.mapper.BillMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 账单表业务处理
 **/
@Service
public class BillService {

    @Resource
    private BillMapper billMapper;
    @Resource
    AcService acService;

    /**
     * 新增
     */
    @Transactional
    public void add(Bill bill) {
        bill.setTime(DateUtil.now());
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            bill.setUserId(currentUser.getId());
        }
        billMapper.insert(bill);

        //开户
       Ac ac =  acService.selectByUserId(currentUser.getId());
        if(ac==null){
              ac = new Ac();
              ac.setUserId(currentUser.getId());
              ac.setBalance(BigDecimal.ZERO);
              ac.setPay(BigDecimal.ZERO);
              ac.setIncome(BigDecimal.ZERO);
              acService.add(ac);
        }

        //更新账户信息
        if(AcTypeEnum.PAY.getValue().equals(bill.getType())){
            ac.setPay(ac.getPay().add(bill.getMoney()));//支出
            ac.setBalance(ac.getBalance().subtract(bill.getMoney()));//余额
        }
        else{
            ac.setIncome(ac.getIncome().add(bill.getMoney()));//收入
            ac.setBalance(ac.getBalance().add(bill.getMoney()));//余额
        }
        acService.updateById(ac);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        billMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            billMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Bill bill) {
        billMapper.updateById(bill);
    }

    /**
     * 根据ID查询
     */
    public Bill selectById(Integer id) {
        return billMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Bill> selectAll(Bill bill) {
        return billMapper.selectAll(bill);
    }

    /**
     * 分页查询
     */
    public PageInfo<Bill> selectPage(Bill bill, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            bill.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Bill> list = billMapper.selectAll(bill);
        return PageInfo.of(list);
    }

}