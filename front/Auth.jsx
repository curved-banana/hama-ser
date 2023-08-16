import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import axios from 'axios';


function Auth(){

    const[nickname, setNickname] = useState("");
    const[email, setEmail] = useState("");
    const[password, setPassword] = useState("");

    const baseUrl = "http://localhost:8088";

    let navigate = useNavigate();


    const onNicknameHandler = (e) => {
        setNickname(e.target.value);
    }


    const onEmailHandler = (e) => {
        setEmail(e.target.value);
    }

    const onPasswordHandler = (e) => {
        setPassword(e.target.value);
    }

    const onSubmitHandler = (e) => {
        e.preventDefault();
    
        let sendData = JSON.stringify({
            "nickname" : nickname,
            "email" : email,
            "password" : password,
        });
    
        axios({
          method:"POST",
          url: baseUrl + "/api/register",
          data: sendData,
          headers: {"Content-type": "application/json"}
        })
        .then((res)=>{
          console.log(res.data);
          console.log(res.status);
          navigate("/login");
          
        })
        .catch((err)=>{
          console.log(err);
        })
      }

    return(
        <div>
            <form onSubmit={onSubmitHandler}>
                <input type="text" value={nickname} placeholder='닉네임을 입력하세요' onChange={onNicknameHandler}></input><br/>
                <input type="text" value={email} placeholder='이메일을 입력하세요' onChange={onEmailHandler}></input><br/>
                <input type="password" value={password} placeholder='비밀번호를 입력하세요' onChange={onPasswordHandler}></input><br/>
                <input type="submit" value="전송"></input>
            </form>
        </div>
    )
}

export default Auth;