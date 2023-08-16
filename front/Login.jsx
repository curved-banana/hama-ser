import React, {useState, useEffect} from "react";
import {useNavigate} from "react-router-dom";
import axios from 'axios';
import { useAuth } from './AuthContext';

function Login() {
  const [data, setData] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const baseUrl = "http://localhost:8088";

  const onLoginHandler = (e) => {
    setEmail(e.target.value);
  }

  
  const onPasswordHandler = (e) => {
    setPassword(e.target.value);
  }

  const authContext = useAuth();
  //console.log(authContext);
//   useEffect(()=>{
//     getByTest();
//   }, []);

//   const getByTest = async() => {
//     await axios.get(baseUrl + "/api/hello-world")
//     .then((res) => {
//       console.log(res);
//       setData(res.data);
//     })
//     .catch((err) => console.error(err));
//   }

  const navigate = useNavigate();

  const onSubmitHandler = (e) => {
    e.preventDefault();
    
    authContext.login()
    let sendData = JSON.stringify({
      "email" : email,
      "password" : password,
    });

    axios({
      method:"POST",
      url: baseUrl + "/api/login",
      data: sendData,
      headers: {"Content-type": "application/json"}
    })
    .then((res)=>{
      authContext.setAuthenticated(true);
      console.log(res.data);
      console.log(res.status);
      navigate("/");
      
    })
    .catch((err)=>{
      console.log(err);
      console.log(err.response.status);
      if(err.response.status === 401){
        navigate("/loginError");
      }
    })
   }

  return (
    <div className="App">
      <form onSubmit={onSubmitHandler}>
        <input type="text" value={email} onChange={onLoginHandler} name="email" placeholder="이메일 입력"/><br/>
        <input type="password" value={password} onChange={onPasswordHandler} name="password" placeholder="비밀번호 입력"/><br/>
        <input type="submit" value="전송"/>
      </form>
      <a href="http://localhost:8088/oauth2/authorization/google">구글 로그인 하기</a><br/>
      <a href="http://localhost:8088/oauth2/authorization/kakao">카카오 로그인 하기</a><br/>
      <a href="http://localhost:8088/oauth2/authorization/naver">네이버로 로그인 하기</a>
    </div>

  );
}

export default Login;
