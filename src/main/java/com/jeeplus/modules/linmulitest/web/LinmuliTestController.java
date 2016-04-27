/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.linmulitest.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.linmulitest.entity.LinmuliTest;
import com.jeeplus.modules.linmulitest.service.LinmuliTestService;

/**
 * 测试功能Controller
 * @author slowdiver
 * @version 2016-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/linmulitest/linmuliTest")
public class LinmuliTestController extends BaseController {

	@Autowired
	private LinmuliTestService linmuliTestService;
	
	@ModelAttribute
	public LinmuliTest get(@RequestParam(required=false) String id) {
		LinmuliTest entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = linmuliTestService.get(id);
		}
		if (entity == null){
			entity = new LinmuliTest();
		}
		return entity;
	}
	
	/**
	 * 测试功能列表页面
	 */
	@RequiresPermissions("linmulitest:linmuliTest:list")
	@RequestMapping(value = {"list", ""})
	public String list(LinmuliTest linmuliTest, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<LinmuliTest> page = linmuliTestService.findPage(new Page<LinmuliTest>(request, response), linmuliTest); 
		model.addAttribute("page", page);
		return "modules/linmulitest/linmuliTestList";
	}

	/**
	 * 查看，增加，编辑测试功能表单页面
	 */
	@RequiresPermissions(value={"linmulitest:linmuliTest:view","linmulitest:linmuliTest:add","linmulitest:linmuliTest:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(LinmuliTest linmuliTest, Model model) {
		model.addAttribute("linmuliTest", linmuliTest);
		return "modules/linmulitest/linmuliTestForm";
	}

	/**
	 * 保存测试功能
	 */
	@RequiresPermissions(value={"linmulitest:linmuliTest:add","linmulitest:linmuliTest:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(LinmuliTest linmuliTest, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, linmuliTest)){
			return form(linmuliTest, model);
		}
		if(!linmuliTest.getIsNewRecord()){//编辑表单保存
			LinmuliTest t = linmuliTestService.get(linmuliTest.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(linmuliTest, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			linmuliTestService.save(t);//保存
		}else{//新增表单保存
			linmuliTestService.save(linmuliTest);//保存
		}
		addMessage(redirectAttributes, "保存测试功能成功");
		return "redirect:"+Global.getAdminPath()+"/linmulitest/linmuliTest/?repage";
	}
	
	/**
	 * 删除测试功能
	 */
	@RequiresPermissions("linmulitest:linmuliTest:del")
	@RequestMapping(value = "delete")
	public String delete(LinmuliTest linmuliTest, RedirectAttributes redirectAttributes) {
		linmuliTestService.delete(linmuliTest);
		addMessage(redirectAttributes, "删除测试功能成功");
		return "redirect:"+Global.getAdminPath()+"/linmulitest/linmuliTest/?repage";
	}
	
	/**
	 * 批量删除测试功能
	 */
	@RequiresPermissions("linmulitest:linmuliTest:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			linmuliTestService.delete(linmuliTestService.get(id));
		}
		addMessage(redirectAttributes, "删除测试功能成功");
		return "redirect:"+Global.getAdminPath()+"/linmulitest/linmuliTest/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("linmulitest:linmuliTest:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(LinmuliTest linmuliTest, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "测试功能"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<LinmuliTest> page = linmuliTestService.findPage(new Page<LinmuliTest>(request, response, -1), linmuliTest);
    		new ExportExcel("测试功能", LinmuliTest.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出测试功能记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/linmulitest/linmuliTest/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("linmulitest:linmuliTest:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<LinmuliTest> list = ei.getDataList(LinmuliTest.class);
			for (LinmuliTest linmuliTest : list){
				linmuliTestService.save(linmuliTest);
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条测试功能记录");
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入测试功能失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/linmulitest/linmuliTest/?repage";
    }
	
	/**
	 * 下载导入测试功能数据模板
	 */
	@RequiresPermissions("linmulitest:linmuliTest:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "测试功能数据导入模板.xlsx";
    		List<LinmuliTest> list = Lists.newArrayList(); 
    		new ExportExcel("测试功能数据", LinmuliTest.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/linmulitest/linmuliTest/?repage";
    }
	

}