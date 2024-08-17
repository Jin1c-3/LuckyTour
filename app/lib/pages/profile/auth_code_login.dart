import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../../controllers/user.dart';
import 'package:pinput/pinput.dart';
import '../../apis/user_apis.dart';
import 'package:get_storage/get_storage.dart';

class AuthCodeLogin extends StatefulWidget {
  const AuthCodeLogin({super.key});

  @override
  State<AuthCodeLogin> createState() => _AuthCodeLoginState();
}

class _AuthCodeLoginState extends State<AuthCodeLogin> {
  UserController userController = Get.find();

  final pinController = TextEditingController();
  final focusNode = FocusNode();
  final formKey = GlobalKey<FormState>();
  String verificationCode = Get.arguments['verificationCode'];

  void _verify(String pin) async {
    if (formKey.currentState!.validate()) {
      final result = await UserApi().login({
        "emailOrPhone": Get.arguments['emailOrPhone'],
        "jrid": GetStorage().read("rid"),
        "rememberMe": true,
      });
      if (result['code'] == 200) {
        GetStorage().write("token", result['data']['token']);
        userController.isLogin.value = true;
        Get.snackbar(
          '成功',
          result['message'],
          snackPosition: SnackPosition.BOTTOM,
          backgroundColor: Colors.green,
          colorText: Colors.white,
          margin: const EdgeInsets.all(20),
        );
        final userInfo = await UserApi().getUserInfo();
        userController.userInfo.value = Map.from(userInfo['data']);
        Get.offAllNamed("/home");
        final followings = await UserApi().getFollowings();
        userController.followings.value = List.from(followings['data']);
        final collections = await UserApi().getCollectionList();
        userController.collections.value = List.from(collections['data']);
        for (var i = 0; i < userController.collections.length; i++) {
          userController.collections[i]['content'] =
              jsonDecode(userController.collections[i]['content']);
        }
      } else {
        Get.snackbar(
          '失败',
          result['message'],
          snackPosition: SnackPosition.BOTTOM,
          backgroundColor: Colors.red,
          colorText: Colors.white,
          margin: const EdgeInsets.all(20),
        );
      }
    }
  }

  void _resend() async {
    final result = await UserApi().getValidateCodeLogin({
      "emailOrPhone": Get.arguments['emailOrPhone'],
    });
    if (result['code'] == 200) {
      verificationCode = result['data'];
    } else {
      Get.snackbar(
        '失败',
        result['message'],
        snackPosition: SnackPosition.BOTTOM,
        backgroundColor: Colors.red,
        colorText: Colors.white,
        margin: const EdgeInsets.all(20),
      );
    }
  }

  @override
  void dispose() {
    pinController.dispose();
    focusNode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    const focusedBorderColor = Color.fromRGBO(23, 171, 144, 1);
    const fillColor = Color.fromRGBO(243, 246, 249, 0);
    const borderColor = Color.fromRGBO(23, 171, 144, 0.4);

    final defaultPinTheme = PinTheme(
      width: 56,
      height: 56,
      textStyle: const TextStyle(
        color: Color.fromRGBO(30, 60, 87, 1),
        fontSize: 24,
      ),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(19),
        border: Border.all(color: borderColor),
      ),
    );

    return Scaffold(
      body: Container(
        width: double.infinity,
        height: double.infinity,
        padding: const EdgeInsets.all(30),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Form(
              key: formKey,
              child: Column(
                children: [
                  const Padding(
                    padding: EdgeInsets.only(bottom: 20),
                    child: Text(
                      '安全验证',
                      style: TextStyle(fontSize: 45, color: Colors.black),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(bottom: 60),
                    child: Text(
                      '我们已经通过你的邮箱/手机号码发送了验证码，请及时查收',
                      textAlign: TextAlign.center,
                      style: TextStyle(fontSize: 15, color: Colors.green[200]),
                    ),
                  ),
                  Pinput(
                    length: 5,
                    controller: pinController,
                    focusNode: focusNode,
                    androidSmsAutofillMethod:
                        AndroidSmsAutofillMethod.smsUserConsentApi,
                    listenForMultipleSmsOnAndroid: true,
                    defaultPinTheme: defaultPinTheme,
                    hapticFeedbackType: HapticFeedbackType.lightImpact,
                    onCompleted: _verify,
                    validator: (value) =>
                        value == verificationCode ? null : '验证码错误',
                    cursor: Column(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: [
                        Container(
                          margin: const EdgeInsets.only(bottom: 9),
                          width: 22,
                          height: 1,
                          color: focusedBorderColor,
                        ),
                      ],
                    ),
                    focusedPinTheme: defaultPinTheme.copyWith(
                      decoration: defaultPinTheme.decoration!.copyWith(
                        borderRadius: BorderRadius.circular(8),
                        border: Border.all(color: focusedBorderColor),
                      ),
                    ),
                    submittedPinTheme: defaultPinTheme.copyWith(
                      decoration: defaultPinTheme.decoration!.copyWith(
                        color: fillColor,
                        borderRadius: BorderRadius.circular(19),
                        border: Border.all(color: focusedBorderColor),
                      ),
                    ),
                    errorPinTheme: defaultPinTheme.copyBorderWith(
                      border: Border.all(color: Colors.redAccent),
                    ),
                  ),
                  Padding(
                    padding: const EdgeInsets.only(top: 20),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        const Text('没有收到验证码?', style: TextStyle(fontSize: 16)),
                        TextButton(
                          onPressed: () => _resend(),
                          style: ButtonStyle(
                            overlayColor:
                                MaterialStateProperty.all(Colors.transparent),
                          ),
                          child: Text('重新发送',
                              style: TextStyle(
                                  fontSize: 16, color: Colors.green[400])),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
