CLANGCROSSCANADIAN = "${@bb.utils.contains('CLANGSDK', '1', 'clang-cross-canadian-${TRANSLATED_TARGET_ARCH}', '', d)}"
RDEPENDS_${PN} += "${@all_multilib_tune_values(d, 'CLANGCROSSCANADIAN')}"
