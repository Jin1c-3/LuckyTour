import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../../apis/user_apis.dart';

class LoginWithVerification extends StatefulWidget {
  const LoginWithVerification({super.key});

  @override
  State<LoginWithVerification> createState() => _LoginWithVerificationState();
}

class _LoginWithVerificationState extends State<LoginWithVerification> {
  final TextEditingController _accountController = TextEditingController();
  final _formKey = GlobalKey<FormState>();

  void _login() async {
    final result = await UserApi().getValidateCodeLogin({
      "emailOrPhone": _accountController.text,
    });
    if (result['code'] == 200) {
      Get.toNamed("/auth_code_login", arguments: {
        "emailOrPhone": _accountController.text,
        "verificationCode": result['data'],
      });
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
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        padding: const EdgeInsets.all(30),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Row(
              children: [
                Text(
                  '云栖',
                  style: TextStyle(
                    fontSize: 40,
                  ),
                ),
              ],
            ),
            Container(
              height: 300,
              margin: const EdgeInsets.only(top: 100),
              width: double.infinity,
              decoration: const BoxDecoration(
                image: DecorationImage(
                  image: AssetImage('images/page/login.png'),
                  fit: BoxFit.fitWidth,
                ),
              ),
            ),
            Form(
              key: _formKey,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Padding(
                    padding: const EdgeInsets.fromLTRB(0, 20, 0, 20),
                    child: TextFormField(
                      controller: _accountController,
                      cursorColor: Colors.green,
                      decoration: InputDecoration(
                        isCollapsed: true,
                        contentPadding: const EdgeInsets.all(10),
                        fillColor: Colors.green[50],
                        filled: true,
                        border: const OutlineInputBorder(
                          borderSide: BorderSide.none,
                          borderRadius: BorderRadius.all(Radius.circular(10)),
                        ),
                        hintText: '邮箱/电话号码',
                      ),
                      validator: (value) {
                        if (value == null || value.isEmpty) {
                          return '请输入邮箱/手机号码';
                        } else if (!GetUtils.isEmail(value) &&
                            !GetUtils.isPhoneNumber(value)) {
                          return '请输入正确的邮箱/手机号码';
                        } else {
                          return null;
                        }
                      },
                    ),
                  ),
                  ElevatedButton(
                    onPressed: _login,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.greenAccent[700],
                      fixedSize: const Size(350, 45),
                      // padding: const EdgeInsets.all(10),
                      shape: const RoundedRectangleBorder(
                        borderRadius: BorderRadius.all(
                          Radius.circular(10),
                        ),
                      ),
                    ),
                    child: const Text(
                      '获取验证码',
                      style: TextStyle(fontSize: 20, color: Colors.white),
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
