FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

LDLIBS_append_toolchain-clang_riscv64 = " -latomic"
LDLIBS_append_toolchain-clang_riscv32 = " -latomic"

do_configure_prepend_toolchain-clang () {
    export LDLIBS="${LDLIBS}"
}
