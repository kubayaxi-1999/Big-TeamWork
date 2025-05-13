package com.example.controller;

import com.example.common.Result;
import com.example.entity.Bill;
import com.example.service.BillService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 账单表前端操作接口
 **/
@RestController
@RequestMapping("/bill")
public class BillController {

    @Resource
    private BillService billService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Bill bill) {
        billService.add(bill);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        billService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        billService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Bill bill) {
        billService.updateById(bill);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Bill bill = billService.selectById(id);
        return Result.success(bill);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Bill bill) {
        List<Bill> list = billService.selectAll(bill);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Bill bill,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Bill> page = billService.selectPage(bill, pageNum, pageSize);
        return Result.success(page);
    }
}