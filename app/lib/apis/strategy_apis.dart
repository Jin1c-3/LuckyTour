import '../utils/request.dart';

class StrategyApi {
  static StrategyApi? _instance;

  factory StrategyApi() => _instance ?? StrategyApi._internal();

  static StrategyApi? get instance => _instance ?? StrategyApi._internal();

  StrategyApi._internal();

  /// 获取攻略信息通过id
  Future getStrategyById(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/blog/getBlogByBid',
      method: DioMethod.get,
      params: params,
    );
  }

  ///增加一次点击量通过id
  Future addClickCount(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/blog/addClick',
      method: DioMethod.get,
      params: params,
    );
  }

  ///ai生成攻略文段
  Future aiCreateStrategy(data) async {
    return await ComputeRequest()
        .request('/travel_notes', method: DioMethod.post, data: data);
  }

  ///创建攻略
  Future createStrategy(data) async {
    return await SeverRequest()
        .request('/blog/create', method: DioMethod.post, data: data);
  }

  ///搜索攻略
  Future searchStrategy(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/blog/search',
      method: DioMethod.get,
      params: params,
    );
  }

  ///删除攻略
  Future deleteStrategy(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/blog/delete',
      method: DioMethod.get,
      params: params,
    );
  }

  ///模板生成计划
  Future createPlanByTemplate(data) async {
    return await ComputeRequest().request(
      '/plan_template',
      method: DioMethod.post,
      data: data,
    );
  }
}
