package com.glu.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.glu.yygh.common.result.Result;
import com.glu.yygh.common.utils.MD5;
import com.glu.yygh.hosp.service.HospitalSetService;
import com.glu.yygh.model.hosp.HospitalSet;
import com.glu.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Random;

@Api("医院设置管理")
//@CrossOrigin //允许跨域
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;


    /**
     * 查询所有医院信息
     * @return
     */
    @ApiOperation("获取所有医院设置信息")
    @GetMapping("findAll")
    public Result findAllHospitalSet(){
        //调用service的方法
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    /**
     * 根据id删除医院设置信息
     * @param id
     * @return
     */
    @ApiOperation("根据ID删除医院设置信息")
    @DeleteMapping("{id}")
    public Result delHosp(@PathVariable("id") Long id){

        boolean flag = hospitalSetService.removeById(id);
        if (flag == true){
            return Result.ok();
        }

        return Result.fail();
    }

    //条件查询带分页
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findePageHospSet(@PathVariable long current,
                                   @PathVariable long limit,
                                   //意思就是这个值可以没有，因为现在是json值了，所以要用PostMapping
                                   @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> page = new Page<>(current,limit);

        //构建条件
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();//医院名称
        String hoscode = hospitalSetQueryVo.getHoscode();//医院编号
        if(StringUtils.isNotBlank(hosname)){
            queryWrapper.like("hosname",hospitalSetQueryVo.getHosname());
        }
        if(StringUtils.isNotBlank(hoscode)){
            queryWrapper.eq("hoscode",hospitalSetQueryVo.getHoscode());
        }

        //调用方法实现分页查询
        Page<HospitalSet> pageHospitalSet = hospitalSetService.page(page, queryWrapper);

        //返回结果
        return Result.ok(pageHospitalSet);

    }


    //添加医院设置
    @PostMapping("saveHospitalSet")
    public Result save(@RequestBody HospitalSet hospitalSet){
        //设置状态1为可以使用，0不可以使用
        hospitalSet.setStatus(1);
        //签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));

        //调用service
        boolean save = hospitalSetService.save(hospitalSet);
        if(save){
            return Result.ok();
        }

        return Result.fail();

    }

    //根据id获取医院设置
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id){

        HospitalSet hospitalSet = hospitalSetService.getById(id);

        return Result.ok(hospitalSet);

    }

    //修改医院设置
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if(flag){
            return Result.ok();
        }
        return Result.fail();
    }

    //批量删除医院设置
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospital(@RequestBody List<Long> ids){
        hospitalSetService.removeByIds(ids);
        return Result.ok();
    }

    //医院设置锁定和解锁
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status){
        //根据id查询出医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }


    //发送签名密钥
    @PutMapping("sendKey/{id}/{status}")
    public Result sendKeyHospitalSet(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //todo 发送短信
        return Result.ok();
    }

}
