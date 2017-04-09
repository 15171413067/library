import React from 'react';
import {HashRouter as Router,Route,Redirect} from 'react-router-dom';
import Homepage from './modules/Homepage.jsx';
import Login from './modules/Login.jsx';
import RedirectListener from './modules/comm/RedirectListener.jsx';
import LoginListener from './modules/comm/LoginListener.jsx'
import Register from './modules/Register.jsx'

const App = React.createClass({
    render: function(){
        return (
            <Router>
                <div>
                    <Route exact path="/" render={() => (
                        <Redirect to="/home"></Redirect>
                    )}></Route>
                    <Route path="/home" component={Homepage}></Route>
                    <Route path="/login" component={Login}></Route>
                    <Route path="/register" component={Register}></Route>
                    <RedirectListener></RedirectListener>
                    <LoginListener></LoginListener>
                </div>
            </Router>
        )
    }
})

export default App