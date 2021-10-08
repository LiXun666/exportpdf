package com.exportpdf.demo.controller;

import com.exportpdf.demo.mapper.ReportsMapper;
import com.exportpdf.demo.pojo.Goods;
import com.exportpdf.demo.pojo.Reports;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Author lxjj
 * @Date 2021-10-04 11:09
 * @Description
 */
@Controller
@RequestMapping("/home")
public class ManageController {

    @Autowired
    private ReportsMapper reportsMapper;

    @GetMapping("/reportsManage")
    public ModelAndView reportsManage(Model model){
        List<Reports> reports = reportsMapper.selectAllReports();

        model.addAttribute("reportsList",reports);
        return new ModelAndView("reportReview", "reportModel", model);
    }

}
