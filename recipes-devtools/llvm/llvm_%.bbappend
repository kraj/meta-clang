PACKAGECONFIG:append:class-native = " ${@bb.utils.contains('PTEST_ENABLED', '1', 'libllvm', '', d)}"
