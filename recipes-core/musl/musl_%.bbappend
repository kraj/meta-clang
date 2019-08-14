DEPENDS_append_toolchain-clang = " clang-cross-${TARGET_ARCH}"
TOOLCHAIN_x86-x32 = "gcc"
TOOLCHAIN_riscv64 = "gcc"
inherit lto
