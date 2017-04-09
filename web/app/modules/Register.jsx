import React from 'react';
import AjaxMixin from './comm/AjaxMixin.jsx';
import eventProxy from '../comm/eventProxy.js';

const Register = React.createClass({
    mixins: [AjaxMixin],
    render: function () {
        return (
            <div className="container">
                <div className="col-md-6 col-md-offset-3">
                    <div className="panel panel-primary">
                        <div className="panel-heading">
                            <div className="panel-title"><h3>用户注册</h3></div>
                        </div>
                        <div className="panel-body">
                            <div className="form-horizontal">
                                <div className="form-group">
                                    <label className="control-label col-md-2">用户名</label>
                                    <div className="col-md-10">
                                        <input type="text" className="form-control" placeholder="请输入用户名" ref="username" required/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label className="control-label col-md-2">邮箱</label>
                                    <div className="col-md-10">
                                        <div className="input-group">
                                            <input type="mail" className="form-control" placeholder="请使用公司邮箱获取验证码" ref="mail"></input>
                                            <span className="input-group-btn">
                                                <button className="btn btn-primary" type="button" onClick={this.handleSendVerification}>发送验证码</button>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label className="control-label col-md-2">验证码</label>
                                    <div className="col-md-10">
                                        <input type="text" className="form-control" placeholder="请输入公司邮箱中的验证码" ref="verification"></input>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label className="control-label col-md-2">密码</label>
                                    <div className="col-md-10">
                                        <input type="password" className="form-control" placeholder="请输入密码" ref="password"/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <label className="control-label col-md-2">确认密码</label>
                                    <div className="col-md-10">
                                        <input type="password" className="form-control" placeholder="请再次输入密码" ref="password2"/>
                                    </div>
                                </div>
                                <div className="form-group">
                                    <div className="col-md-12"><button className="btn btn-primary form-control" onClick={this.handleRegister}>注册</button></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    },
    handleSendVerification: function (e) {
        e.preventDefault();
        var mail = this.refs.mail.value;
        this.sendVerification(mail);
    },
    handleRegister: function(e){
        e.preventDefault();
        this.register({
            username:this.refs.username.value,
            password: this.refs.password.value,
            password2: this.refs.password2.value,
            verification: this.refs.verification.value,
            mail: this.refs.mail.value
        });
    },
    sendVerification: function(destination){
        var _self = this;
        this.ajax({
            service: 'verificationService',
            method: 'send',
            param: {
                type: 'register',
                method: 'mail',
                destination: destination
            },
            success: function(data){
                //获取系统响应码及信息
                var rtnCode = data['rtnCode'];
                var rtnMessage = data['rtnMessage'];
                var param = data['param'];

                if(rtnCode == _self.rtnCode.SUCCESS){
                    alert('发送验证码成功');
                }else{
                    alert(rtnMessage);
                }
            }
        })
    },
    register: function (registerInfo) {
        if(registerInfo.password != registerInfo.password2){
            alert('确认密码不一致');
            return false;
        }

        var _self = this;
        this.ajax({
            service: 'registerAjaxService',
            method: 'register',
            param: registerInfo,
            success: function(data){
                //获取系统响应码及信息
                var rtnCode = data['rtnCode'];
                var rtnMessage = data['rtnMessage'];
                var param = data['param'];

                if(rtnCode == _self.rtnCode.SUCCESS){
                    alert('注册成功');
                    eventProxy.emit('注册完成');
                }else{
                    alert(rtnMessage);
                }
            }
        })
    }
})

export default Register