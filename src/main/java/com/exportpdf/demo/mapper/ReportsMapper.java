package com.exportpdf.demo.mapper;

import com.exportpdf.demo.pojo.Goods;
import com.exportpdf.demo.pojo.Reports;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReportsMapper {
    List<Reports> selectAllReports();
    Reports getReportById(Integer id);
}
