/**
 * Http 请求封装
 */
const HttpHelper = cc.Class({
    extends: cc.Component,

    statics: {
    },

    properties: {

    },

    /**
     * get请求
     * @param {string} url 
     * @param {function} callback 
     */
    httpGet(url, callback) {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && (xhr.status >= 200 && xhr.status < 400)) {
                var response = xhr.responseText;
                console.log(response);
            }
        };
        xhr.open("GET", url, true);
        xhr.timeout = 8000;// 8 seconds for timeout
        xhr.send();
    },

    /**
     * post请求
     * @param {string} url 
     * @param {object} params 
     * @param {function} callback 
     */
    httpPost(url, params, callback) {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && (xhr.status >= 200 && xhr.status < 400)) {
                var response = xhr.responseText;
                console.log(response);
                let rsp = JSON.parse(response);
                callback(rsp);
            }
        };
        xhr.open("POST", url, true);
        xhr.timeout = 8000;// 8 seconds for timeout
        if (params != null){
            console.log(params)
            xhr.setRequestHeader("Content-Type","application/json")
            xhr.send(JSON.stringify(params));
        }else{
            xhr.send();
        }
        
    },
    

    /**
     * 登录专用
     * @param {string} url 
     * @param {object} params 
     * @param {function} callback 
     * @param {string} account 
     * @param {string} password 
     */
    httpPostLogin(url, params, callback, account, password) {
        // cc.myGame.gameUi.onShowLockScreen();
        let xhr = cc.loader.getXMLHttpRequest();
        xhr.onreadystatechange = function () {
            // cc.log('xhr.readyState=' + xhr.readyState + '  xhr.status=' + xhr.status);
            if (xhr.readyState === 4 && xhr.status == 200) {
                let respone = xhr.responseText;
                let rsp = JSON.parse(respone);
                // cc.myGame.gameUi.onHideLockScreen();
                callback(rsp);
            } else {
                callback(-1);
            }
        };
        xhr.open('POST', url, true);
        xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
        xhr.setRequestHeader('Access-Control-Allow-Methods', 'GET, POST');
        xhr.setRequestHeader('Access-Control-Allow-Headers', 'x-requested-with,content-type');
        xhr.setRequestHeader("Content-Type", "application/json");
        let str = account + "@" + password;
        xhr.setRequestHeader('Authorization', 'Basic' + ' ' + window.btoa(str));

        xhr.timeout = 8000;// 8 seconds for timeout

        xhr.send(JSON.stringify(params));

    }
});

window.HttpHelper = new HttpHelper();