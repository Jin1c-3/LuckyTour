import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:flutter_map_animations/flutter_map_animations.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:get/get.dart';
import 'package:latlong2/latlong.dart';
import 'package:lucky_tour/controllers/plan.dart';
import 'package:lucky_tour/controllers/user.dart';
import 'package:share_plus/share_plus.dart';
import '../../apis/user_apis.dart';
import '../../apis/strategy_apis.dart';

class MapPointsDetails extends StatefulWidget {
  const MapPointsDetails({super.key});

  @override
  State<MapPointsDetails> createState() => _MapPointsDetailsState();
}

class _MapPointsDetailsState extends State<MapPointsDetails>
    with TickerProviderStateMixin {
  PlanController planController = Get.find();
  UserController userController = Get.find();
  late final _animatedMapController = AnimatedMapController(
    vsync: this,
    duration: const Duration(milliseconds: 500),
    curve: Curves.easeInOut,
  );
  int _selectedIndex = 0;
  bool _show = false;
  bool _isLike = false;
  bool _isLoading = true;
  late LatLng _center;
  final List<AnimatedMarker> _markers = [];
  Map _info = {};

  void _prepareMarkers() {
    double lat = 0;
    double lng = 0;
    int count = 0;
    for (var i = 0;
        i < planController.selectedStrategyItem['content'].length;
        i++) {
      lat += double.parse(planController.selectedStrategyItem['content'][i]
              ['location']
          .split(',')[1]);
      lng += double.parse(planController.selectedStrategyItem['content'][i]
              ['location']
          .split(',')[0]);
      _markers.add(
        AnimatedMarker(
          width: 280.0,
          height: 250.0,
          point: LatLng(
            double.parse(planController.selectedStrategyItem['content'][i]
                    ['location']
                .split(',')[1]),
            double.parse(planController.selectedStrategyItem['content'][i]
                    ['location']
                .split(',')[0]),
          ),
          rotate: false,
          alignment: Alignment.center,
          builder: (context, animation) {
            return Stack(
              alignment: Alignment.center,
              children: [
                Stack(
                  children: [
                    IconButton(
                      icon: Icon(
                        Icons.location_on,
                        color: Colors.green[900],
                        size: 30,
                      ),
                      // focusColor: Colors.white,
                      onPressed: () {
                        setState(
                          () {
                            _selectedIndex = i;
                            _show = true;
                            _animatedMapController.animateTo(
                              dest: LatLng(
                                double.parse(planController
                                    .selectedStrategyItem['content'][i]
                                        ['location']
                                    .split(',')[1]),
                                double.parse(planController
                                    .selectedStrategyItem['content'][i]
                                        ['location']
                                    .split(',')[0]),
                              ),
                              zoom: 16.0,
                              rotation: 0.0,
                            );
                          },
                        );
                      },
                    ),
                    Positioned(
                      top: -5,
                      left: 0,
                      child: Text(
                        "${i + 1}",
                        style: TextStyle(
                          fontSize: 22,
                          fontWeight: FontWeight.w900,
                          color: Colors.green[900],
                        ),
                      ),
                    ),
                  ],
                ),
              ],
            );
          },
        ),
      );
      count++;
    }
    _center = LatLng(lat / count, lng / count);
  }

  void _getInfo() async {
    final strategy = await StrategyApi().getStrategyById({
      "bid": planController.selectedStrategyItem['bid'],
    });
    final islike = await UserApi().isLike({
      "bid": planController.selectedStrategyItem['bid'],
    });

    setState(() {
      _isLike = islike["data"];
      _info = Map.from(strategy['data']);
      _isLoading = false;
    });
  }

  void _collectOrUnCollect() async {
    final result = await UserApi().collectOrUnCollect({
      "bid": planController.selectedStrategyItem['bid'],
    });
    final strategy = await StrategyApi().getStrategyById({
      "bid": planController.selectedStrategyItem['bid'],
    });
    if (result["code"] == 200) {
      userController.isCollect.value = !userController.isCollect.value;
      final collections = await UserApi().getCollectionList();
      userController.collections.value = List.from(collections['data']);
      for (var i = 0; i < userController.collections.length; i++) {
        userController.collections[i]['content'] =
            jsonDecode(userController.collections[i]['content']);
      }
      setState(() {
        _info = Map.from(strategy['data']);
      });
    }
  }

  void _likeOrUnlike() async {
    final result = await UserApi().likeOrUnlike({
      "bid": planController.selectedStrategyItem['bid'],
    });
    if (result["code"] == 200) {
      final strategy = await StrategyApi().getStrategyById({
        "bid": planController.selectedStrategyItem['bid'],
      });
      final islike = await UserApi().isLike({
        "bid": planController.selectedStrategyItem['bid'],
      });
      setState(() {
        _isLike = islike["data"];
        _info = Map.from(strategy['data']);
      });
    }
  }

  @override
  void initState() {
    super.initState();
    _getInfo();
    _prepareMarkers();
  }

  @override
  void dispose() {
    _animatedMapController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        alignment: Alignment.topCenter,
        children: [
          Visibility(
            visible: _isLoading,
            child: const Center(
              child: SpinKitSpinningLines(
                color: Colors.green,
                size: 50.0,
              ),
            ),
          ),
          Visibility(
            visible: !_isLoading,
            child: Stack(
              alignment: Alignment.topCenter,
              children: [
                FlutterMap(
                  mapController: _animatedMapController.mapController,
                  options: MapOptions(
                      initialCenter: _center,
                      initialZoom: 14.0,
                      onTap: (point, latlng) {
                        setState(() {
                          _selectedIndex = 0;
                          _show = false;
                          _animatedMapController.animateTo(
                            dest: _center,
                            zoom: 14.0,
                            rotation: 0.0,
                          );
                        });
                      }),
                  children: [
                    TileLayer(
                      urlTemplate:
                          'https://map.geoq.cn/arcgis/rest/services/ChinaOnlineCommunity_Mobile/MapServer/tile/{z}/{y}/{x}',
                      userAgentPackageName: 'com.example.app',
                      retinaMode: true,
                    ),
                    RichAttributionWidget(
                      attributions: [
                        TextSourceAttribution(
                          'OpenStreetMap contributors',
                          onTap: () {},
                        ),
                      ],
                    ),
                    AnimatedMarkerLayer(markers: _markers),
                  ],
                ),
                Positioned(
                  top: 80,
                  height: 300,
                  width: MediaQuery.of(context).size.width - 20,
                  child: Visibility(
                    visible: _show,
                    child: Card(
                      color: Colors.green[50],
                      child: ListView(
                        padding: const EdgeInsets.all(10),
                        children: [
                          Container(
                            margin: const EdgeInsets.all(5),
                            height: 100,
                            child: ListView.builder(
                              scrollDirection: Axis.horizontal,
                              itemCount: planController
                                  .selectedStrategyItem['content']
                                      [_selectedIndex]['photos']
                                  .length,
                              itemBuilder: (context, index) => InkWell(
                                onTap: () {
                                  Get.toNamed(
                                    '/photo_view_simple',
                                    arguments: {
                                      'photo': planController
                                              .selectedStrategyItem['content']
                                          [_selectedIndex]['photos'][index]
                                    },
                                  );
                                },
                                child: Container(
                                  height: 100,
                                  width: 100,
                                  margin: const EdgeInsets.all(5),
                                  decoration: BoxDecoration(
                                    image: DecorationImage(
                                      image: NetworkImage(planController
                                                  .selectedStrategyItem[
                                                      'content'][_selectedIndex]
                                                      ['photos']
                                                  .length >
                                              0
                                          ? planController.selectedStrategyItem[
                                                  'content'][_selectedIndex]
                                              ['photos'][index]
                                          : ""),
                                      fit: BoxFit.cover,
                                    ),
                                    color: Colors.white,
                                    borderRadius: BorderRadius.circular(10),
                                  ),
                                ),
                              ),
                            ),
                          ),
                          ListTile(
                            title: Text(
                              "${_selectedIndex + 1}# ${planController.selectedStrategyItem['content'][_selectedIndex]['destination']}",
                              style: const TextStyle(
                                fontSize: 20,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            subtitle: Text(
                              planController.selectedStrategyItem['content']
                                  [_selectedIndex]['address'],
                              style: const TextStyle(
                                fontSize: 15,
                                color: Colors.grey,
                              ),
                            ),
                          ),
                          Padding(
                            padding: const EdgeInsets.all(10),
                            child: Text(
                              planController.selectedStrategyItem['content']
                                  [_selectedIndex]['comment'],
                              style: const TextStyle(fontSize: 15),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
                Positioned(
                  bottom: 20,
                  height: 80,
                  width: MediaQuery.of(context).size.width - 20,
                  child: Card(
                    color: Colors.white,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            TextButton(
                              onPressed: _collectOrUnCollect,
                              style: ButtonStyle(
                                overlayColor: MaterialStateProperty.all(
                                    Colors.transparent),
                              ),
                              child: Obx(
                                () => Column(
                                  children: [
                                    Icon(
                                      userController.isCollect.value
                                          ? Icons.star
                                          : Icons.star_border_outlined,
                                      color: Colors.green,
                                    ),
                                    Text(
                                      userController.isCollect.value
                                          ? "取消收藏"
                                          : "收藏",
                                      style: const TextStyle(
                                        fontSize: 12,
                                        color: Colors.green,
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                          ],
                        ),
                        Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            TextButton(
                              onPressed: () {
                                Share.shareUri(
                                  Uri.parse('https://www.baidu.com'),
                                );
                              },
                              style: ButtonStyle(
                                overlayColor: MaterialStateProperty.all(
                                    Colors.transparent),
                              ),
                              child: const Column(
                                children: [
                                  Icon(
                                    Icons.share_outlined,
                                    color: Colors.green,
                                  ),
                                  Text(
                                    "分享",
                                    style: TextStyle(
                                      fontSize: 12,
                                      color: Colors.green,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ],
                        ),
                        Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            TextButton(
                              onPressed: _likeOrUnlike,
                              style: ButtonStyle(
                                overlayColor: MaterialStateProperty.all(
                                    Colors.transparent),
                              ),
                              child: Column(
                                children: [
                                  Icon(
                                    _isLike
                                        ? Icons.favorite
                                        : Icons.favorite_border_outlined,
                                    color: Colors.green,
                                  ),
                                  Text(
                                    _isLike ? "取消点赞" : "点赞",
                                    style: const TextStyle(
                                      fontSize: 12,
                                      color: Colors.green,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ],
                        ),
                        Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            TextButton(
                              onPressed: () {
                                Get.toNamed('/plan_chat_rebuild');
                              },
                              style: ButtonStyle(
                                overlayColor: MaterialStateProperty.all(
                                    Colors.transparent),
                              ),
                              child: const Column(
                                children: [
                                  Icon(
                                    Icons.edit_outlined,
                                    color: Colors.green,
                                  ),
                                  Text(
                                    "创作",
                                    style: TextStyle(
                                      fontSize: 12,
                                      color: Colors.green,
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                ),
                Positioned(
                  bottom: 100,
                  right: 20,
                  child: Column(
                    children: [
                      Padding(
                        padding: const EdgeInsets.all(5),
                        child: Column(
                          children: [
                            const Icon(
                              Icons.star,
                              color: Colors.yellow,
                              size: 30,
                            ),
                            Text(
                              "${_info['favoriteCount']}",
                              style: const TextStyle(
                                fontSize: 12,
                              ),
                            ),
                          ],
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(5),
                        child: Column(
                          children: [
                            const Icon(
                              Icons.favorite,
                              color: Colors.red,
                              size: 30,
                            ),
                            Text(
                              "${_info['likeCount']}",
                              style: const TextStyle(
                                fontSize: 12,
                              ),
                            ),
                          ],
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(5),
                        child: Column(
                          children: [
                            const Icon(
                              Icons.ads_click_outlined,
                              color: Colors.blue,
                              size: 30,
                            ),
                            Text(
                              "${_info['clickVolume']}",
                              style: const TextStyle(
                                fontSize: 12,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
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
