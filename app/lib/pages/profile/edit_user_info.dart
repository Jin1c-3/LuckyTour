import 'dart:io';
import 'package:dio/dio.dart' as dio;
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import '../../apis/user_apis.dart';
import 'package:get/get.dart';
import '../../controllers/user.dart';

class EditUserInfo extends StatefulWidget {
  const EditUserInfo({super.key});

  @override
  State<EditUserInfo> createState() => _EditUserInfoState();
}

class _EditUserInfoState extends State<EditUserInfo> {
  UserController userController = Get.find();
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nickNameController = TextEditingController();
  final TextEditingController _passWordController = TextEditingController();
  final TextEditingController _phoneController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _sexController = TextEditingController();
  final TextEditingController _birthdayController = TextEditingController();

  XFile? _image;
  File? file;
  dio.MultipartFile? multipartFile;

  void _openGallery() async {
    _image = await ImagePicker().pickImage(source: ImageSource.gallery);
    if (_image == null) return;
    file = File(_image!.path);
    multipartFile = await dio.MultipartFile.fromFile(file!.path);
    setState(() {});
  }

  void _save() async {
    if (_formKey.currentState!.validate()) {
      dio.FormData data = dio.FormData.fromMap({'avatarPic': multipartFile});
      final params = {
        "id": userController.userInfo['id'] ?? '',
        'nickname': _nickNameController.text,
        'password': _passWordController.text,
        'phone': _phoneController.text,
        'email': _emailController.text,
        'birthday': _birthdayController.text,
        'sex': _sexController.text,
      };
      final result = await UserApi().updateUserInfo(params, data);
      if (result['code'] == 200) {
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
        setState(() {
          _image = null;
          _nickNameController.text = userController.userInfo['nickname'] ?? '';
          _passWordController.text = '';
          _phoneController.text = userController.userInfo['phone'] ?? '';
          _emailController.text = userController.userInfo['email'] ?? '';
          _sexController.text = userController.userInfo['sex'] ?? '';
          _birthdayController.text = userController.userInfo['birthday'] ?? '';
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
  void initState() {
    super.initState();
    _nickNameController.text = userController.userInfo['nickname'] ?? '';
    _passWordController.text = '';
    _phoneController.text = userController.userInfo['phone'] ?? '';
    _emailController.text = userController.userInfo['email'] ?? '';
    _sexController.text = userController.userInfo['sex'] ?? '';
    _birthdayController.text = userController.userInfo['birthday'] ?? '';
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        actions: [
          TextButton(
            onPressed: _save,
            child: const Text(
              '保存',
              style: TextStyle(color: Colors.green),
            ),
          ),
        ],
      ),
      body: ListView(
        padding: const EdgeInsets.fromLTRB(20, 10, 20, 10),
        children: [
          Center(
            child: Stack(
              children: [
                CircleAvatar(
                  radius: 60,
                  backgroundColor: Colors.grey[200]!,
                  backgroundImage: _image == null
                      ? null
                      : FileImage(File(_image!.path)) as ImageProvider,
                ),
                Positioned(
                  bottom: 0,
                  right: 0,
                  child: Container(
                    decoration: BoxDecoration(
                      color: const Color.fromRGBO(255, 255, 255, 0.5),
                      borderRadius: BorderRadius.circular(50),
                    ),
                    child: IconButton(
                      onPressed: _openGallery,
                      icon: const Icon(Icons.camera_alt_outlined),
                    ),
                  ),
                ),
              ],
            ),
          ),
          Form(
            key: _formKey,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Padding(
                  padding: EdgeInsets.fromLTRB(20, 20, 0, 0),
                  child: Text('昵称', style: TextStyle(fontSize: 18)),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(20, 5, 20, 0),
                  child: TextFormField(
                    controller: _nickNameController,
                    decoration: InputDecoration(
                      isCollapsed: true,
                      contentPadding: const EdgeInsets.all(10),
                      fillColor: Colors.green[100],
                      filled: true,
                      border: const OutlineInputBorder(
                        borderSide: BorderSide.none,
                        borderRadius: BorderRadius.all(Radius.circular(10)),
                      ),
                    ),
                  ),
                ),
                const Padding(
                  padding: EdgeInsets.fromLTRB(20, 20, 0, 0),
                  child: Text('密码', style: TextStyle(fontSize: 18)),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(20, 5, 20, 0),
                  child: TextFormField(
                    controller: _passWordController,
                    obscureText: true,
                    decoration: InputDecoration(
                      isCollapsed: true,
                      contentPadding: const EdgeInsets.all(10),
                      fillColor: Colors.green[100],
                      filled: true,
                      border: const OutlineInputBorder(
                        borderSide: BorderSide.none,
                        borderRadius: BorderRadius.all(Radius.circular(10)),
                      ),
                    ),
                  ),
                ),
                const Padding(
                  padding: EdgeInsets.fromLTRB(20, 20, 0, 0),
                  child: Text('电话号码', style: TextStyle(fontSize: 18)),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(20, 5, 20, 0),
                  child: TextFormField(
                    controller: _phoneController,
                    decoration: InputDecoration(
                      isCollapsed: true,
                      contentPadding: const EdgeInsets.all(10),
                      fillColor: Colors.green[100],
                      filled: true,
                      border: const OutlineInputBorder(
                        borderSide: BorderSide.none,
                        borderRadius: BorderRadius.all(Radius.circular(10)),
                      ),
                    ),
                    validator: (value) =>
                        value != '' && !GetUtils.isPhoneNumber(value!)
                            ? '请输入正确的电话号码'
                            : null,
                  ),
                ),
                const Padding(
                  padding: EdgeInsets.fromLTRB(20, 20, 0, 0),
                  child: Text('邮箱', style: TextStyle(fontSize: 18)),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(20, 5, 20, 0),
                  child: TextFormField(
                      controller: _emailController,
                      decoration: InputDecoration(
                        isCollapsed: true,
                        contentPadding: const EdgeInsets.all(10),
                        fillColor: Colors.green[100],
                        filled: true,
                        border: const OutlineInputBorder(
                          borderSide: BorderSide.none,
                          borderRadius: BorderRadius.all(Radius.circular(10)),
                        ),
                      ),
                      validator: (value) =>
                          value != '' && !GetUtils.isEmail(value!)
                              ? '请输入正确的邮箱'
                              : null),
                ),
                const Padding(
                  padding: EdgeInsets.fromLTRB(20, 20, 0, 0),
                  child: Text('性别', style: TextStyle(fontSize: 18)),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(20, 5, 20, 0),
                  child: TextFormField(
                    controller: _sexController,
                    decoration: InputDecoration(
                      isCollapsed: true,
                      contentPadding: const EdgeInsets.all(10),
                      fillColor: Colors.green[100],
                      filled: true,
                      border: const OutlineInputBorder(
                        borderSide: BorderSide.none,
                        borderRadius: BorderRadius.all(Radius.circular(10)),
                      ),
                    ),
                    validator: (value) =>
                        value != '' && !(value == "男" || value == "女")
                            ? "请输入正确的性别"
                            : null,
                  ),
                ),
                const Padding(
                  padding: EdgeInsets.fromLTRB(20, 20, 0, 0),
                  child: Text('出生日期', style: TextStyle(fontSize: 18)),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(20, 5, 20, 0),
                  child: TextFormField(
                    controller: _birthdayController,
                    decoration: InputDecoration(
                      isCollapsed: true,
                      contentPadding: const EdgeInsets.all(10),
                      fillColor: Colors.green[100],
                      filled: true,
                      border: const OutlineInputBorder(
                        borderSide: BorderSide.none,
                        borderRadius: BorderRadius.all(Radius.circular(10)),
                      ),
                    ),
                    onTap: () async {
                      final date = await showDatePicker(
                          context: context,
                          initialDate: DateTime.now(),
                          firstDate: DateTime(1950),
                          lastDate: DateTime.now());
                      if (date != null) {
                        _birthdayController.text =
                            date.toString().substring(0, 10);
                      }
                    },
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
