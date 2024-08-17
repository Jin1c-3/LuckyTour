import 'dart:async';
import 'dart:io';
import 'dart:math';

import 'package:amap_flutter_location/amap_flutter_location.dart';
import 'package:amap_flutter_location/amap_location_option.dart';
import 'package:get_storage/get_storage.dart';
import 'package:lucky_tour/controllers/app.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:lucky_tour/controllers/plan.dart';
import 'package:get/get.dart';
import 'package:lucky_tour/utils/notification_util.dart';
import '../apis/user_apis.dart';

class Location {
  static Map? locationResult;
  static StreamSubscription<Map<String, Object>>? locationListener;
  static AMapFlutterLocation? locationPlugin;
  Map<String, double>? lastLocation;
  PlanController planController = Get.put(PlanController());
  AppController appController = Get.put(AppController());
  final NotificationHelper _notificationHelper = NotificationHelper();

  void init() {
    AMapFlutterLocation.updatePrivacyShow(true, true);
    AMapFlutterLocation.updatePrivacyAgree(true);
    requestPermission();

    ///设置Android和iOS的apiKey<br>
    ///key的申请请参考高德开放平台官网说明<br>
    ///Android: https://lbs.amap.com/api/android-location-sdk/guide/create-project/get-key
    ///iOS: https://lbs.amap.com/api/ios-location-sdk/guide/create-project/get-key
    AMapFlutterLocation.setApiKey(
        "224007cc30da67813ca411fdac01ff99", "224007cc30da67813ca411fdac01ff99");

    ///iOS 获取native精度类型
    if (Platform.isIOS) {
      requestAccuracyAuthorization();
    }

    locationPlugin ??= AMapFlutterLocation();

    ///注册定位结果监听
    locationListener ??= locationPlugin!
        .onLocationChanged()
        .listen((Map<String, Object> result) {
      locationResult = result;
      print("定位结果： $result");
      if (GetStorage().read('dynamicPlanId') != null) {
        checkPositionIsInActivePlanDestinations();
        checkPositionIsMoveMoreThan(result);
      }
    });
  }

  void dispose() {
    if (null != locationListener) {
      locationListener?.cancel();
    }

    ///销毁定位
    locationPlugin!.destroy();
  }

  ///获取iOS native的accuracyAuthorization类型
  void requestAccuracyAuthorization() async {
    AMapAccuracyAuthorization currentAccuracyAuthorization =
        await locationPlugin!.getSystemAccuracyAuthorization();
    if (currentAccuracyAuthorization ==
        AMapAccuracyAuthorization.AMapAccuracyAuthorizationFullAccuracy) {
      print("精确定位类型");
    } else if (currentAccuracyAuthorization ==
        AMapAccuracyAuthorization.AMapAccuracyAuthorizationReducedAccuracy) {
      print("模糊定位类型");
    } else {
      print("未知定位类型");
    }
  }

  void setLocationOption() {
    AMapLocationOption locationOption = AMapLocationOption();

    ///是否单次定位
    locationOption.onceLocation = false;

    ///是否需要返回逆地理信息
    locationOption.needAddress = true;

    ///逆地理信息的语言类型
    locationOption.geoLanguage = GeoLanguage.DEFAULT;

    locationOption.desiredLocationAccuracyAuthorizationMode =
        AMapLocationAccuracyAuthorizationMode.ReduceAccuracy;

    locationOption.fullAccuracyPurposeKey = "AMapLocationScene";

    ///设置Android端连续定位的定位间隔
    locationOption.locationInterval = 2000;

    ///设置Android端的定位模式<br>
    ///可选值：<br>
    ///<li>[AMapLocationMode.Battery_Saving]</li>
    ///<li>[AMapLocationMode.Device_Sensors]</li>
    ///<li>[AMapLocationMode.Hight_Accuracy]</li>
    locationOption.locationMode = AMapLocationMode.Hight_Accuracy;

    ///设置iOS端的定位最小更新距离<br>
    locationOption.distanceFilter = -1;

    ///设置iOS端期望的定位精度
    /// 可选值：<br>
    /// <li>[DesiredAccuracy.Best] 最高精度</li>
    /// <li>[DesiredAccuracy.BestForNavigation] 适用于导航场景的高精度 </li>
    /// <li>[DesiredAccuracy.NearestTenMeters] 10米 </li>
    /// <li>[DesiredAccuracy.Kilometer] 1000米</li>
    /// <li>[DesiredAccuracy.ThreeKilometers] 3000米</li>
    locationOption.desiredAccuracy = DesiredAccuracy.Best;

    ///设置iOS端是否允许系统暂停定位
    locationOption.pausesLocationUpdatesAutomatically = false;

    ///将定位参数设置给定位插件
    locationPlugin!.setLocationOption(locationOption);
  }

  ///开始定位
  void startLocation() {
    ///开始定位之前设置定位参数
    setLocationOption();
    locationPlugin!.startLocation();
  }

  ///停止定位
  void stopLocation() {
    locationPlugin!.stopLocation();
  }

  /// 动态申请定位权限
  void requestPermission() async {
    // 申请权限
    bool hasLocationPermission = await requestLocationPermission();
    bool hasNotificationPermission = await requestNotificationPermission();
    if (hasLocationPermission) {
      print("定位权限申请通过");
    } else {
      print("定位权限申请不通过");
    }
    if (hasNotificationPermission) {
      print("通知权限申请通过");
    } else {
      print("通知权限申请不通过");
    }
  }

  /// 申请定位权限
  /// 授予定位权限返回true， 否则返回false
  Future<bool> requestLocationPermission() async {
    //获取当前的权限
    var status = await Permission.location.status;
    if (status == PermissionStatus.granted) {
      //已经授权
      return true;
    } else {
      //未授权则发起一次申请
      status = await Permission.location.request();
      if (status == PermissionStatus.granted) {
        return true;
      } else {
        return false;
      }
    }
  }

  ///申请通知权限
  ///授予通知权限返回true， 否则返回false
  Future<bool> requestNotificationPermission() async {
    //获取当前的权限
    var status = await Permission.notification.status;
    if (status == PermissionStatus.granted) {
      //已经授权
      return true;
    } else {
      //未授权则发起一次申请
      status = await Permission.notification.request();
      if (status == PermissionStatus.granted) {
        return true;
      } else {
        return false;
      }
    }
  }

  void checkPositionIsInActivePlanDestinations() {
    final activePlan = GetStorage().read('dynamicPlan');
    if (activePlan == null) return;
    for (var key in activePlan['content'].keys) {
      for (var destination in activePlan['content'][key]) {
        if (destination['type'] == '景点') {
          final location = destination['location'].split(',');
          if ((double.parse(location[1]) - locationResult!['latitude']).abs() <
                  0.005 &&
              (double.parse(location[0]) - locationResult!['longitude']).abs() <
                  0.005) {
            GetStorage().write('destination', destination);
            Map gones = GetStorage().read('dynamicPlanGones');
            if (gones[destination['name']] == false) {
              _notificationHelper.showNotification(
                title: '旅行导游',
                body: '检测到您已到达${destination['name']}，点击即可开启智能导游服务',
                payload: 'guide',
              );
              gones[destination['name']] = true;
              GetStorage().write('dynamicPlanGones', gones);
              DateTime now = DateTime.now();
              appController.notificationList.add({
                'title': '旅行导游',
                'description': '您已到达${destination['name']}',
                'time':
                    '${now.month.toString().padLeft(2, '0')}-${now.day.toString().padLeft(2, '0')}',
              });
              appController.hasNotification.value = true;
              planController.guideLocation.value =
                  "${destination['pname']}${destination['cityname']}${destination['name']}";
            }
          }
        }
      }
    }
  }

  void checkPositionIsMoveMoreThan(result) {
    if (lastLocation != null) {
      double distance = calculateDistance(
        lastLocation!['latitude'],
        lastLocation!['longitude'],
        result['latitude'],
        result['longitude'],
      );

      // 如果距离超过100米，发出请求
      if (distance > 0.1) {
        UserApi().monitorUserStatus({
          "latitudeAndLongitude":
              "${result['latitude']},${result['longitude']}",
          "pid": GetStorage().read('dynamicPlanId'),
        });
      }
    }

    // 更新上一次的位置
    lastLocation = {
      'latitude': result['latitude'],
      'longitude': result['longitude'],
    };
  }

  double calculateDistance(lat1, lon1, lat2, lon2) {
    var p = 0.017453292519943295;
    var c = cos;
    var a = 0.5 -
        c((lat2 - lat1) * p) / 2 +
        c(lat1 * p) * c(lat2 * p) * (1 - c((lon2 - lon1) * p)) / 2;
    return 12742 * asin(sqrt(a));
  }
}
