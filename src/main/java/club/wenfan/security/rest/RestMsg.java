package club.wenfan.security.rest;

/**
 * @author:wenfan
 * @description:
 * @data: 2019/2/27 17:03
 */
public enum RestMsg {

    SUCCESS(1, "操作成功"),

    FAILURE(0, "操作失败"),

    OK(200, "OK"),

    FAIL(500, "内部服务器错误"),

    BAD_REQUEST(400, "没有该资源"),

    ERROR_PARAMETER(401, "错误参数");


    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    RestMsg(Integer code, String messgage) {

        this.code = code;
        this.message = messgage;
    }

    public static Integer getCode(String name) {
        for (RestMsg restMsg : RestMsg.values()) {
            if (restMsg.getMessage().equals(name)) {
                return restMsg.code;
            }
        }
        return null;
    }


    public static String getMessage(Integer id) {
        for (RestMsg resultCode : RestMsg.values()) {
            if (resultCode.getCode().equals(id)) {
                return resultCode.message;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
