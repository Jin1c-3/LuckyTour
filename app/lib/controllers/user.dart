import 'package:get/get.dart';

class UserController extends GetxController {
  RxBool isLogin = false.obs;
  RxBool isFollow = false.obs;
  RxBool isCollect = false.obs;
  RxString selectedPersonId = "".obs;
  RxList followings = [].obs;
  RxList collections = [].obs;
  RxMap userInfo = {}.obs;
  RxMap selectedPersonInfo = {}.obs;
}
