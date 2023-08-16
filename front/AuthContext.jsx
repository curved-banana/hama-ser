import {createContext, useState, useContext} from 'react';
import axios from 'axios';

export const AuthContext = createContext({});

export const useAuth = () => useContext(AuthContext);

const AuthProvider = ({children}) => {
    //const [auth, setAuth] = useState({});
    const baseUrl = "http://localhost:8088";

    const[number, setNumber] = useState(0);

    let[isAuthenticated, setAuthenticated] = useState(false);

   function login (email, password){
        if(email === "play5555@naver.com" && password === "5555"){
            setAuthenticated(true);
            return true;
        }else{
            setAuthenticated(false);
            return false;
        }
    }

    function logout(){
        setAuthenticated(false);
    }
    return(
        <AuthContext.Provider value={{number, isAuthenticated, setAuthenticated, login, logout}}>
            {children}
        </AuthContext.Provider>
    )
}

export default AuthProvider;