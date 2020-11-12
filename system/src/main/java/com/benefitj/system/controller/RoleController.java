package com.benefitj.system.controller;

import com.benefitj.scaffold.Checker;
import com.benefitj.scaffold.page.RequestPage;
import com.benefitj.scaffold.security.token.JwtTokenManager;
import com.benefitj.scaffold.vo.CommonStatus;
import com.benefitj.scaffold.vo.HttpResult;
import com.benefitj.spring.aop.AopWebPointCut;
import com.benefitj.system.model.SysRole;
import com.benefitj.system.service.SysRoleService;
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
 * 角色
 */
@AopWebPointCut
@Api(tags = {"角色"}, description = "对角色的各种操作")
@RestController
@RequestMapping("/roles")
public class RoleController {

  @Autowired
  private SysRoleService roleService;

  @ApiOperation("获取角色")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "角色ID", required = true, dataType = "String"),
  })
  @GetMapping
  public HttpResult<?> get(String id) {
    SysRole role = roleService.get(id);
    return HttpResult.create(CommonStatus.OK, role);
  }

  @ApiOperation("添加角色")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "role", value = "角色数据"),
  })
  @PostMapping
  public HttpResult<?> create(SysRole role) {
    role = roleService.create(role);
    return HttpResult.create(CommonStatus.CREATED, role);
  }

  @ApiOperation("更新角色")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "role", value = "角色数据"),
  })
  @PutMapping
  public HttpResult<?> update(@RequestBody SysRole role) {
    if (StringUtils.isAnyBlank(role.getId(), role.getName())) {
      return HttpResult.failure("角色ID和角色名都不能为空");
    }
    roleService.update(role);
    return HttpResult.create(CommonStatus.CREATED, role);
  }

  @ApiOperation("删除角色")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "角色ID", dataType = "String"),
      @ApiImplicitParam(name = "force", value = "是否强制", dataType = "Boolean"),
  })
  @DeleteMapping
  public HttpResult<?> delete(String id, Boolean force) {
    int count = roleService.delete(id, Boolean.TRUE.equals(force));
    return HttpResult.create(CommonStatus.NO_CONTENT, count);
  }

  @ApiOperation("改变角色的状态")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "id", value = "角色ID", dataType = "String", paramType = "form"),
      @ApiImplicitParam(name = "status", value = "状态", dataType = "Boolean", paramType = "form"),
  })
  @PatchMapping("/active")
  public HttpResult<?> changeActive(String id, Boolean status) {
    if (StringUtils.isBlank(id)) {
      return HttpResult.failure("角色ID不能为空");
    }
    Boolean result = roleService.changeActive(id, status);
    return HttpResult.create(CommonStatus.OK, result);
  }

  @ApiOperation("获取角色列表分页")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "page", value = "分页参数", dataType = "RequestPage"),
  })
  @GetMapping("/page")
  public HttpResult<?> getPage(RequestPage<SysRole> page) {
    PageInfo<SysRole> roleList = roleService.getPage(page);
    return HttpResult.success(roleList);
  }

  @ApiOperation("获取机构的角色列表")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "orgId", value = "机构ID", dataType = "String"),
      @ApiImplicitParam(name = "active", value = "是否可用", dataType = "Boolean"),
      @ApiImplicitParam(name = "multiLevel", value = "是否返回多级机构的数据", dataType = "Boolean"),
  })
  @GetMapping("/list")
  public HttpResult<?> getRoleList(String orgId, Boolean active, Boolean multiLevel) {
    orgId = Checker.checkNotBlank(orgId, JwtTokenManager.currentOrgId());
    if (StringUtils.isBlank(orgId)) {
      return HttpResult.failure("机构ID不能为空");
    }
    List<SysRole> roleList = roleService.getRoleList(orgId, active, multiLevel);
    return HttpResult.success(roleList);
  }


}
