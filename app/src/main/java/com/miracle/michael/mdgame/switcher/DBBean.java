package com.miracle.michael.mdgame.switcher;

public class DBBean {


    /**
     * code : 1
     * msg : 请求成功
     * time : 1537528799
     * data : {"rflag":"1","rurl":"https://www.55355tt.com/","uflag":"0","uurl":""}
     */

    private int status;
    private String msg;
    private DataBean info;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getInfo() {
        return info;
    }

    public void setInfo(DataBean info) {
        this.info = info;
    }

    public static class DataBean {
        /**
         * rflag : 1
         * rurl : https://www.55355tt.com/
         * uflag : 0
         * uurl :
         */

        private int code;
        private String url;
        private String must_url;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMust_url() {
            return must_url;
        }

        public void setMust_url(String must_url) {
            this.must_url = must_url;
        }
    }
}
