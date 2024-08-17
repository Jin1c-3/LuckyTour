import '../utils/request.dart';

class UserApi {
  static UserApi? _instance;

  factory UserApi() => _instance ?? UserApi._internal();

  static UserApi? get instance => _instance ?? UserApi._internal();

  UserApi._internal();

  /// 登录
  Future login(Map<String, dynamic> data) async {
    return await SeverRequest().request(
      '/auth/login',
      method: DioMethod.post,
      data: data,
    );
  }

  /// 获取用户信息
  Future getUserInfo() async {
    return await SeverRequest().request(
      '/user/getinfo',
      method: DioMethod.get,
    );
  }

  ///用户登出
  Future logout() async {
    final response = await SeverRequest().request(
      '/auth/logout',
      method: DioMethod.post,
    );
    return response;
  }

  ///登录获取验证码
  Future getValidateCodeLogin(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/auth/getCodeInDB',
      method: DioMethod.get,
      params: params,
    );
  }

  ///注册获取验证码
  Future getValidateCodeRegister(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/auth/getCodeNotInDB',
      method: DioMethod.get,
      params: params,
    );
  }

  /// 创建用户
  Future createUser(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/user/new',
      method: DioMethod.get,
      params: params,
    );
  }

  /// 修改用户信息
  Future updateUserInfo(params, data) async {
    return await SeverRequest().request(
      '/user/update',
      method: DioMethod.post,
      params: params,
      data: data,
    );
  }

  ///获取粉丝列表
  Future getFollowers() async {
    return await SeverRequest().request(
      '/follow/getFollowerByRequest',
      method: DioMethod.get,
    );
  }

  ///获取关注列表
  Future getFollowings() async {
    return await SeverRequest().request(
      '/follow/getFollowedByRequest',
      method: DioMethod.get,
    );
  }

  ///获取粉丝数
  Future getFollowerCount(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/follow/getFollowerCount',
      method: DioMethod.get,
      params: params,
    );
  }

  ///获取关注数
  Future getFollowingCount(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/follow/getFollowedCount',
      method: DioMethod.get,
      params: params,
    );
  }

  ///关注或取消关注
  Future followOrUnfollow(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/follow/followUnfollow',
      method: DioMethod.get,
      params: params,
    );
  }

  ///获取收藏列表
  Future getCollectionList() async {
    return await SeverRequest().request(
      '/favorite/getFavorByRequest',
      method: DioMethod.get,
    );
  }

  ///获取其他用户信息
  Future getOtherUserInfo(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/user/getinfoById',
      method: DioMethod.get,
      params: params,
    );
  }

  ///获取用户的博客列表
  Future getUserBlogList(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/blog/getBlogByUid',
      method: DioMethod.get,
      params: params,
    );
  }

  ///判断用户是否点赞
  Future isLike(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/liked/isLiked',
      method: DioMethod.get,
      params: params,
    );
  }

  ///收藏或取消收藏
  Future collectOrUnCollect(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/favorite/favorUnfavor',
      method: DioMethod.get,
      params: params,
    );
  }

  ///点赞或取消点赞
  Future likeOrUnlike(Map<String, dynamic> params) async {
    return await SeverRequest().request(
      '/liked/likeUnliked',
      method: DioMethod.get,
      params: params,
    );
  }

  ///监控用户状态
  Future monitorUserStatus(data) async {
    return await SeverRequest().request(
      '/dynamic/monitor',
      method: DioMethod.post,
      data: data,
    );
  }
}
