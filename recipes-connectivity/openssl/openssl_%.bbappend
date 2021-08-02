FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

LDLIBS:append:toolchain-clang:riscv64 = " -latomic"
LDLIBS:append:toolchain-clang:riscv32 = " -latomic"

do_configure:prepend:toolchain-clang () {
    export LDLIBS="${LDLIBS}"
}
