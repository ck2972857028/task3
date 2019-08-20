package com.controller;

import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.Message;
import com.pojo.Studio;
import com.service.MessageService;
import com.service.StudioService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/*
*
* 删掉视图层
* */
@Controller
public class StudioController {
    @Autowired
    StudioService studioService;
    private static final Logger log= LogManager.getLogger(StudioController.class);
    //工作室列表
    @RequestMapping(value = "Studios/AllStudios",method = RequestMethod.GET)
    @ResponseBody
    public String AllS(){
        List<Studio> studioList = studioService.findAll();
        log.error("一次列表查询"+studioList.size());
        return "AllStudios : "+studioList;
    }
    //根据名称查询
    @RequestMapping(value = "Studios/StudiosByName",method = RequestMethod.GET)
    @ResponseBody
    public String StudioBN( String studio_name,
                           @Validated Studio studio, BindingResult bindingResult){
        if(studio_name.length()==0&&bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError:allErrors){
                log.error(objectError.getDefaultMessage());
            }
            return "Error"+allErrors;
        }
        List<Studio> studioList = studioService.findByStudioName(studio_name);
        log.error("据名查询"+studioList);
        return "StudioFBN: " +studioList;
    }
    //根据状态查询
    @RequestMapping(value = "Studios/StudiosByStatus",method = RequestMethod.GET)
    @ResponseBody
    public String StudioBS(HttpServletRequest request,String status,
                           @Validated Studio studio,BindingResult bindingResult){
        if(status.length()==0&&bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError:allErrors){
                log.error(objectError.getDefaultMessage());
            }
            return "Error: "+allErrors;
        }
        List<Studio> studioList=studioService.findByStatus(status);
        log.error("据状态查询"+studioList);
        return "StudioFBS "+studioList;
    }
    //新增,简介类型,图片,正文
    @RequestMapping(value = "Studios/MoreStudios",method = RequestMethod.POST)
    @ResponseBody
    public String MoreS(HttpServletRequest request,
                        @Validated Studio studio,BindingResult bindingResult){
        if(studio.getStudio_name().length()==0||bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError:allErrors){
                log.error(objectError.getDefaultMessage());
            }
            log.error("执行了if语句块");
            return "Error"+bindingResult;
        }
        studio.setCreate_at(System.currentTimeMillis());
        studio.setUpdate_at(System.currentTimeMillis());
        studioService.insert(studio);
        return "Success" + studio;
    }
    //编辑
    @RequestMapping(value = "Studios/StudiosInfo",method = RequestMethod.PUT)
    @ResponseBody
    public String StudioI(HttpServletRequest request,
                          @Validated Studio studio,BindingResult bindingResult){
        if(studio.getStudio_name().length()==0||bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError:allErrors){
                log.error(objectError.getDefaultMessage());
            }
            return "Error: "+allErrors;
        }
        studio.setUpdate_at(System.currentTimeMillis());
        boolean flag = studioService.updateByPrimaryKey(studio);
        if(flag=true)
        {return "Success "+studio;}
        return "Error";
    }
    //上架
    @RequestMapping(value = "Studios/UpStudios",method = RequestMethod.PUT)
    @ResponseBody
    public String US(Long id){
        studioService.upStudio(id);
        log.error("上架了工作室,id为"+id);
        return "up studio id: " + id;
    }
    //下架
    @RequestMapping(value = "Studios/DownStudios",method = RequestMethod.PUT)
    @ResponseBody
    public String DS(Long id){
        studioService.downStudio(id);
        log.error("下架了工作室,id为"+id);
        return "down studio id: " + id;
    }

   /* //下面都是测试
    //测试json
    @RequestMapping(value = "Studios/Json")
    public String selectAll(Model model) {
        List<Studio> studioList = studioService.findAll();
        model.addAttribute("code", "200");
        model.addAttribute("message", "传递成功");
        model.addAttribute("studioList", studioList);
        return "Json";
    }

    //用于测试
    @RequestMapping(value = "/Json")
    @ResponseBody
    public List<Studio> J(){
        List<Studio> studioList = studioService.findAll();
        return studioList;
    }

    @RequestMapping(value = "/Json2")
    @ResponseBody
    public String S(){
        String s = "update success";
        return s;
    }*/
}
