DEPENDS:append:toolchain-clang = " clang-cross-${TARGET_ARCH}"
DEPENDS:remove:toolchain-clang = "virtual/${TARGET_PREFIX}gcc"
TOOLCHAIN:x86-x32 = "gcc"
TOOLCHAIN:powerpc64 = "gcc"
