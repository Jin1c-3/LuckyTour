import 'package:amap_flutter_map/amap_flutter_map.dart';
import 'package:amap_flutter_base/amap_flutter_base.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:lucky_tour/controllers/plan.dart';

class MapPointsView extends StatefulWidget {
  const MapPointsView({super.key});

  @override
  State<MapPointsView> createState() => _MapPointsViewState();
}

class _MapPointsViewState extends State<MapPointsView> {
  PlanController planController = Get.find();
  final List<Widget> _approvalNumberWidget = [];
  final Map<String, Marker> _initMarkerMap = <String, Marker>{};
  AMapWidget? map;
  AMapController? _mapController;

  @override
  void initState() {
    super.initState();

    double centerLatitude = 0;
    double centerLongitude = 0;
    int count = 0;

    for (var key in planController.selectedPlanInfo['content'].keys) {
      var item = planController.selectedPlanInfo['content'][key];
      for (var i = 0; i < item.length; i++) {
        final location = item[i]['location'].split(",");
        centerLatitude += double.parse(location[1]);
        centerLongitude += double.parse(location[0]);
        count++;
        Marker marker = Marker(
          icon:
              BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueGreen),
          position:
              LatLng(double.parse(location[1]), double.parse(location[0])),
          infoWindow: InfoWindow(
            title: item[i]['name'],
            snippet: item[i]['address'],
          ),
        );
        _initMarkerMap[marker.id] = marker;
      }
    }

    map = AMapWidget(
      privacyStatement: const AMapPrivacyStatement(
          hasShow: true, hasAgree: true, hasContains: true),
      onMapCreated: onMapCreated,
      apiKey: const AMapApiKey(
        androidKey: "224007cc30da67813ca411fdac01ff99",
      ),
      markers: Set<Marker>.of(_initMarkerMap.values),
      initialCameraPosition: CameraPosition(
        target: LatLng(centerLatitude / count, centerLongitude / count),
        zoom: 12.0,
      ),
    );

    getApprovalNumber();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: SizedBox(
        width: MediaQuery.of(context).size.width,
        height: MediaQuery.of(context).size.height,
        child: map,
      ),
    );
  }

  void onMapCreated(AMapController controller) {
    setState(() {
      _mapController = controller;
    });
  }

  /// 获取审图号
  void getApprovalNumber() async {
    //普通地图审图号
    String? mapContentApprovalNumber =
        await _mapController?.getMapContentApprovalNumber();
    //卫星地图审图号
    String? satelliteImageApprovalNumber =
        await _mapController?.getSatelliteImageApprovalNumber();
    setState(() {
      if (null != mapContentApprovalNumber) {
        _approvalNumberWidget.add(Text(mapContentApprovalNumber));
      }
      if (null != satelliteImageApprovalNumber) {
        _approvalNumberWidget.add(Text(satelliteImageApprovalNumber));
      }
    });
  }
}
