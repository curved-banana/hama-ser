import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import {useAuth} from "./components/AuthContext";

import Header from "./components/Header";
import Main from "./components/Main";
import Login from "./components/Login";
import LoginError from "./components/LoginError";
import Auth from "./components/Auth";
//import  "./component/firebaseMessaging";
import Fcm from './components/firebaseMessaging';
import PasswordRest from './components/PasswordReset';
import AuthProvider from './components/AuthContext';
// function App(){  
//   return(
//     <div>
    
//     </div>
//   )

  
// }
function AuthenticatedRouter({children}){
  const authContext = useAuth();
  console.log(authContext.isAuthenticated);
  if(authContext.isAuthenticated){
    return(
      children
    )
  }

  return <Navigate to="/login"/>
}

function App() {
  return (
    <div>
      
    <AuthProvider >
      <BrowserRouter>
        <Header/>
        <Routes>  
          <Route path="/" element={<Main/>}/>
          <Route path="/login" element={<Login/>}/>
          <Route path="/loginError" element={<LoginError/>}/>
          <Route path="/auth" element={<Auth/>}/>
          
          <Route path="/fcm" element={
          <AuthenticatedRouter>
            <Fcm/>
          </AuthenticatedRouter>
          }></Route>

          <Route path="/PasswordReset" element={<PasswordRest/>}/>
          
        </Routes>
      </BrowserRouter>
    </AuthProvider>
    </div>
    
  );
}

export default App;
