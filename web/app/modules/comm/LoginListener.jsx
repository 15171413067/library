import React from 'react'
import eventProxy from '../../comm/eventProxy.js'
import AjaxMixin from './AjaxMixin.jsx'

const LoginListener = React.createClass({
    mixins: [AjaxMixin],
    render: function(){
        return null
    },
    componentDidMount: function () {
        eventProxy.on('注销',this.logout)
    },
    logout: function(){
        var _self = this;
        this.ajax({
            service: 'loginAjaxService',
            method: 'logout',
            success: function(data){
                //获取系统响应码及信息
                var rtnCode = data['rtnCode'];
                var rtnMessage = data['rtnMessage'];
                var param = data['param'];

                if(rtnCode == _self.rtnCode.SUCCESS){
                    sessionStorage.removeItem("user");
                    eventProxy.emit('注销完成');
                }else{
                    alert(rtnMessage);
                }
            }
        })
    }
})

export default LoginListener