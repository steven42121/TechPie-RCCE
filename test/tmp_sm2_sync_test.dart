import 'package:flutter_test/flutter_test.dart';
import 'package:techpie/features/campus_card/data/crypto/sm2_offline_crypto.dart';

List<int> hex(String s) => [
      for (var i = 0; i < s.length; i += 2) int.parse(s.substring(i, i + 2), radix: 16),
    ];

void main() {
  const x = 'BF0230DA44A432BEC6D7B478D5B415F455BAB2C609BA5357C2EF27EAF110C91F';
  const y = 'C476D3A71CCC02B60AE6A495FDFDCAF0B6B3D354467BA81CD30802E21E6350A9';
  const msg = '56385A75CF6C6ADF0240D1';
  final crypto = Sm2OfflineCrypto();

  test('control: old-js fixture signature verifies', () {
    expect(
      crypto.verify(
        publicKeyXHex: x,
        publicKeyYHex: y,
        message: hex(msg),
        signatureHex:
            '4913FD51381D4D12497D903CDC0B57089995290036AAF83F72A3A4AC37B875D2130992C9791C444469C5A9B09BDE8F17C029693D36629FDBD26974566548F11C',
      ),
      isTrue,
    );
  });

  test('experiment: canonical-js signature verifies', () {
    expect(
      crypto.verify(
        publicKeyXHex: x,
        publicKeyYHex: y,
        message: hex(msg),
        signatureHex:
            '5B6ABF2798DEF0808416132622EC897D7CD351EE37F47B2F93376088C468396DEE4CC3886CD5979992B4F85C854112316316E843906880E3CDE029907ACBB792',
      ),
      isTrue,
    );
  });
}
