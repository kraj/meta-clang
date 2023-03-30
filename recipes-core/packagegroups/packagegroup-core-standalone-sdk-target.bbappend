RRECOMMENDS:${PN}:append:toolchain-clang = "${@bb.utils.contains('CLANGSDK', '1', ' libcxx-dev libcxx-staticdev compiler-rt-dev compiler-rt-staticdev', '', d)}"
