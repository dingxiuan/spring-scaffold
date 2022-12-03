package com.benefitj.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 按钮的操作
 */
@ApiModel("按钮的操作")
public enum ButtonAction implements IAction {

  @ApiModelProperty("查看")
  VIEW(Action.VIEW, "查看"),

  @ApiModelProperty("添加")
  ADD(Action.ADD, "添加"),

  @ApiModelProperty("删除")
  DELETE(Action.DELETE, "删除"),

  @ApiModelProperty("可点击")
  CLICK(Action.CLICK, "可点击"),
  ;

  /**
   * 类型
   */
  private final Action type;
  /**
   * 名称
   */
  private final String name;

  ButtonAction(Action type, String name) {
    this.type = type;
    this.name = name;
  }

  @Override
  public Action getType() {
    return type;
  }

  @Override
  public String getName() {
    return name;
  }

}
