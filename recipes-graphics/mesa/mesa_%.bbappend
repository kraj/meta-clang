FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:toolchain-clang = " file://clang13.patch "

LDFLAGS:append:toolchain-clang = " -latomic -lm"
DEPENDS:append:toolchain-clang = " libatomic-ops"

EXTRA_OEMASON:append:toolchain-clang:x86 = " -Dasm=false"
EXTRA_OEMASON:append:toolchain-clang:x86-64 = " -Dasm=false"

export YOCTO_ALTERNATE_EXE_PATH = "${STAGING_LIBDIR}/llvm-config"

PACKAGECONFIG[gallium-llvm] = "-Dllvm=true -Dshared-llvm=true -Ddraw-use-llvm=true,-Dllvm=false,clang clang-native elfutils"
