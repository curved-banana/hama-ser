import React from 'react';
import {initializeApp} from 'firebase/app';
import {getMessaging, getToken, onMessage} from 'firebase/messaging';
import axios from 'axios';

function Fcm(){
    const baseUrl = "http://localhost:8088";
    
    const firebaseConfig = {
        apiKey: "AIzaSyC8gwrfFBYzj1VgFj4BbKqkpg8-eO_DncU",
        authDomain: "hamahama-sku20230818.firebaseapp.com",
        projectId: "hamahama-sku20230818",
        storageBucket: "hamahama-sku20230818.appspot.com",
        messagingSenderId: "97022055548",
        appId: "1:97022055548:web:8ad8c37b2af5c26a1f65f9",
        measurementId: "G-014B3WDR3L"
    };


    function requestPermission(e){
        e.currentTarget.disabled = true;
        console.log("Requesting permission...");
        if(!"Notification" in window){
            console.log("데스크톱 알림을 지원하지 않는 브라우저입니다.");
        }
        Notification.requestPermission().then((permission) => {
            if(permission === 'granted'){
                console.log("Notification permission granted.");

                const app = initializeApp(firebaseConfig);
                const messaging = getMessaging();

                console.log(messaging);

                getToken(messaging, {
                    vapidKey: "BOfIF-8CVjIqkjkxh_hMij9KBMEbMVjoua-Ras7kKRvSutwP7GlFlC6__bMGdIyrQf-t1mQfxLXbQydpI7_eLfc",
                })
                .then((currentToken) => {
                    if(currentToken){
                        console.log("current Token: " + currentToken);
                        
                        let sendData = JSON.stringify({
                            "email" : "회원 이메일",
                            "fcmToken" : currentToken,
                            "fcmStatus" : 1,
                          });
                      
                        axios({
                            method:"POST",
                            url: baseUrl + "/api/user/saveFcmToken",
                            data: sendData,
                            headers: {"Content-type": "application/json"}
                          })
                          .then((res)=>{
                            console.log(res.data);
                          })
                          .catch((err)=>{
                            console.log(err);
                            console.log(err);
                          })
                    }
                    else{
                        console.log("No registration token available. Request permission to generate one.");

                    }
                })
                .catch((err)=>{
                    console.log("An error occurred while retrieving token" + err)
                })

            

            }else{
                console.log("Don't have permission");
            }
        })
    }


    return(
        <div>
            <button onClick={requestPermission} >알림 설정</button>
        </div>
    )
}

export default Fcm;
