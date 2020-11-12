package com.benefitj.system.controller;

import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.page.RequestPage;
import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.AopWebPointCut;
import com.benefitj.system.model.SysMenu;
import com.benefitj.system.service.SysMenuService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单
 */
@AopWebPointCut
@Api(tags = {"菜单"}, description = "对菜单的各种操作")
@RestController
@RequestMapping("/menus")
public class MenuController {

  @Autowired
  private SysMenuService menuService;

  @ApiOperation("获取菜单")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "菜单ID", required = true, dataType = "String"),
  })
  @GetMapping
  public HttpResult<?> get(String id) {
    SysMenu menu = menuService.get(id);
    return HttpResult.create(CommonStatus.OK, menu);
  }

  @ApiOperation("添加菜单")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "menu", value = "菜单数据"),
  })
  @PostMapping
  public HttpResult<?> create(SysMenu menu) {
    menu = menuService.create(menu);
    return HttpResult.create(CommonStatus.CREATED, menu);
  }

  @ApiOperation("更新菜单")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "menu", value = "菜单数据"),
  })
  @PutMapping
  public HttpResult<?> update(@RequestBody SysMenu menu) {
    if (StringUtils.isAnyBlank(menu.getId(), menu.getName())) {
      return HttpResult.failure("菜单ID和菜单名都不能为空");
    }
    menu = menuService.update(menu);
    return HttpResult.create(CommonStatus.CREATED, menu);
  }

  @ApiOperation("删除菜单")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "菜单ID", dataType = "String"),
      @ApiImplicitParam(name = "force", value = "是否强制", dataType = "Boolean"),
  })
  @DeleteMapping
  public HttpResult<?> delete(String id, Boolean force) {
    int count = menuService.delete(id, Boolean.TRUE.equals(force));
    return HttpResult.create(CommonStatus.NO_CONTENT, count);
  }

  @ApiOperation("改变菜单的状态")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "菜单ID", dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "status", value = "状态", dataType = "Boolean", paramType = "form"),
  })
  @PatchMapping("/active")
  public HttpResult<?> changeActive(String id, Boolean status) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("菜单ID不能为空");
    }
    Boolean result = menuService.changeActive(id, status);
    return HttpResult.create(CommonStatus.OK, result);
  }

  @ApiOperation("获取菜单列表分页")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "page", value = "分页参数", dataType = "RequestPage"),
  })
  @GetMapping("/page")
  public HttpResult<?> getPage(RequestPage<SysMenu> page) {
    PageInfo<SysMenu> menuList = menuService.getPage(page);
    return HttpResult.success(menuList);
  }

  @ApiOperation("获取机构的菜单列表")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "orgId", value = "机构ID", dataType = "String"),
      @ApiImplicitParam(name = "active", value = "是否可用", dataType = "Boolean"),
      @ApiImplicitParam(name = "multiLevel", value = "是否返回多级机构的数据", dataType = "Boolean"),
  })
  @GetMapping("/list")
  public HttpResult<?> getMenuList(String orgId, Boolean active, Boolean multiLevel) {
    orgId = Checker.checkNotBlank(orgId, JwtTokenManager.currentOrgId());
    if (StringUtils.isBlank(orgId)) {
      return HttpResult.failure("orgId为空");
    }
    List<SysMenu> menuList = menuService.getMenuList(orgId, active, multiLevel);
    return HttpResult.success(menuList);
  }


}