import 'package:flutter/material.dart';

class ChineseTextEditController extends TextEditingController {
  String completeText = '';

  @override
  TextSpan buildTextSpan(
      {required BuildContext context, TextStyle? style, bool? withComposing}) {
    if (!value.composing.isValid || !withComposing!) {
      if (completeText != value.text) {
        completeText = value.text;
        WidgetsBinding.instance.addPostFrameCallback((_) {
          notifyListeners();
        });
      }
      return TextSpan(style: style, text: text);
    }

    ///返回输入样式，可自定义样式
    final TextStyle composingStyle = style!.merge(
      const TextStyle(decoration: TextDecoration.underline),
    );
    return TextSpan(style: style, children: <TextSpan>[
      TextSpan(text: value.composing.textBefore(value.text)),
      TextSpan(
        style: composingStyle,
        text: value.composing.isValid && !value.composing.isCollapsed
            ? value.composing.textInside(value.text)
            : "",
      ),
      TextSpan(text: value.composing.textAfter(value.text)),
    ]);
  }
}
