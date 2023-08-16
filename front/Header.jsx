import React from 'react';
import {Link} from 'react-router-dom';
import {useAuth} from './AuthContext';

function Header(){

    const authContext = useAuth();
    const isAuthenticated = authContext.isAuthenticated;

    function logout(){
        authContext.logout();
    }
    return(
        <div style={{display:"flex", justifyContent:"end",}}>
            {!isAuthenticated &&
            <div>
                <Link to="/login" >로그인</Link>
                <span>|</span>
                <Link to="/auth" >회원가입</Link>
            </div>}
            { isAuthenticated &&
            <div>
               <Link to="/login" onClick={logout}>로그아웃</Link>
           </div>}

            
        </div>
    )

}

export default Header;

//style={{textDecorationLine:"none", color:"black",}