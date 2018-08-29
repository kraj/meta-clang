CLANGCROSSCANADIAN ?= "clang-cross-canadian-${TRANSLATED_TARGET_ARCH}"
CLANGCROSSCANADIAN_riscv64 = ""
RDEPENDS_${PN} += "\
    ${@all_multilib_tune_values(d, 'CLANGCROSSCANADIAN')} \
"
