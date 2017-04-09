import React from 'react'
import {withRouter} from 'react-router-dom'
import eventProxy from '../../comm/eventProxy.js'

const RedirectListener = withRouter(React.createClass({
    render: function () {
        return null
    },
    componentDidMount: function(){
        var history = this.props.history;
        eventProxy.on('登录',function(){
            history.push('/login')
        })
        eventProxy.on('登录完成',function(user){
            history.replace({pathname:'/home',state:{user: user}})
        })
        eventProxy.on('注销完成',function () {
            history.replace('/login')
        })
        eventProxy.on('注册',function(){
            history.push('/register')
        })
        eventProxy.on('注册完成',function () {
            history.replace('/login')
        })
    }
}))

export default RedirectListener