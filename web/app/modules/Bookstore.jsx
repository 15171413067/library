import React from 'react'
import AjaxMixin from './comm/AjaxMixin.jsx'
import eventProxy from '../comm/eventProxy.js'

const Bookstore = React.createClass({
    mixins: [AjaxMixin],
    getInitialState: function(){
        return {
            books: []
        }
    },
    componentDidMount: function(){
        var _self = this;
        _self.ajax({
            service: 'bookAjaxService',
            method: 'query',
            success: function(data){
                //获取系统响应码及信息
                var rtnCode = data['rtnCode'];
                var rtnMessage = data['rtnMessage'];
                var param = data['param'];

                if(rtnCode == _self.rtnCode.SUCCESS){
                    var books = param['books'];
                    _self.setState({
                        books: books
                    });
                }else{
                    alert(rtnMessage);
                }
            }
        })
    },
    render: function(){
        var _self = this;
        return (
            <div className="Bookstore">
                <div className="panel panel-default">
                    <div className="panel-heading"><h3 className="panel-title">书库</h3></div>
                    <table className="table">
                        <thead>
                            <tr>
                                <th>序号</th>
                                <th>书名</th>
                                <th>简介</th>
                                <th>主人</th>
                                <th>读者</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                        {this.state.books.map(function(book,index){
                            return (
                                <tr key={book.bookid}>
                                    <td>{index+1}</td>
                                    <td>{book.title}</td>
                                    <td>{book.description}</td>
                                    <td>{book.owner}</td>
                                    <td>{book.reader}</td>
                                    <td>{book.status}</td>
                                    <td>
                                        <button className="btn-default" onClick={_self.handleBorrow.bind(_self,book.bookid)}>借阅</button>
                                    </td>
                                </tr>
                            )
                        })}
                        </tbody>
                    </table>
                </div>
            </div>
        )
    },
    handleBorrow: function(bookid,e){
        this.borrow(bookid);
    },
    borrow: function(bookid){
        var _self = this;
        this.ajax({
            service: 'bookAjaxService',
            method: 'borrow',
            param: {
                bookid: bookid
            },
            success: function(data){
                //获取系统响应码及信息
                var rtnCode = data['rtnCode'];
                var rtnMessage = data['rtnMessage'];
                var param = data['param'];

                if(rtnCode == _self.rtnCode.SUCCESS){
                    _self.componentDidMount();
                }else if(rtnCode == _self.rtnCode.NOLOGIN){
                    alert('未登录');
                    eventProxy.emit('登录');
                }else{
                    alert(rtnMessage);
                }
            }
        })
    }
})

export default Bookstore