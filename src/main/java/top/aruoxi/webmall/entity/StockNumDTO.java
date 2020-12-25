
package top.aruoxi.webmall.entity;

import lombok.Data;

/**
 * 库存修改所需实体
 */

@Data
public class StockNumDTO {
    private Long goodsId;

    private Integer goodsCount;
}
