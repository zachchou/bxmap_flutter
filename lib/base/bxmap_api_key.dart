
import 'package:flutter/cupertino.dart';

class BXMapApiKey {
  ///iOS平台的key
  final String? iosKey;

  ///Android平台的key
  final String? androidKey;

  const BXMapApiKey({this.iosKey, this.androidKey});

  dynamic toMap() {
    final Map<String, dynamic> json = <String, dynamic>{};

    void addIfPresent(String fieldName, dynamic value) {
      if (value != null) {
        json[fieldName] = value;
      }
    }
    addIfPresent('androidKey', androidKey);
    addIfPresent('iosKey', iosKey);
    return json;
  }

  @override
  bool operator ==(dynamic other) {
    // TODO: implement ==
    if (identical(this, other)) return true;
    if (runtimeType != other.runtimeType) return false;
    final BXMapApiKey typedOther = other;
    return androidKey == typedOther.androidKey && iosKey == typedOther.iosKey;
  }

  @override
  // TODO: implement hashCode
  int get hashCode => hashValues(androidKey, iosKey);

  @override
  String toString() {
    // TODO: implement toString
    return 'BXMapApiKey(androidKey: $androidKey, iosKey: $iosKey)';
  }

}