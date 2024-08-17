import 'package:get/get.dart';

class AppController extends GetxController {
  RxBool hasNotification = false.obs;
  RxList notificationList = [].obs;
  RxInt notificationCount = 0.obs;
}
