import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:lucky_tour/controllers/app.dart';
// import 'package:url_launcher/url_launcher.dart';
import '../../controllers/user.dart';
import '../../apis/user_apis.dart';
import 'package:get_storage/get_storage.dart';
import '../../utils/location.dart';

class Profile extends StatefulWidget {
  const Profile({super.key});

  @override
  State<Profile> createState() => _ProfileState();
}

class _ProfileState extends State<Profile> {
  UserController userController = Get.find();
  AppController appController = Get.find();
  Location location = Location();

  void _logout() async {
    final result = await UserApi().logout();
    if (result['code'] == 200) {
      GetStorage().remove("token");
      userController.isLogin.value = false;
      userController.userInfo.value = {};
      Get.snackbar(
        '成功',
        "退出成功",
        snackPosition: SnackPosition.BOTTOM,
        backgroundColor: Colors.green,
        colorText: Colors.white,
        margin: const EdgeInsets.all(20),
      );
      Get.offAllNamed("/login_or_register");
    }
  }

  // Future<void> _launchUrl() async {
  //   final Uri url = Uri.parse('imeituan://www.meituan.com/hotel/search?q=海悦建国');
  //   if (!await launchUrl(url)) {
  //     throw Exception('Could not launch $url');
  //   }
  // }

  @override
  void initState() {
    super.initState();
    if (GetStorage().read("isDynamicState") == null) {
      GetStorage().write("isDynamicState", false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("我的"),
      ),
      body: ListView(
        children: [
          Obx(
            () => Offstage(
              offstage: !userController.isLogin.value,
              child: ListTile(
                style: ListTileStyle.drawer,
                leading: ClipOval(
                  child: userController.userInfo["avatar"] != null
                      ? Image.network(
                          userController.userInfo["avatar"],
                          width: 50,
                          height: 50,
                          fit: BoxFit.cover,
                        )
                      : const Icon(Icons.account_circle),
                ),
                title: Text(userController.userInfo["nickname"] ?? ""),
                subtitle: Text(userController.userInfo["email"] ?? ""),
              ),
            ),
          ),
          const Padding(
            padding: EdgeInsets.only(left: 15, bottom: 10, top: 10),
            child: Text(
              '用户',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
          ),
          ListTile(
            leading: Container(
              width: 40,
              height: 40,
              decoration: BoxDecoration(
                color: Colors.green[50],
                borderRadius: BorderRadius.circular(5),
              ),
              child: Icon(
                Icons.person_2_outlined,
                color: Colors.green[900],
              ),
            ),
            title: const Text('资料'),
            trailing: const Icon(Icons.arrow_forward_ios_outlined),
            onTap: () => Get.toNamed("/user_info"),
          ),
          ListTile(
            leading: Container(
              width: 40,
              height: 40,
              decoration: BoxDecoration(
                color: Colors.green[50],
                borderRadius: BorderRadius.circular(5),
              ),
              child: Icon(
                Icons.view_kanban_outlined,
                color: Colors.green[900],
              ),
            ),
            title: const Text('攻略'),
            trailing: const Icon(Icons.arrow_forward_ios_outlined),
            onTap: () => Get.toNamed("/strategy"),
          ),
          ListTile(
            leading: Container(
              width: 40,
              height: 40,
              decoration: BoxDecoration(
                color: Colors.green[50],
                borderRadius: BorderRadius.circular(5),
              ),
              child: Icon(
                Icons.mode_edit_outline_outlined,
                color: Colors.green[900],
              ),
            ),
            title: const Text('通知'),
            trailing: const Icon(Icons.arrow_forward_ios_outlined),
            onTap: () {
              appController.hasNotification.value = false;
              Get.toNamed("/notifications");
            },
          ),
          const Padding(
            padding: EdgeInsets.only(left: 15, bottom: 10, top: 10),
            child: Text(
              '设置',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
          ),
          ListTile(
            leading: Container(
              width: 40,
              height: 40,
              decoration: BoxDecoration(
                color: Colors.green[50],
                borderRadius: BorderRadius.circular(5),
              ),
              child: Icon(
                Icons.color_lens_outlined,
                color: Colors.green[900],
              ),
            ),
            title: const Text("主题"),
            onTap: () => Get.changeTheme(
              Get.isDarkMode ? ThemeData.light() : ThemeData.dark(),
            ),
          ),
          ListTile(
            leading: Container(
              width: 40,
              height: 40,
              decoration: BoxDecoration(
                color: Colors.green[50],
                borderRadius: BorderRadius.circular(5),
              ),
              child: Icon(Icons.apps_outlined, color: Colors.green[900]),
            ),
            title: const Text("智能服务"),
            trailing: Switch(
              activeColor: Colors.green,
              inactiveTrackColor: Colors.green[50],
              inactiveThumbColor: Colors.green[900],
              value: GetStorage().read("isDynamicState"),
              onChanged: (value) => setState(() {
                final location = Location();
                if (value == true) {
                  location.init();
                }
                GetStorage().write("isDynamicState", value);
                switch (value) {
                  case true:
                    location.startLocation();
                    break;
                  case false:
                    location.stopLocation();
                    break;
                }
              }),
            ),
          ),
          const Padding(
            padding: EdgeInsets.only(left: 15, bottom: 10, top: 10),
            child: Text(
              '其他',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
          ),
          ListTile(
            leading: Container(
              width: 40,
              height: 40,
              decoration: BoxDecoration(
                color: Colors.green[50],
                borderRadius: BorderRadius.circular(5),
              ),
              child:
                  Icon(Icons.help_outline_outlined, color: Colors.green[900]),
            ),
            title: const Text('帮助'),
            trailing: const Icon(Icons.arrow_forward_ios_outlined),
            onTap: () {},
          ),
          ListTile(
            leading: Container(
              width: 40,
              height: 40,
              decoration: BoxDecoration(
                color: Colors.green[50],
                borderRadius: BorderRadius.circular(5),
              ),
              child: Icon(Icons.update_outlined, color: Colors.green[900]),
            ),
            title: const Text('检测更新'),
            trailing: const Icon(Icons.arrow_forward_ios_outlined),
            onTap: () {},
          ),
          ListTile(
            leading: Container(
              width: 40,
              height: 40,
              decoration: BoxDecoration(
                color: Colors.green[50],
                borderRadius: BorderRadius.circular(5),
              ),
              child: Icon(Icons.logout, color: Colors.green[900]),
            ),
            title: const Text('登出'),
            trailing: const Icon(Icons.arrow_forward_ios_outlined),
            onTap: _logout,
          ),
        ],
      ),
    );
  }
}
