import React from 'react';
import eventProxy from '../comm/eventProxy.js'

const Header = React.createClass({
    getInitialState: function () {
        return {

        }
    },
    componentDidMount: function(){
        var user = sessionStorage.getItem("user");
        if(user){
            this.setState({
                user: JSON.parse(user)
            })
        }
    },
    render: function(){
        return (
            <div className="Header jumbotron">
                <h1>Library</h1>
                <p className="pull-right">开发二组</p>
                <p className="">{this.state.user?(<span>{this.state.user.realname}<button className="btn btn-default" onClick={this.handleLogout}>注销</button></span>):<button className="btn btn-default" onClick={this.handleLogin}>登录</button>}</p>
            </div>
        )
    },
    handleLogin: function(){
        eventProxy.emit('登录');
    },
    handleLogout: function(){
        eventProxy.emit('注销');
    }
})

export default Header