
package top.aruoxi.webmall.service.impl;

import top.aruoxi.webmall.api.param.SaveCartItemParam;
import top.aruoxi.webmall.api.param.UpdateCartItemParam;
import top.aruoxi.webmall.common.Constants;
import top.aruoxi.webmall.common.WebMallException;
import top.aruoxi.webmall.common.ServiceResultEnum;
import top.aruoxi.webmall.api.vo.WebMallShoppingCartItemVO;
import top.aruoxi.webmall.dao.WebMallGoodsMapper;
import top.aruoxi.webmall.dao.WebMallShoppingCartItemMapper;
import top.aruoxi.webmall.entity.WebMallGoods;
import top.aruoxi.webmall.entity.WebMallShoppingCartItem;
import top.aruoxi.webmall.service.WebMallShoppingCartService;
import top.aruoxi.webmall.util.BeanUtil;
import top.aruoxi.webmall.util.PageQueryUtil;
import top.aruoxi.webmall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WebMallShoppingCartServiceImpl implements WebMallShoppingCartService {

    @Autowired
    private WebMallShoppingCartItemMapper webMallShoppingCartItemMapper;

    @Autowired
    private WebMallGoodsMapper webMallGoodsMapper;

    @Override
    public String saveNewBeeMallCartItem(SaveCartItemParam saveCartItemParam, Long userId) {
        WebMallShoppingCartItem temp = webMallShoppingCartItemMapper.selectByUserIdAndGoodsId(userId, saveCartItemParam.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            WebMallException.fail(ServiceResultEnum.SHOPPING_CART_ITEM_EXIST_ERROR.getResult());
        }
        WebMallGoods webMallGoods = webMallGoodsMapper.selectByPrimaryKey(saveCartItemParam.getGoodsId());
        //商品为空
        if (webMallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = webMallShoppingCartItemMapper.selectCountByUserId(userId);
        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() < 1) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_NUMBER_ERROR.getResult();
        }        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        WebMallShoppingCartItem webMallShoppingCartItem = new WebMallShoppingCartItem();
        BeanUtil.copyProperties(saveCartItemParam, webMallShoppingCartItem);
        webMallShoppingCartItem.setUserId(userId);
        //保存记录
        if (webMallShoppingCartItemMapper.insertSelective(webMallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateNewBeeMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId) {
        WebMallShoppingCartItem webMallShoppingCartItemUpdate = webMallShoppingCartItemMapper.selectByPrimaryKey(updateCartItemParam.getCartItemId());
        if (webMallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (!webMallShoppingCartItemUpdate.getUserId().equals(userId)) {
            WebMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        //超出单个商品的最大数量
        if (updateCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        webMallShoppingCartItemUpdate.setGoodsCount(updateCartItemParam.getGoodsCount());
        webMallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (webMallShoppingCartItemMapper.updateByPrimaryKeySelective(webMallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public WebMallShoppingCartItem getNewBeeMallCartItemById(Long newBeeMallShoppingCartItemId) {
        WebMallShoppingCartItem webMallShoppingCartItem = webMallShoppingCartItemMapper.selectByPrimaryKey(newBeeMallShoppingCartItemId);
        if (webMallShoppingCartItem == null) {
            WebMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return webMallShoppingCartItem;
    }

    @Override
    public Boolean deleteById(Long newBeeMallShoppingCartItemId) {
        return webMallShoppingCartItemMapper.deleteByPrimaryKey(newBeeMallShoppingCartItemId) > 0;
    }

    @Override
    public List<WebMallShoppingCartItemVO> getMyShoppingCartItems(Long newBeeMallUserId) {
        List<WebMallShoppingCartItemVO> webMallShoppingCartItemVOS = new ArrayList<>();
        List<WebMallShoppingCartItem> webMallShoppingCartItems = webMallShoppingCartItemMapper.selectByUserId(newBeeMallUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        return getNewBeeMallShoppingCartItemVOS(webMallShoppingCartItemVOS, webMallShoppingCartItems);
    }

    @Override
    public List<WebMallShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long newBeeMallUserId) {
        List<WebMallShoppingCartItemVO> webMallShoppingCartItemVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(cartItemIds)) {
            WebMallException.fail("购物项不能为空");
        }
        List<WebMallShoppingCartItem> webMallShoppingCartItems = webMallShoppingCartItemMapper.selectByUserIdAndCartItemIds(newBeeMallUserId, cartItemIds);
        if (CollectionUtils.isEmpty(webMallShoppingCartItems)) {
            WebMallException.fail("购物项不能为空");
        }
        if (webMallShoppingCartItems.size() != cartItemIds.size()) {
            WebMallException.fail("参数异常");
        }
        return getNewBeeMallShoppingCartItemVOS(webMallShoppingCartItemVOS, webMallShoppingCartItems);
    }

    /**
     * 数据转换
     *
     * @param webMallShoppingCartItemVOS
     * @param webMallShoppingCartItems
     * @return
     */
    private List<WebMallShoppingCartItemVO> getNewBeeMallShoppingCartItemVOS(List<WebMallShoppingCartItemVO> webMallShoppingCartItemVOS, List<WebMallShoppingCartItem> webMallShoppingCartItems) {
        if (!CollectionUtils.isEmpty(webMallShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> newBeeMallGoodsIds = webMallShoppingCartItems.stream().map(WebMallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<WebMallGoods> webMallGoods = webMallGoodsMapper.selectByPrimaryKeys(newBeeMallGoodsIds);
            Map<Long, WebMallGoods> newBeeMallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(webMallGoods)) {
                newBeeMallGoodsMap = webMallGoods.stream().collect(Collectors.toMap(WebMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (WebMallShoppingCartItem webMallShoppingCartItem : webMallShoppingCartItems) {
                WebMallShoppingCartItemVO webMallShoppingCartItemVO = new WebMallShoppingCartItemVO();
                BeanUtil.copyProperties(webMallShoppingCartItem, webMallShoppingCartItemVO);
                if (newBeeMallGoodsMap.containsKey(webMallShoppingCartItem.getGoodsId())) {
                    WebMallGoods webMallGoodsTemp = newBeeMallGoodsMap.get(webMallShoppingCartItem.getGoodsId());
                    webMallShoppingCartItemVO.setGoodsCoverImg(webMallGoodsTemp.getGoodsCoverImg());
                    String goodsName = webMallGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    webMallShoppingCartItemVO.setGoodsName(goodsName);
                    webMallShoppingCartItemVO.setSellingPrice(webMallGoodsTemp.getSellingPrice());
                    webMallShoppingCartItemVOS.add(webMallShoppingCartItemVO);
                }
            }
        }
        return webMallShoppingCartItemVOS;
    }

    @Override
    public PageResult getMyShoppingCartItems(PageQueryUtil pageUtil) {
        List<WebMallShoppingCartItemVO> webMallShoppingCartItemVOS = new ArrayList<>();
        List<WebMallShoppingCartItem> webMallShoppingCartItems = webMallShoppingCartItemMapper.findMyNewBeeMallCartItems(pageUtil);
        int total = webMallShoppingCartItemMapper.getTotalMyNewBeeMallCartItems(pageUtil);
        PageResult pageResult = new PageResult(getNewBeeMallShoppingCartItemVOS(webMallShoppingCartItemVOS, webMallShoppingCartItems), total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
