
package top.aruoxi.webmall.common;

public class WebMallException extends RuntimeException {

    public WebMallException() {
    }

    public WebMallException(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     *
     * @param message
     */
    public static void fail(String message) {
        throw new WebMallException(message);
    }

}
