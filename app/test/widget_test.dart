// This is a basic Flutter widget test.
//
// To perform an interaction with a widget in your test, use the WidgetTester
// utility in the flutter_test package. For example, you can send tap and scroll
// gestures. You can also use WidgetTester to find child widgets in the widget
// tree, read text, and verify that the values of widget properties are correct.

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:lucky_tour/main.dart';
import 'package:lucky_tour/pages/profile/login_with_password.dart';

void main() {
  testWidgets('密码登录组件测试', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(const LoginWithPassword());

    await tester.enterText(find.byKey(const Key('account')), 'test@qq.com');
    await tester.enterText(find.byKey(const Key('password')), '123456');
    await tester.tap(find.text('登录'));

    // Tap the '+' icon and trigger a frame.
    // await tester.tap(find.byIcon(Icons.add));
    // await tester.pump();

    // // Verify that our counter has incremented.
    // expect(find.text('0'), findsNothing);
    // expect(find.text('1'), findsOneWidget);
  });
}
