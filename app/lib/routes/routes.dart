import 'package:get/get.dart';

import '../pages/root/root.dart';
import '../pages/profile/login_with_password.dart';
import '../pages/profile/login_with_verification.dart';
import '../pages/profile/register.dart';
import '../pages/profile/auth_code_login.dart';
import '../pages/profile/auth_code_register.dart';
import '../pages/profile/notifications.dart';
import '../pages/profile/user_info.dart';
import '../pages/profile/edit_user_info.dart';
import '../pages/profile/login_or_register.dart';
import '../pages/plan/planbuilder/destination.dart';
import '../pages/plan/planbuilder/destination_info.dart';
import '../pages/plan/planbuilder/customer.dart';
import '../pages/plan/planbuilder/date.dart';
import '../pages/plan/planbuilder/budget.dart';
import '../pages/plan/planbuilder/traffic.dart';
import '../pages/plan/planbuilder/hobby.dart';
import '../pages/plan/planbuilder/all.dart';
import '../pages/plan/plan_chat_rebuild.dart';
import '../pages/plan/plan_chat_create.dart';
import '../pages/plan/plan_chat_update.dart';
import '../pages/plan/plan_show.dart';
import '../pages/plan/plan_item_details.dart';
import '../pages/plan/strategy.dart';
import '../pages/plan/strategy_confirm.dart';
import '../pages/discover/person_details.dart';
import '../pages/others/photo_view_simple.dart';
import '../pages/others/map_points_view.dart';
import '../pages/others/map_points_details.dart';
import '../pages/others/chat_guide.dart';

class AppPage {
  static final routes = [
    GetPage(name: '/home', page: () => const RootPage()),
    GetPage(
        name: '/login_with_password', page: () => const LoginWithPassword()),
    GetPage(
        name: '/login_with_verification',
        page: () => const LoginWithVerification()),
    GetPage(name: '/register', page: () => const Register()),
    GetPage(name: '/auth_code_login', page: () => const AuthCodeLogin()),
    GetPage(name: '/auth_code_register', page: () => const AuthCodeRegister()),
    GetPage(name: '/notifications', page: () => const Notifications()),
    GetPage(name: '/user_info', page: () => const UserInfo()),
    GetPage(name: '/edit_user_info', page: () => const EditUserInfo()),
    GetPage(name: '/login_or_register', page: () => const LoginOrRegister()),
    GetPage(name: '/destination', page: () => const Destination()),
    GetPage(name: '/destination_info', page: () => const DestinationInfo()),
    GetPage(name: '/customer', page: () => const Customer()),
    GetPage(name: '/date', page: () => const Date()),
    GetPage(name: '/budget', page: () => const Budget()),
    GetPage(name: '/traffic', page: () => const Traffic()),
    GetPage(name: '/hobby', page: () => const Hobby()),
    GetPage(name: '/all', page: () => const All()),
    GetPage(name: '/plan_chat_rebuild', page: () => const PlanChatRebuild()),
    GetPage(name: '/plan_chat_create', page: () => const PlanChatCreate()),
    GetPage(name: '/plan_chat_update', page: () => const PlanChatUpdate()),
    GetPage(name: '/plan_show', page: () => const PlanShow()),
    GetPage(name: '/plan_item_details', page: () => const PlanItemDetails()),
    GetPage(name: '/person_details', page: () => const PersonDetails()),
    GetPage(name: '/strategy', page: () => const Strategy()),
    GetPage(name: '/strategy_confirm', page: () => const StrategyConfirm()),
    GetPage(
        name: '/photo_view_simple', page: () => const PhotoViewSimpleScreen()),
    GetPage(name: '/map_points_view', page: () => const MapPointsView()),
    GetPage(name: '/map_points_details', page: () => const MapPointsDetails()),
    GetPage(name: '/chat_guide', page: () => const ChatGuide()),
  ];
}
