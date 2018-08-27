LDFLAGS_append_toolchain-clang = " -latomic -lm"
DEPENDS_append_toolchain-clang = " libatomic-ops"

EXTRA_OECONF_append_toolchain-clang_x86 = " --disable-asm"
EXTRA_OECONF_append_toolchain-clang_x86-64 = " --disable-asm"
