package com.seeyon.ctp.organization.listener;

import com.seeyon.ctp.common.security.MessageEncoder;
import com.seeyon.ctp.organization.bo.V3xOrgMember;
import com.seeyon.ctp.organization.event.ChangePwdEvent;
import com.seeyon.ctp.organization.util.DBHelper;
import com.seeyon.ctp.util.annotation.ListenEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModPasswordListener {

    @ListenEvent(event = ChangePwdEvent.class, async = true)
    public void doUpdatePassword(ChangePwdEvent event) throws Exception {
        DBHelper db1 = null;
        ResultSet ret = null;

        V3xOrgMember member=event.getMember();


        String username=member.getLoginName();
        String pwd=member.getPassword();
        MessageEncoder encoder= new MessageEncoder();
        String password=encoder.encode(username,pwd);//oa密码加密
        String guid=member.getId().toString();

        String sqlstr="update user_ set password='"+password+"' where guid='"+guid+"'";

        db1 = new DBHelper(sqlstr);
        try {
            db1.pst.executeUpdate(sqlstr);//更新user_表中的password
            //System.out.println("sqlstr==="+sqlstr);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ret.close();
            db1.close();//关闭连接
        }
    }
}


