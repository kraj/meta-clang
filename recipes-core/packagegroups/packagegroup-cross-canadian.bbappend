RDEPENDS_${PN} += "\
    clang-cross-canadian-${@' clang-cross-canadian-'.join(all_multilib_tune_values(d,'TRANSLATED_TARGET_ARCH').split())} \
    "
