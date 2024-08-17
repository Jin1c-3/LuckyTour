import 'package:get/get.dart';

class PlanController extends GetxController {
  RxBool isGenerate = false.obs;
  RxMap selectedCityLatlng = {}.obs;
  RxMap selectedCityInfo = {}.obs;
  RxList selectedTagsInfo = [].obs;
  RxMap temp = {}.obs;
  RxMap selectedPlanItemInfo = {}.obs;
  RxMap selectedPlanInfo = {}.obs;
  RxMap selectedStrategyItem = {}.obs;
  RxList strategyList = [].obs;
  RxString guideLocation = "".obs;
}
