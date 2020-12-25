
package top.aruoxi.webmall.dao;

import top.aruoxi.webmall.entity.WebMallOrderAddress;

public interface WebMallOrderAddressMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(WebMallOrderAddress record);

    int insertSelective(WebMallOrderAddress record);

    WebMallOrderAddress selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(WebMallOrderAddress record);

    int updateByPrimaryKey(WebMallOrderAddress record);
}