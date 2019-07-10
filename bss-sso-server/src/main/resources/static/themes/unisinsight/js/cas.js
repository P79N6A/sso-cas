function cambiar_login() {
    console.log("12342143")
    document.querySelector('.cont_forms').className = "cont_forms cont_forms_active_login";
    // document.querySelector('.cont_form_login').style.display = "block";
    // document.querySelector('.cont_form_sign_up').style.opacity = "0";

    // setTimeout(function () {
    //     document.querySelector('.cont_form_login').style.opacity = "1";
    // }, 400);

    // setTimeout(function () {
    //     document.querySelector('.cont_form_sign_up').style.display = "none";
    // }, 200);
}

function cambiar_sign_up(at) {
    document.querySelector('.cont_forms').className = "cont_forms cont_forms_active_sign_up";
    document.querySelector('.cont_form_sign_up').style.display = "block";
    document.querySelector('.cont_form_login').style.opacity = "0";

    setTimeout(function () {
        document.querySelector('.cont_form_sign_up').style.opacity = "1";
    }, 100);

    setTimeout(function () {
        document.querySelector('.cont_form_login').style.display = "none";
    }, 400);


}


function ocultar_login_sign_up() {


    document.querySelector('.cont_forms').className = "cont_forms";
    document.querySelector('.cont_form_sign_up').style.opacity = "0";
    document.querySelector('.cont_form_login').style.opacity = "0";

    setTimeout(function () {
        document.querySelector('.cont_form_sign_up').style.display = "none";
        document.querySelector('.cont_form_login').style.display = "none";
    }, 500);

}

/*
function getParamArgs(parms,parmName){
    var argIndex = parms.indexOf("?");
    var arg = parms.substring(argIndex+1);
    var valArg = "";
    args = arg.split("&");
    for(var i = 0;i<args.length;i++){
        str = args[i];
        var arg = str.split("=");
        if(arg.length<=1) continue;
        if(arg[0] == parmName)
           return  arg[1];
    }
    return valArg;
}

function submitReq(){
    var oriUrl = window.location.href;
    alert(oriUrl);
    var realUrl = decodeURIComponent(oriUrl);
    if (realUrl != null && realUrl.indexOf("auto") != -1) {
        var auto = getParamArgs(realUrl,"auto");
        if(auto == "true") {
            document.forms[0].submit();
        }
    }
}
submitReq();*/
