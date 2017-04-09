import React from 'react'
import AjaxMixin from './comm/AjaxMixin.jsx'
import eventProxy from '../comm/eventProxy.js'
import './Login.css';

const Login = React.createClass({
    mixins: [AjaxMixin],
    render: function(){
        return (
            <div className="Login container">
                <div className="col-md-6 col-md-offset-3">
                    <div className="panel panel-primary ">
                        <div className="panel-heading">
                            <div className="panel-title"><h3>用户登录</h3></div>
                        </div>
                        <div className="panel-body">
                            <form className="form-horizontal">
                                <div className="form-group">
                                    <label className="col-md-2 control-label">用户名</label>
                                    <div className="col-md-10"><input className="form-control" type="text" ref="username"/></div>
                                </div>
                                <div className="form-group">
                                    <label className="col-md-2 control-label">密码</label>
                                    <div className="col-md-10"><input className="form-control" type="password" ref="password"/></div>
                                </div>
                                <div className="form-group">
                                    <div className="col-md-12"><button className="btn btn-primary form-control" onClick={this.handleLogin}>登录</button></div>
                                </div>
                                <div className="form-group">
                                    <div className="col-md-12"><button className="btn btn-link" onClick={this.handleRegister}>注册新用户</button></div>
                                </div>
                                <input type="text" style={{display: 'none'}}/>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        )
    },
    handleLogin: function (e) {
        e.preventDefault();
        var username = this.refs.username.value;
        var password = this.refs.password.value;
        this.login(username,password);
    },
    handleRegister: function (e) {
        e.preventDefault();
        eventProxy.emit('注册');
    },
    login: function(username,password){
        var _self = this;
        this.ajax({
            service: 'loginAjaxService',
            method: 'login',
            param: {
                username: username,
                password: password
            },
            success: function(data){
                //获取系统响应码及信息
                var rtnCode = data['rtnCode'];
                var rtnMessage = data['rtnMessage'];
                var param = data['param'];

                if(rtnCode == _self.rtnCode.SUCCESS){
                    var user = param;
                    sessionStorage.setItem("user",JSON.stringify(user));
                    eventProxy.emit('登录完成',user)
                }else{
                    alert(rtnMessage);
                }
            }
        })
    }
})

export default Login