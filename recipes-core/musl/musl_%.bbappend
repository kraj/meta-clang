DEPENDS:append:toolchain-clang = " clang-cross-${TARGET_ARCH}"
DEPENDS:remove:toolchain-clang = "virtual/${TARGET_PREFIX}gcc"
TOOLCHAIN:x86-x32 = "gcc"

# crashes seen in malloc@plt
# Dump of assembler code for function malloc@got.plt:
# => 0x3f7fc2e8 <+0>:     addis   r27,r20,-22264

TOOLCHAIN:powerpc = "gcc"
