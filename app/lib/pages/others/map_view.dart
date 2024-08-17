import 'package:amap_flutter_map/amap_flutter_map.dart';
import 'package:amap_flutter_base/amap_flutter_base.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:lucky_tour/controllers/plan.dart';

class MapView extends StatefulWidget {
  const MapView({Key? key}) : super(key: key);

  @override
  State<MapView> createState() => _MapViewState();
}

class _MapViewState extends State<MapView> {
  PlanController planController = Get.find();
  final List<Widget> _approvalNumberWidget = [];
  AMapWidget? map;
  AMapController? _mapController;

  @override
  void initState() {
    super.initState();
    map = AMapWidget(
      privacyStatement: const AMapPrivacyStatement(
          hasShow: true, hasAgree: true, hasContains: true),
      onMapCreated: onMapCreated,
      apiKey: const AMapApiKey(
        androidKey: "224007cc30da67813ca411fdac01ff99",
      ),
      initialCameraPosition: CameraPosition(
        target: LatLng(planController.selectedCityLatlng['latitude'] ?? 0,
            planController.selectedCityLatlng['longitude'] ?? 0),
        zoom: 10.0,
      ),
    );
    getApprovalNumber();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return ConstrainedBox(
      constraints: const BoxConstraints.expand(),
      child: Stack(
        alignment: Alignment.center,
        children: [
          SizedBox(
            height: MediaQuery.of(context).size.height,
            width: MediaQuery.of(context).size.width,
            child: map,
          ),
          Positioned(
            right: 10,
            top: 15,
            child: Container(
              alignment: Alignment.centerLeft,
              child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: _approvalNumberWidget),
            ),
          ),
        ],
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
