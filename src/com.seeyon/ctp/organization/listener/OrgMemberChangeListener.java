package com.seeyon.ctp.organization.listener;

import com.seeyon.ctp.common.exceptions.BusinessException;
import com.seeyon.ctp.common.security.MessageEncoder;
import com.seeyon.ctp.organization.bo.V3xOrgMember;
import com.seeyon.ctp.organization.event.AddMemberEvent;
import com.seeyon.ctp.organization.event.DeleteMemberEvent;
import com.seeyon.ctp.organization.event.MemberUpdateDeptEvent;
import com.seeyon.ctp.organization.event.UpdateMemberEvent;
import com.seeyon.ctp.organization.util.CommonUtil;
import com.seeyon.ctp.organization.util.DBHelper;
import com.seeyon.ctp.util.annotation.ListenEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrgMemberChangeListener {

    /**
     * 添加用户监听
     * @param event
     * @throws BusinessException
     */
    @ListenEvent(event = AddMemberEvent.class, async = true)
    public void doAddMember(AddMemberEvent event) throws Exception {
        executeSql(event.getMember(),"1");
    }

    /**
     * 更新用户监听
     * @param event
     * @throws BusinessException
     */
    @ListenEvent(event = UpdateMemberEvent.class, async = true)
    public void doUpdateMember(UpdateMemberEvent event) throws Exception {
        executeSql(event.getMember(),"2");
    }


    /**
     * 删除用户监听
     * @param event
     * @throws BusinessException
     */
    @ListenEvent(event = DeleteMemberEvent.class, async = true)
    public void doDeleteMember(DeleteMemberEvent event) throws Exception {
        executeSql(event.getMember(),"3");
    }




    private static void executeSql(V3xOrgMember member,String type) throws Exception {//type:1插入 2更新 3删除
        DBHelper db1 = null;
        ResultSet ret = null;

        String username=member.getLoginName();
        String pwd=member.getPassword();
        //BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        MessageEncoder encoder= new MessageEncoder();
        String password=encoder.encode(username,pwd);//oa密码加密
        String email=member.getEmailAddress();
        String phone=member.getTelNumber();

        Date currdate=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String create_time=df.format(currdate);//获得当前时间

        String id=CommonUtil.generateID();

        String guid= member.getId().toString();
        String sqlstr="";
        switch(type){
            case "1":sqlstr="insert user_(id,guid,email,password,phone,create_time,username,archived) values('"+id+"','"+guid+"','"+email+"','"+password+"','"+phone+"','"+create_time+"','"+username+"','0')";break;
            case "2":sqlstr="update user_ set email='"+email+"',password='"+password+"',phone='"+phone+"',username='"+username+"',archived='0' where guid='"+guid+"'";break;
            case "3":sqlstr="delete from user_  where guid='"+guid+"'";break;
            default:;break;
        }
        db1 = new DBHelper(sqlstr);
        try {
            db1.pst.executeUpdate(sqlstr);//插入user_表
            if(type.equals("1")){
                String prisql="insert into user_privilege(user_id,privilege) values ('"+id+"','UNITY')";
                db1.pst.executeUpdate(prisql);//user_privilege
                //System.out.println("privsql===="+prisql);
            }
            //System.out.println("sqlstr==="+sqlstr);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ret.close();
            db1.close();//关闭连接
        }
    }




    public static void main(String[] args) {
       /* V3xOrgMember member=new V3xOrgMember();
        try{
            executeSql(member,"1");
        }catch (Exception e){
            e.printStackTrace();
        }*/
        DBHelper db1 = null;
        ResultSet ret = null;
        try {
            // MessageEncoder encoder=new MessageEncoder();
            String username="admin";
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String password=encoder.encode(username);//密码加密
            String email="test0021@qq.com";
            System.out.println("===============pwd==========="+password);
            String phone="123456";
            String create_time="2020-11-09";
            String guid="0000021";

          /*  String insertsql="insert user_(guid,email,password,phone,username) values('"+guid+"','"+email+"','"+password+"','"+phone+"','"+username+"')";
            db1 = new DBHelper(insertsql);//创建DBHelper对象
            db1.pst.executeUpdate(insertsql);*/

            ret.close();
            db1.close();//关闭连接
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
