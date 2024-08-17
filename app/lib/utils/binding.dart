import 'package:get/get.dart';
import '../controllers/user.dart';
import '../controllers/plan.dart';
import '../controllers/app.dart';

class AllControllerBinding implements Bindings {
  @override
  void dependencies() {
    Get.put<UserController>(UserController());
    Get.put<PlanController>(PlanController());
    Get.put<AppController>(AppController());
  }
}
