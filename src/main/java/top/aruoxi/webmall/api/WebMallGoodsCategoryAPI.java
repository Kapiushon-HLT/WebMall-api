
package top.aruoxi.webmall.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import top.aruoxi.webmall.api.vo.WebMallIndexCategoryVO;
import top.aruoxi.webmall.common.WebMallException;
import top.aruoxi.webmall.common.ServiceResultEnum;
import top.aruoxi.webmall.service.WebMallCategoryService;
import top.aruoxi.webmall.util.Result;
import top.aruoxi.webmall.util.ResultGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "3.WebMall分类页面接口")
@RequestMapping("/api/v1")
public class WebMallGoodsCategoryAPI {

    @Resource
    private WebMallCategoryService webMallCategoryService;

    @GetMapping("/categories")
    @ApiOperation(value = "获取分类数据", notes = "分类页面使用")
    public Result<List<WebMallIndexCategoryVO>> getCategories() {
        List<WebMallIndexCategoryVO> categories = webMallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            WebMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(categories);
    }
}
