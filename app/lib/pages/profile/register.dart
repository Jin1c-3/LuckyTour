import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../../apis/user_apis.dart';
import 'package:lucky_tour/controllers/user.dart';

class Register extends StatefulWidget {
  const Register({super.key});

  @override
  State<Register> createState() => _RegisterState();
}

class _RegisterState extends State<Register> {
  UserController userController = Get.find();

  final TextEditingController _accountController = TextEditingController();
  final _formKey = GlobalKey<FormState>();

  void _register() async {
    if (_formKey.currentState!.validate()) {
      final result = await UserApi().getValidateCodeRegister({
        "emailOrPhone": _accountController.text,
      });
      if (result['code'] == 200) {
        Get.toNamed("/auth_code_register", arguments: {
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
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        padding: const EdgeInsets.all(20),
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
              height: 400,
              width: double.infinity,
              decoration: const BoxDecoration(
                image: DecorationImage(
                  image: AssetImage('images/page/register.png'),
                  fit: BoxFit.fitWidth,
                ),
              ),
            ),
            Form(
              key: _formKey,
              child: Column(
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
                        }),
                  ),
                  ElevatedButton(
                    onPressed: _register,
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
            Padding(
              padding: const EdgeInsets.fromLTRB(0, 20, 0, 0),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Text('已经拥有账号？', style: TextStyle(fontSize: 16)),
                  TextButton(
                    onPressed: () => Get.back(),
                    style: ButtonStyle(
                      overlayColor:
                          MaterialStateProperty.all(Colors.transparent),
                    ),
                    child: Text(
                      '登录',
                      style: TextStyle(fontSize: 16, color: Colors.green[400]),
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
