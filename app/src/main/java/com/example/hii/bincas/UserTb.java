package com.example.hii.bincas;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class UserTb {

    @NonNull
    @PrimaryKey
    private String timmid;
    private String contno;
    private String id;
    private String name;
    private String Addr;
    private String vtype;
    private String vno;

    @NonNull
    public String getTimmid() {
        return timmid;
    }

    public void setTimmid(@NonNull String timmid) {
        this.timmid = timmid;
    }

    public String getContno() {
        return contno;
    }

    public void setContno(@NonNull String contno) {
        this.contno = contno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }

    public String getVtype() {
        return vtype;
    }

    public void setVtype(String vtype) {
        this.vtype = vtype;
    }

    public String getVno() {
        return vno;
    }

    public void setVno(String vno) {
        this.vno = vno;
    }



}
