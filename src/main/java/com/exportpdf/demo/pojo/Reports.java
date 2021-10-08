package com.exportpdf.demo.pojo;

import lombok.*;

/**
 * @Author lxjj
 * @Date 2021-10-04 11:15
 * @Description
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reports {
   private Long id;
   private String testNo;
   private String applicant;
   private String curState;
   private String projectName;

}
