package com.yidou.wandou.example_11.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/13.
 */

public class News
{


    private String reason;

    private ResultBean result;
    private int error_code;

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public ResultBean getResult()
    {
        return result;
    }

    public void setResult(ResultBean result)
    {
        this.result = result;
    }

    public int getError_code()
    {
        return error_code;
    }

    public void setError_code(int error_code)
    {
        this.error_code = error_code;
    }

    public static class ResultBean
    {
        private String stat;

        private List<DataBean> data;

        public String getStat()
        {
            return stat;
        }

        public void setStat(String stat)
        {
            this.stat = stat;
        }

        public List<DataBean> getData()
        {
            return data;
        }

        public void setData(List<DataBean> data)
        {
            this.data = data;
        }


    }
}
