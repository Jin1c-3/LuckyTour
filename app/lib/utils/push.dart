import 'dart:convert';

import 'package:get_storage/get_storage.dart';
import 'package:jpush_flutter/jpush_flutter.dart';
import 'package:get/get.dart';
import 'package:lucky_tour/utils/map.dart';
import './notification_util.dart';
import '../apis/plan_apis.dart';
import 'package:url_launcher/url_launcher.dart';

class Push {
  final jpush = JPush();

  Future<void> init() async {
    try {
      jpush.addEventHandler(
          onReceiveNotification: (Map<String, dynamic> message) async {
        NotificationHelper.id++;
        print("flutter onReceiveNotification: $message");
      }, onOpenNotification: (Map<String, dynamic> message) async {
        print("flutter onOpenNotification: $message");
        String type =
            jsonDecode(message['extras']['cn.jpush.android.EXTRA'])['type'];
        String reason =
            jsonDecode(message['extras']['cn.jpush.android.EXTRA'])['reason'];
        switch (type) {
          case 'weather':
            dynamicWeather(reason);
            break;
          case 'raingoods':
            pushRainGoods();
            break;
          case 'snowgoods':
            pushSnowGoods();
            break;
          case 'foggoods':
            pushFogGoods();
            break;
          case 'toilet':
            pushToilet();
            break;
          default:
            break;
        }
      }, onReceiveMessage: (Map<String, dynamic> message) async {
        print("flutter onReceiveMessage: $message");
      }, onReceiveNotificationAuthorization:
              (Map<String, dynamic> message) async {
        print("flutter onReceiveNotificationAuthorization: $message");
      }, onNotifyMessageUnShow: (Map<String, dynamic> message) async {
        print("flutter onNotifyMessageUnShow: $message");
      }, onInAppMessageShow: (Map<String, dynamic> message) async {
        print("flutter onInAppMessageShow: $message");
      }, onInAppMessageClick: (Map<String, dynamic> message) async {
        print("flutter onInAppMessageClick: $message");
      }, onConnected: (Map<String, dynamic> message) async {
        print("flutter onConnected: $message");
      });
    } catch (e) {
      print(e);
    }

    jpush.setAuth(enable: true);
    jpush.setup(
      appKey: "484d33dab2b304676067ef55", //你自己应用的 AppKey
      channel: "developer-default",
      production: false,
      debug: true,
    );
    jpush.applyPushAuthority(
        const NotificationSettingsIOS(sound: true, alert: true, badge: true));

    // Platform messages may fail, so we use a try/catch PlatformException.
    jpush.getRegistrationID().then((rid) {
      GetStorage().write('rid', rid);
    });

    // iOS要是使用应用内消息，请在页面进入离开的时候配置pageEnterTo 和  pageLeave 函数，参数为页面名。
    jpush.pageEnterTo("HomePage"); // 在离开页面的时候请调用 jpush.pageLeave("HomePage");

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
  }

  void dynamicWeather(reason) async {
    final result = await PlanApi().updatePlan({
      "plan": GetStorage().read('dynamicPlan'),
      "reason": reason,
    });
    if (result['code'] == 200) {
      final res = await PlanApi().confirmUpdatePlan({
        "message": "成功",
      });
    }
  }

  void pushRainGoods() async {
    final Uri url = Uri.parse(
        'https://so.m.jd.com/ware/search.action?keyword=雨具&searchFrom=home&sf=15&as=0');
    if (!await launchUrl(url)) {
      throw Exception('Could not launch $url');
    }
  }

  void pushSnowGoods() async {
    final Uri url = Uri.parse(
        'https://so.m.jd.com/ware/search.action?keyword=雪具&searchFrom=home&sf=15&as=0');
    if (!await launchUrl(url)) {
      throw Exception('Could not launch $url');
    }
  }

  void pushFogGoods() async {
    final Uri url = Uri.parse(
        'https://so.m.jd.com/ware/search.action?keyword=防雾&searchFrom=home&sf=15&as=0');
    if (!await launchUrl(url)) {
      throw Exception('Could not launch $url');
    }
  }

  void pushToilet() async {
    MapUtil.aroundSearch('厕所');
  }
}
