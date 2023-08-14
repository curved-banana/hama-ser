// resources/static/firebase-message-sw.js
import Scripts('https://www.gstatic.com/firebasejs/4.8.1/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/4.8.1/firebase-messaging.js');
// Initialize Firebase
var config = {
    apiKey: "AIzaSyC8gwrfFBYzj1VgFj4BbKqkpg8-eO_DncU",
    authDomain: "hamahama-sku20230818.firebaseapp.com",
    projectId: "hamahama-sku20230818",
    storageBucket: "hamahama-sku20230818.appspot.com",
    messagingSenderId: "97022055548",
    appId: "1:97022055548:web:8ad8c37b2af5c26a1f65f9",
    measurementId: "G-014B3WDR3L"
};
firebase.initializeApp(config);
const messaging = firebase.messaging();
messaging.setBackgroundMessageHandler(function (payload) {
    const title = "Hello World";
    const options = { body: payload.data.status };
    return self.registration.showNotification(title, options);
});