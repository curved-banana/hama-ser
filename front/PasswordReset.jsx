import React from 'react';

function PasswordReset() {
    

    return(

        <div>
            <form>
                <input type="text" placeholder="새 비밀번호를 입력해주세요"></input><br/>
                <input type='text' placeholder="새 비밀번호를 재입력해주세요"></input><br/>
                <input type="submit" value="다음"></input>
            </form>
        </div>
    )
}

export default PasswordReset;