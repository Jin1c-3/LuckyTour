import 'dart:io';
import 'package:url_launcher/url_launcher.dart';

class MapUtil {
  /// 导航
  static void navigation(longitude, latitude) async {
    var url =
        '${Platform.isAndroid ? 'android' : 'ios'}amap://navi?sourceApplication=amap&lat=$latitude&lon=$longitude&dev=0&style=2';
    Uri uri = Uri.parse(url);
    if (!await launchUrl(uri)) {
      throw 'Could not launch $uri';
    }
  }

  /// 路径规划
  static void routePlan(slongitude, slatitude, dlongitude, dlatitude) async {
    var url =
        'amapuri://route/plan/?slat=$slatitude&slon=$slongitude&dlat=$dlatitude&dlon=$dlongitude&dev=0&t=0';
    Uri uri = Uri.parse(url);
    if (!await launchUrl(uri)) {
      throw 'Could not launch $uri';
    }
  }

  ///详情页
  static void detail(poiname, longitude, latitude, poiid) async {
    var url =
        'amapuri://poi/detail?poiname=$poiname&lat=$latitude&lon=$longitude&poiid=$poiid';
    Uri uri = Uri.parse(url);
    if (!await launchUrl(uri)) {
      throw 'Could not launch $uri';
    }
  }

  ///周边搜索
  static void aroundSearch(keywords) async {
    var url = 'androidamap://poi?sourceApplication=amap&keywords=$keywords';
    Uri uri = Uri.parse(url);
    if (!await launchUrl(uri)) {
      throw 'Could not launch $uri';
    }
  }
}
