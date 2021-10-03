package tech.whaleeye.misc.ajax;

import com.alibaba.fastjson.JSON;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class AjaxResult implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public AjaxResult(ResultType resultType) {
        this.code = resultType.code;
        this.msg = resultType.defaultMsg;
    }

    public static AjaxResult setSuccess(boolean success) {
        if (success) {
            return new AjaxResult(ResultType.SUCCESS);
        } else {
            return new AjaxResult(ResultType.FAIL);
        }
    }

    public AjaxResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public AjaxResult setCode(int code) {
        this.code = code;
        return this;
    }

    public AjaxResult setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public enum ResultType {
        SUCCESS(200, "Success"),
        FAIL(400, "Fail"),
        INTERNAL_ERROR(500, "Server Internal Error"),
        UNAUTHORIZED(401, "Unauthorized"),
        NOT_FOUND(404, "Resource Not Found");

        public int code;
        public String defaultMsg;

        ResultType(int code, String defaultMsg) {
            this.code = code;
            this.defaultMsg = defaultMsg;
        }
    }
}
