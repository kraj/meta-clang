FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_toolchain-clang = " file://clang13.patch "

LDFLAGS_append_toolchain-clang = " -latomic -lm"
DEPENDS_append_toolchain-clang = " libatomic-ops"

EXTRA_OEMASON_append_toolchain-clang_x86 = " -Dasm=false"
EXTRA_OEMASON_append_toolchain-clang_x86-64 = " -Dasm=false"

export YOCTO_ALTERNATE_EXE_PATH = "${STAGING_LIBDIR}/llvm-config"

PACKAGECONFIG[gallium-llvm] = "-Dllvm=true -Dshared-llvm=true -Ddraw-use-llvm=true,-Dllvm=false,clang clang-native elfutils"

# see https://bugs.llvm.org/show_bug.cgi?id=51126
TOOLCHAIN_aarch64 = "gcc"
