// import 'package:dio/dio.dart';
import '../utils/request.dart';

class PlanApi {
  static PlanApi? _instance;

  factory PlanApi() => _instance ?? PlanApi._internal();

  static PlanApi? get instance => _instance ?? PlanApi._internal();

  PlanApi._internal();

  /// 获取所有城市
  Future getCities(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/data/getCityDescription',
      method: DioMethod.get,
      params: params,
    );
  }

  /// 获取经纬度
  Future getLatlng(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/data/getGeoCode',
      method: DioMethod.get,
      params: params,
    );
  }

  /// GPT 生成计划
  Future generatePlanByChat(data) async {
    return await ComputeRequest().request(
      '/generateFreeModeTravelItinerary',
      method: DioMethod.post,
      data: data,
    );
  }

  /// 生成计划
  Future generatePlan(Map<String, dynamic> data) async {
    return await SeverRequest().request(
      '/plan/create',
      method: DioMethod.post,
      data: data,
    );
  }

  /// 获取计划列表
  Future getPlanList(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/plan/getByUid',
      method: DioMethod.get,
      params: params,
    );
  }

  ///根据pid获取计划
  Future getPlanByPid(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/plan/getByPid',
      method: DioMethod.get,
      params: params,
    );
  }

  ///修改计划返回结果
  Future updatePlan(data) async {
    return await ComputeRequest().request(
      '/updatePlan',
      method: DioMethod.post,
      data: data,
    );
  }

  ///确认是否修改计划
  Future confirmUpdatePlan(data) async {
    return await ComputeRequest().request(
      '/updatePlan1',
      method: DioMethod.post,
      data: data,
    );
  }

  ///AI导游
  Future getAiGuide(data) async {
    return await ComputeRequest().request(
      '/guide',
      method: DioMethod.post,
      data: data,
    );
  }

  ///删除计划
  Future deletePlan(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/plan/delete',
      method: DioMethod.get,
      params: params,
    );
  }
}
