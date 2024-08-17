import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
// import 'package:get_storage/get_storage.dart';
import 'package:lucky_tour/apis/user_apis.dart';
import 'package:lucky_tour/controllers/user.dart';

class LoginOrRegister extends StatefulWidget {
  const LoginOrRegister({super.key});

  @override
  State<LoginOrRegister> createState() => _LoginOrRegisterState();
}

class _LoginOrRegisterState extends State<LoginOrRegister> {
  UserController userController = Get.find();

  void _autoLogin() async {
    final userInfo = await UserApi().getUserInfo();
    if (userInfo['code'] != 200) return;
    userController.isLogin.value = true;
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
  }

  @override
  void initState() {
    super.initState();
    _autoLogin();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text(
              '云栖',
              style: TextStyle(
                  fontSize: 50,
                  fontWeight: FontWeight.w600,
                  color: Colors.green),
            ),
            Text(
              '"乘云之致远,而今且栖"',
              style: TextStyle(
                fontSize: 25,
                color: Colors.green[200],
              ),
            ),
            Container(
              height: 400,
              width: double.infinity,
              decoration: const BoxDecoration(
                image: DecorationImage(
                  image: AssetImage('images/page/login_or_register.png'),
                  fit: BoxFit.fitWidth,
                ),
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                ElevatedButton(
                  onPressed: () => Get.toNamed("/login_with_password"),
                  style: ButtonStyle(
                    backgroundColor: MaterialStateProperty.all(Colors.green),
                    padding: MaterialStateProperty.all(
                      const EdgeInsets.fromLTRB(50, 10, 50, 10),
                    ),
                  ),
                  child: const Text(
                    "登录",
                    style: TextStyle(color: Colors.white),
                  ),
                ),
                const SizedBox(
                  width: 20,
                ),
                TextButton(
                  onPressed: () => Get.toNamed("/register"),
                  style: ButtonStyle(
                    padding: MaterialStateProperty.all(
                      const EdgeInsets.fromLTRB(50, 10, 50, 10),
                    ),
                    overlayColor: MaterialStateProperty.all(Colors.transparent),
                    backgroundColor:
                        MaterialStateProperty.all(Colors.green[50]),
                  ),
                  child: const Text(
                    "注册",
                    style: TextStyle(color: Colors.green),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
