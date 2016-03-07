package com.xuewen.kidsbook.utils;

/**
 * Created by lker_zy on 16-3-6.
 */
public class VersionUpdateJsonBean {
    private String errno;
    private String needUpdate;
    private String version;
    private String verDesc;

    public int getErrno() {
        return errno.equals("0") ? 0 : -1;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public boolean isNeedUpdate() {
        return needUpdate.equals("true") ? true : false;
    }

    public void setNeedUpdate(String needUpdate) {
        this.needUpdate = needUpdate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVerDesc() {
        return verDesc;
    }

    public void setVerDesc(String verDesc) {
        this.verDesc = verDesc;
    }
}
