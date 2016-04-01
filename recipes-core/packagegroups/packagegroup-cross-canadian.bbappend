CLANG = "clang-cross-canadian-${TRANSLATED_TARGET_ARCH}"
RDEPENDS_${PN} += "\
    ${@all_multilib_tune_values(d, 'CLANG')} \
"
