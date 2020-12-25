
package top.aruoxi.webmall.common;


public enum WebMallCategoryLevelEnum {

    DEFAULT(0, "ERROR"),
    LEVEL_ONE(1, "一级分类"),
    LEVEL_TWO(2, "二级分类"),
    LEVEL_THREE(3, "三级分类");

    private int level;

    private String name;

    WebMallCategoryLevelEnum(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public static WebMallCategoryLevelEnum getNewBeeMallOrderStatusEnumByLevel(int level) {
        for (WebMallCategoryLevelEnum webMallCategoryLevelEnum : WebMallCategoryLevelEnum.values()) {
            if (webMallCategoryLevelEnum.getLevel() == level) {
                return webMallCategoryLevelEnum;
            }
        }
        return DEFAULT;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
