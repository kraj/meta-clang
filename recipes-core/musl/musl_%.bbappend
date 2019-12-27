DEPENDS_append_toolchain-clang = " clang-cross-${TARGET_ARCH}"
TOOLCHAIN_x86-x32 = "gcc"
TOOLCHAIN_riscv64 = "gcc"
TOOLCHAIN_powerpc64 = "gcc"

inherit lto

# workaround until https://bugs.llvm.org/show_bug.cgi?id=44384
# is fixed
do_configure_prepend_toolchain-clang () {
    sed -i -e '/frounding-math/d' ${S}/configure
}
