LDFLAGS_append_toolchain-clang = " -latomic -lm"
DEPENDS_append_toolchain-clang = " libatomic-ops"

EXTRA_OECONF_append_toolchain-clang_x86 = " --disable-asm"
EXTRA_OECONF_append_toolchain-clang_x86-64 = " --disable-asm"

export YOCTO_ALTERNATE_EXE_PATH = "${STAGING_LIBDIR}/llvm-config"

PACKAGECONFIG[gallium-llvm] = "-Dllvm=true -Dshared-llvm=true, -Dllvm=false, clang clang-native \
${@'elfutils' if ${GALLIUMDRIVERS_LLVM33_ENABLED} else ''}"
