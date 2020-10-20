CLANGCROSSCANADIAN = "clang-cross-canadian-${TRANSLATED_TARGET_ARCH}"
CLANGCROSSCANADIANDEPS += "${@all_multilib_tune_values(d, 'CLANGCROSSCANADIAN')}"
RDEPENDS_${PN} += "${@bb.utils.contains('CLANGSDK', '1', '${CLANGCROSSCANADIANDEPS}', '', d)}"
