- [WebMall](#webmall)
  - [简介](#%E7%AE%80%E4%BB%8B)
  - [后端主要相关技术](#%E5%90%8E%E7%AB%AF%E4%B8%BB%E8%A6%81%E7%9B%B8%E5%85%B3%E6%8A%80%E6%9C%AF)
  - [后端项目结构图](#%E5%90%8E%E7%AB%AF%E9%A1%B9%E7%9B%AE%E7%BB%93%E6%9E%84%E5%9B%BE)
  - [接口文档](#%E6%8E%A5%E5%8F%A3%E6%96%87%E6%A1%A3)
  - [页面展示](#%E9%A1%B5%E9%9D%A2%E5%B1%95%E7%A4%BA)

# WebMall

组长:[@Kapiushon-HLT](https://github.com/Kapiushon-HLT/WebMall)  
组员:[@deshuo](https://github.com/deng-shuo) , [@hjwforever](https://github.com/hjwforever)

## 简介

WebMall 项目是一个简易的商城系统，采用前后端分离，基于 前端 Vue 及 后端 Spring Boot 2.X 以及相关技术栈开发。  
前台商城系统包含首页门户、商品分类、新品上线、首页轮播、商品推荐、商品搜索、商品展示、购物车、订单结算、订单流程、个人订单管理、会员中心、帮助中心等模块。  
后台管理系统包含数据面板、轮播图管理、商品管理、订单管理、会员管理、分类管理、设置等模块。

本仓库为 WebMall 商城的 后端 Springboot 项目, 前端项目在另外一个仓库[hjwforever/WebMall-vue](https://github.com/hjwforever/webmall-vue)

后端 api 服务器项目仓库地址:[Kapiushon-HLT/WebMall-api](https://github.com/Kapiushon-HLT/WebMall-api)
后端[在线接口文档](http://api.aruoxi.top:28019/swagger-ui.html)

WebMall 商城 Vue 版本线上预览地址[WebMall](https://webmall.aruoxi.top)，账号可自行注册，建议使用手机模式打开。

## 后端主要相关技术

- [spring-projects](https://github.com/spring-projects/spring-boot)
- [mybatis](https://github.com/mybatis/mybatis-3)
- [swagger](https://github.com/swagger-api) (生成[在线接口文档](http://47.94.235.82:28019/swagger-ui.html))
- [Lombok](https://github.com/rzwitserloot)

## 后端项目结构图

```code
├─.gitattributes
├─.gitignore  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // Git忽略文件
├─LICENSE
├─pom.xml
├─README.md  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // README文件
├─src  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 主目录
│ └─main
│   ├─java
│   │ └─top
│   │   └─aruoxi
│   │     └─webmall
│   │       ├─api  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // api接口模块
│   │       │ ├─param  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 参数模块
│   │       │ │ ├─MallUserLoginParam.java  - - - - - - - - - - - - - - - - - - - - - - - // 用户登录参数
│   │       │ │ ├─MallUserRegisterParam.java  - - - - - - - - - - - - - - - - - - - - // 用户注册参数
│   │       │ │ ├─MallUserUpdateParam.java  - - - - - - - - - - - - - - - - - - - - - - // 用户信息更新参数
│   │       │ │ ├─SaveCartItemParam.java  - - - - - - - - - - - - - - - - - - - - - - - - // 添加商品至购物车参数
│   │       │ │ ├─SaveMallUserAddressParam.java  - - - - - - - - - - - - - - - - - // 新增地址参数
│   │       │ │ ├─SaveOrderParam.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - // 创建订单参数
│   │       │ │ ├─UpdateCartItemParam.java  - - - - - - - - - - - - - - - - - - - - - - // 更新购物车参数
│   │       │ │ └─UpdateMallUserAddressParam.java  - - - - - - - - - - - - - - - // 更新地址参数
│   │       │ ├─vo  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // vo模块
│   │       │ │ ├─IndexInfoVO.java
│   │       │ │ ├─SearchPageCategoryVO.java
│   │       │ │ ├─SecondLevelCategoryVO.java
│   │       │ │ ├─ThirdLevelCategoryVO.java
│   │       │ │ ├─WebMallIndexCarouselVO.java
│   │       │ │ ├─WebMallIndexCategoryVO.java
│   │       │ │ ├─WebMallIndexConfigGoodsVO.java
│   │       │ │ ├─WebMallMallGoodsDetailVO.java
│   │       │ │ ├─WebMallOrderDetailVO.java
│   │       │ │ ├─WebMallOrderItemVO.java
│   │       │ │ ├─WebMallOrderListVO.java
│   │       │ │ ├─WebMallSearchGoodsVO.java
│   │       │ │ ├─WebMallShoppingCartItemVO.java
│   │       │ │ ├─WebMallUserAddressVO.java
│   │       │ │ └─WebMallUserVO.java
│   │       │ ├─WebMallGoodsAPI.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 商品相关API
│   │       │ ├─WebMallGoodsCategoryAPI.java  - - - - - - - - - - - - - - - - - - - - // 商品分类相关API
│   │       │ ├─WebMallIndexAPI.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 首页数据相关API
│   │       │ ├─WebMallOrderAPI.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 订单相关API
│   │       │ ├─WebMallPersonalAPI.java  - - - - - - - - - - - - - - - - - - - - - - - - - // 个人信息相关API
│   │       │ ├─WebMallShoppingCartAPI.java  - - - - - - - - - - - - - - - - - - - - - // 购物车相关API
│   │       │ └─WebMallUserAddressAPI.java  - - - - - - - - - - - - - - - - - - - - - - // 地址信息相关API
│   │       ├─common  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 常用模块
│   │       │ ├─Constants.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 首页内容
│   │       │ ├─IndexConfigTypeEnum.java  - - - - - - - - - - - - - - - - - - - - - - - - // 首页商品类型(热门商品，新品上市等)
│   │       │ ├─PayStatusEnum.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 支付状态
│   │       │ ├─PayTypeEnum.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 支付方式(微信、支付宝等)
│   │       │ ├─ServiceResultEnum.java  - - - - - - - - - - - - - - - - - - - - - - - - - - // 操作结果(登录失败、商品不存在、购物车数据异常等信息)
│   │       │ ├─WebMallCategoryLevelEnum.java
│   │       │ ├─WebMallException.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - // 抛出异常
│   │       │ └─WebMallOrderStatusEnum.java  - - - - - - - - - - - - - - - - - - - - - // 订单状态
│   │       ├─config
│   │       │ ├─annotation
│   │       │ │ └─TokenToMallUser.java  - - - - - - - - - - - - - - - - - - - - - - - - - - // token用户名字
│   │       │ ├─handler
│   │       │ │ └─TokenToMallUserMethodArgumentResolver.java  - - - - // token方法
│   │       │ ├─Swagger2Config.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // Swagger2参数设置(用于生成本项目在线接口文档)
│   │       │ ├─WebMallExceptionHandler.java  - - - - - - - - - - - - - - - - - - - - // 全局异常处理
│   │       │ └─WebMallWebMvcConfigurer.java  - - - - - - - - - - - - - - - - - - - - // 跨域配置
│   │       ├─dao  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // dao层
│   │       ├─entity  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 实体层
│   │       │ ├─Carousel.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 轮播图
│   │       │ ├─GoodsCategory.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 商品分类
│   │       │ ├─IndexConfig.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 首页参数设置
│   │       │ ├─MallUser.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 用户
│   │       │ ├─MallUserAddress.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 用户收货地址
│   │       │ ├─MallUserToken.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 用户token
│   │       │ ├─StockNumDTO.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 商品库存
│   │       │ ├─WebMallGoods.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 商品
│   │       │ ├─WebMallOrder.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 订单
│   │       │ ├─WebMallOrderAddress.java  - - - - - - - - - - - - - - - - - - - - - - - - // 订单收货地址
│   │       │ ├─WebMallOrderItem.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - // 订单个体
│   │       │ └─WebMallShoppingCartItem.java  - - - - - - - - - - - - - - - - - - - - // 购物车商品
│   │       ├─service  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 服务层
│   │       │ ├─impl  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 实现
│   │       │ │ ├─WebMallCarouselServiceImpl.java
│   │       │ │ ├─WebMallCategoryServiceImpl.java
│   │       │ │ ├─WebMallGoodsServiceImpl.java
│   │       │ │ ├─WebMallIndexConfigServiceImpl.java
│   │       │ │ ├─WebMallOrderServiceImpl.java
│   │       │ │ ├─WebMallShoppingCartServiceImpl.java
│   │       │ │ ├─WebMallUserAddressServiceImpl.java
│   │       │ │ └─WebMallUserServiceImpl.java
│   │       │ ├─WebMallCarouselService.java  - - - - - - - - - - - - - - - - - - - - - // 提供轮播图
│   │       │ ├─WebMallCategoryService.java  - - - - - - - - - - - - - - - - - - - - - // 提供商品分类
│   │       │ ├─WebMallGoodsService.java  - - - - - - - - - - - - - - - - - - - - - - - - // 获取商品详情及商品搜索
│   │       │ ├─WebMallIndexConfigService.java  - - - - - - - - - - - - - - - - - - // 返回固定数量的首页配置商品对象(首页调用)
│   │       │ ├─WebMallOrderService.java  - - - - - - - - - - - - - - - - - - - - - - - - // 获取及操作商品订单
│   │       │ ├─WebMallShoppingCartService.java  - - - - - - - - - - - - - - - - - // 获取及操作购物车
│   │       │ ├─WebMallUserAddressService.java  - - - - - - - - - - - - - - - - - - // 获取及操作收货地址
│   │       │ └─WebMallUserService.java  - - - - - - - - - - - - - - - - - - - - - - - - - // 登录、注册、登出、修改用户信息
│   │       ├─util  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 工具模块
│   │       │ ├─BeanUtil.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // Bean工具
│   │       │ ├─MD5Util.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // MD5加密
│   │       │ ├─NumberUtil.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // Number工具
│   │       │ ├─PageQueryUtil.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 分页查找
│   │       │ ├─PageResult.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 分页结果
│   │       │ ├─Result.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 返回结果
│   │       │ ├─ResultGenerator.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 生成返回结果类型
│   │       │ ├─SystemUtil.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 登录、注册后对token的操作
│   │       │ └─WebMallUtils.java  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 替换字符串
│   │       └─WebMallAPIApplication.java  - - - - - - - - - - - - - - - - - - - - - - - - // 主线程类
│   └─resources  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 资源文件目录
│     ├─application.properties  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 项目设置(项目端口、数据库参数配置等)
│     ├─mapper  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // mapper层
│     │ ├─CarouselMapper.xml
│     │ ├─GoodsCategoryMapper.xml
│     │ ├─IndexConfigMapper.xml
│     │ ├─MallUserAddressMapper.xml
│     │ ├─MallUserMapper.xml
│     │ ├─WebMallGoodsMapper.xml
│     │ ├─WebMallOrderAddressMapper.xml
│     │ ├─WebMallOrderItemMapper.xml
│     │ ├─WebMallOrderMapper.xml
│     │ ├─WebMallShoppingCartItemMapper.xml
│     │ └─WebMallUserTokenMapper.xml
│     ├─upload.zip
│     └─webmall_db_schema.sql  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - // 项目数据库初始化sql脚本
└─webmall-api.iml
```

## 接口文档

**!! 详情请看->**[在线接口文档](http://api.aruoxi.top:28019/swagger-ui.html)
![后端接口部分图(Swagger生成)](http://img.aruoxi.top/webmall-vue/WebMall-api-swagger.png)

## 页面展示

以下为 WebMall 商城 Vue 版本的页面预览：

- 登录页

![登录页](http://img.aruoxi.top/webmall-vue/%E7%99%BB%E5%BD%95.png)

- 首页

![首页](http://img.aruoxi.top/webmall-vue/%E9%A6%96%E9%A1%B5.png)

- 商品搜索

![商品搜索](http://img.aruoxi.top/webmall-vue/%E5%95%86%E5%93%81%E6%90%9C%E7%B4%A2.png)

- 商品详情页

![商品详情页](http://img.aruoxi.top/webmall-vue/%E5%95%86%E5%93%81%E8%AF%A6%E6%83%85.png)

- 购物车

![购物车](http://img.aruoxi.top/webmall-vue/%E8%B4%AD%E7%89%A9%E8%BD%A6.png)

- 生成订单

![生成订单](http://img.aruoxi.top/webmall-vue/%E7%94%9F%E6%88%90%E8%AE%A2%E5%8D%95.png)

- 地址管理

![地址管理](http://img.aruoxi.top/webmall-vue/%E5%9C%B0%E5%9D%80%E7%AE%A1%E7%90%86.png)

- 订单列表

![订单列表](http://img.aruoxi.top/webmall-vue/%E8%AE%A2%E5%8D%95%E5%88%97%E8%A1%A8.png)

- 订单详情

![订单详情](http://img.aruoxi.top/webmall-vue/%E8%AE%A2%E5%8D%95%E8%AF%A6%E6%83%85.png)
