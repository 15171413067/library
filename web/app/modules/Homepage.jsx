import React from 'react'
import Header from './Header.jsx'
import Bookstore from './Bookstore.jsx'
import Footer from './Footer.jsx'

const Homepage = React.createClass({
    render: function(){
        return (
            <div className="Homepage container">
                <Header></Header>
                <Bookstore></Bookstore>
                <Footer></Footer>
            </div>
        )
    }
})

export default Homepage