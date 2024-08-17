import 'package:dio/dio.dart';
import 'package:get_storage/get_storage.dart';

/// 请求方法:枚举类型
enum DioMethod {
  get,
  post,
  put,
  delete,
  patch,
  head,
}

// 创建请求类：封装dio
class SeverRequest {
  /// 单例模式
  static SeverRequest? _instance;

  // 工厂函数：执行初始化
  factory SeverRequest() => _instance ?? SeverRequest._internal();

  // 获取实例对象时，如果有实例对象就返回，没有就初始化
  static SeverRequest? get instance => _instance ?? SeverRequest._internal();

  /// Dio实例
  static Dio _dio = Dio();

  /// 初始化
  SeverRequest._internal() {
    // 初始化基本选项
    BaseOptions options = BaseOptions(
        baseUrl: 'http://172.29.185.216:58087',
        connectTimeout: const Duration(seconds: 150),
        receiveTimeout: const Duration(seconds: 150));
    _instance = this;
    // 初始化dio
    _dio = Dio(options);
    // 添加拦截器
    _dio.interceptors.add(InterceptorsWrapper(
        onRequest: _onRequest, onResponse: _onResponse, onError: _onError));
  }

  /// 请求拦截器
  void _onRequest(RequestOptions options, RequestInterceptorHandler handler) {
    options.headers["X-token"] = GetStorage().read('token') ?? '';
    handler.next(options);
  }

  /// 相应拦截器
  void _onResponse(
      Response response, ResponseInterceptorHandler handler) async {
    handler.next(response);
  }

  /// 错误处理: 网络错误等
  void _onError(DioException error, ErrorInterceptorHandler handler) {
    handler.next(error);
  }

  /// 请求类：支持异步请求操作
  Future<T> request<T>(
    String path, {
    DioMethod method = DioMethod.get,
    Map<String, dynamic>? params,
    dynamic data,
    CancelToken? cancelToken,
    Options? options,
    ProgressCallback? onSendProgress,
    ProgressCallback? onReceiveProgress,
  }) async {
    const methodValues = {
      DioMethod.get: 'get',
      DioMethod.post: 'post',
      DioMethod.put: 'put',
      DioMethod.delete: 'delete',
      DioMethod.patch: 'patch',
      DioMethod.head: 'head'
    };
    // 默认配置选项
    options ??= Options(method: methodValues[method]);
    try {
      Response response;
      // 开始发送请求
      response = await _dio.request(path,
          data: data,
          queryParameters: params,
          cancelToken: cancelToken,
          options: options,
          onSendProgress: onSendProgress,
          onReceiveProgress: onReceiveProgress);
      return response.data;
    } on DioException catch (e) {
      print("发送请求异常: $e");
      rethrow;
    }
  }

  /// 开启日志打印
  /// 需要打印日志的接口在接口请求前 SeverRequest.instance?.openLog();
  void openLog() {
    _dio.interceptors
        .add(LogInterceptor(responseHeader: false, responseBody: true));
  }
}

// 创建请求类：封装dio
class ComputeRequest {
  /// 单例模式
  static ComputeRequest? _instance;

  // 工厂函数：执行初始化
  factory ComputeRequest() => _instance ?? ComputeRequest._internal();

  // 获取实例对象时，如果有实例对象就返回，没有就初始化
  static ComputeRequest? get instance =>
      _instance ?? ComputeRequest._internal();

  /// Dio实例
  static Dio _dio = Dio();

  /// 初始化
  ComputeRequest._internal() {
    // 初始化基本选项
    BaseOptions options = BaseOptions(
        baseUrl: 'http://172.29.20.72:5000',
        connectTimeout: const Duration(seconds: 100),
        receiveTimeout: const Duration(seconds: 120));
    _instance = this;
    // 初始化dio
    _dio = Dio(options);
    // 添加拦截器
    _dio.interceptors.add(InterceptorsWrapper(
        onRequest: _onRequest, onResponse: _onResponse, onError: _onError));
  }

  /// 请求拦截器
  void _onRequest(RequestOptions options, RequestInterceptorHandler handler) {
    // options.headers["X-token"] = GetStorage().read('token') ?? '';
    handler.next(options);
  }

  /// 相应拦截器
  void _onResponse(
      Response response, ResponseInterceptorHandler handler) async {
    handler.next(response);
  }

  /// 错误处理: 网络错误等
  void _onError(DioException error, ErrorInterceptorHandler handler) {
    handler.next(error);
  }

  /// 请求类：支持异步请求操作
  Future<T> request<T>(
    String path, {
    DioMethod method = DioMethod.get,
    Map<String, dynamic>? params,
    dynamic data,
    CancelToken? cancelToken,
    Options? options,
    ProgressCallback? onSendProgress,
    ProgressCallback? onReceiveProgress,
  }) async {
    const methodValues = {
      DioMethod.get: 'get',
      DioMethod.post: 'post',
      DioMethod.put: 'put',
      DioMethod.delete: 'delete',
      DioMethod.patch: 'patch',
      DioMethod.head: 'head'
    };
    // 默认配置选项
    options ??= Options(method: methodValues[method]);
    try {
      Response response;
      // 开始发送请求
      response = await _dio.request(path,
          data: data,
          queryParameters: params,
          cancelToken: cancelToken,
          options: options,
          onSendProgress: onSendProgress,
          onReceiveProgress: onReceiveProgress);
      return response.data;
    } on DioException catch (e) {
      print("发送请求异常: $e");
      rethrow;
    }
  }

  /// 开启日志打印
  /// 需要打印日志的接口在接口请求前 ComputeRequest.instance?.openLog();
  void openLog() {
    _dio.interceptors
        .add(LogInterceptor(responseHeader: false, responseBody: true));
  }
}
