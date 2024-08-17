import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:get_storage/get_storage.dart';
import '../../apis/user_apis.dart';
import '../../controllers/user.dart';

class LoginWithPassword extends StatefulWidget {
  const LoginWithPassword({super.key});

  @override
  State<LoginWithPassword> createState() => _LoginWithPasswordState();
}

class _LoginWithPasswordState extends State<LoginWithPassword> {
  UserController userController = Get.find();

  final TextEditingController _accountController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final _formKey = GlobalKey<FormState>();

  void _login() async {
    if (_formKey.currentState!.validate()) {
      final result = await UserApi().login({
        "emailOrPhone": _accountController.text,
        "password": _passwordController.text,
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
              height: 220,
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
                children: [
                  Padding(
                    padding: const EdgeInsets.fromLTRB(0, 20, 0, 10),
                    child: TextFormField(
                      key: const Key('account'),
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
                          return '请输入邮箱/电话号码';
                        } else if (!GetUtils.isEmail(value) &&
                            !GetUtils.isPhoneNumber(value)) {
                          return '请输入正确的邮箱/电话号码';
                        } else {
                          return null;
                        }
                      },
                    ),
                  ),
                  Padding(
                    key: const Key('password'),
                    padding: const EdgeInsets.fromLTRB(0, 10, 0, 20),
                    child: TextFormField(
                      controller: _passwordController,
                      obscureText: true,
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
                        hintText: '密码',
                      ),
                      validator: (value) =>
                          value == null || value.isEmpty ? '请输入密码' : null,
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
                      '登录',
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
                  const Text('还没有账号？', style: TextStyle(fontSize: 16)),
                  TextButton(
                    onPressed: () => Get.toNamed("/register"),
                    style: ButtonStyle(
                      overlayColor:
                          MaterialStateProperty.all(Colors.transparent),
                    ),
                    child: Text(
                      '注册',
                      style: TextStyle(fontSize: 16, color: Colors.green[400]),
                    ),
                  ),
                ],
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Column(
                  children: [
                    Container(
                      margin: const EdgeInsets.only(top: 10),
                      height: 50,
                      width: 50,
                      alignment: Alignment.center,
                      decoration: BoxDecoration(
                        color: Colors.green[100],
                        shape: BoxShape.circle,
                      ),
                      child: IconButton(
                        onPressed: () =>
                            Get.toNamed("/login_with_verification"),
                        icon: const Icon(Icons.phone_android),
                        iconSize: 30,
                        color: Colors.white,
                      ),
                    ),
                    const SizedBox(
                      height: 10,
                    ),
                    const Text(
                      '手机登录',
                      style: TextStyle(fontSize: 14),
                    ),
                  ],
                ),
                const SizedBox(width: 50),
                Column(
                  children: [
                    Container(
                      margin: const EdgeInsets.only(top: 10),
                      height: 50,
                      width: 50,
                      alignment: Alignment.center,
                      decoration: BoxDecoration(
                        color: Colors.green[100],
                        shape: BoxShape.circle,
                      ),
                      child: IconButton(
                        onPressed: () =>
                            Get.toNamed("/login_with_verification"),
                        icon: const Icon(Icons.email),
                        iconSize: 30,
                        color: Colors.white,
                      ),
                    ),
                    const SizedBox(
                      height: 10,
                    ),
                    const Text(
                      '邮箱登录',
                      style: TextStyle(fontSize: 14),
                    ),
                  ],
                )
              ],
            ),
          ],
        ),
      ),
    );
  }
}
