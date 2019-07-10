$(function () {
    $(".middle").css("margin-top", ($(".cotn_principal").height() - 750) > 0 ? ($(".cotn_principal").height() - 750) / 2 : 0);
    $(window).resize(function () {
        $(".middle").css("margin-top", ($(".cotn_principal").height() - 750) > 0 ? ($(".cotn_principal").height() - 750) / 2 : 0);
    });
    $("#code").val(""); // 每次返回或刷新情况验证码输入框
    //判断浏览器是否支持placeholder属性
    var supportPlaceholder = 'placeholder' in document.createElement('input')
    var
        browserTitle = config.browserTitle,
        title = config.title,
        footText = config.footText,
        browserTitleLogo = config.browserTitleLogo,
        logo = config.logo,
        logoBackgroundImage = config.logoBackgroundImage,
        backgroundImage = config.backgroundImage,
        buttonColor = config.buttonColor,
        animationOpen = config.animationOpen,
        haloOpen = config.haloOpen,
        usernameImage = config.usernameImage,
        usernamePlaceholder = config.usernamePlaceholder,
        passwordImage = config.passwordImage,
        passwordPlaceholder = config.passwordPlaceholder,
        isIdentifyingCodeText = config.isIdentifyingCodeText
        ;
    browserTitle && $("title").html(browserTitle);
    title && $(".title").html(title);
    footText && $(".footer").html(footText);
    browserTitleLogo && $('head').append('<link href="./' + browserTitleLogo + '" rel="shortcut icon" type="image/x-icon" />');
    logo && $(".logo").css("background-image", "url(./" + logo + ")");
    logoBackgroundImage && $(".logoBg").css("background-image", "url(./" + logoBackgroundImage + ")");
    backgroundImage && $(".cotn_principal").css("background-image", "url(./" + backgroundImage + ")");
    animationOpen && animation();
    haloOpen && $(".vignette").css("background-image", "-webkit-radial-gradient(50% 50%, ellipse, rgba(0,0,0,0) 40%, rgba(0,0,0,1) 100%)");
    buttonColor && $(".btn_login").css("background", buttonColor);
    isIdentifyingCodeText && $("#identifyingCodeText").css("display","block");
    usernameImage && $(".usernameImage").css("background-image", "url(./" + usernameImage + ")");
    usernamePlaceholder && $(".usernameImage").attr("title", usernamePlaceholder);
    usernameImage && $(".passwordImage").css("background-image", "url(./" + passwordImage + ")");
    passwordPlaceholder && $(".passwordImage").attr("title", passwordPlaceholder);
    if (supportPlaceholder) {
        usernamePlaceholder && $("#username").attr("placeholder", usernamePlaceholder);
        passwordPlaceholder && $("#password").attr("placeholder", passwordPlaceholder);
    } else {
        $(".placeholder").css("display", "block");
        $(".loginInput").each(function () {
            if ($(this).val().length > 0) {
                $(this).siblings(".placeholder").css("display", "none");
            } else {
                $(this).siblings(".placeholder").css("display", "block");
            }
        });
        $(".loginInput").bind('input propertychange keyup', function () {
            if ($(this).val().length > 0) {
                $(this).siblings(".placeholder").css("display", "none");
            } else {
                $(this).siblings(".placeholder").css("display", "block");
            }
        });
    }
    $(".showHidden").click(function () {
        if ($("#password").attr("type") === "password") {
            $("#password").attr("type", "text")
            $(this).css("background-image", "url(themes/unisinsight/images/eyeOpen.png)");
        } else {
            $("#password").attr("type", "password")
            $(this).css("background-image", "url(themes/unisinsight/images/eyeClose.png)");
        }
        setTimeout(function () {
            var data = $("input:-webkit-autofill");
            data.each(function (i, obj) {
                $(obj).removeClass("input:-webkit-autofill");
                obj.value = obj.value;
            });
        }, 1);
    });
    $(".placeholder").click(function () {
        $(this).siblings("input").focus();
    });
})
