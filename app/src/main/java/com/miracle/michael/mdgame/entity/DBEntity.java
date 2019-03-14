package com.miracle.michael.mdgame.entity;

public class DBEntity {


    /**
     * code : 1
     * msg : 请求成功
     * time : 1537528799
     * data : {"rflag":"1","rurl":"https://www.55355tt.com/","uflag":"0","uurl":""}
     */

    private int code;
    private String msg;
    private String time;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * rflag : 1
         * rurl : https://www.55355tt.com/
         * uflag : 0
         * uurl :
         */

        private int rflag;
        private String rurl;
        private int uflag;
        private String uurl;

        public int getRflag() {
            return rflag;
        }

        public void setRflag(int rflag) {
            this.rflag = rflag;
        }

        public String getRurl() {
            return rurl;
        }

        public void setRurl(String rurl) {
            this.rurl = rurl;
        }

        public int getUflag() {
            return uflag;
        }

        public void setUflag(int uflag) {
            this.uflag = uflag;
        }

        public String getUurl() {
            return uurl;
        }

        public void setUurl(String uurl) {
            this.uurl = uurl;
        }
    }
}
