FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

LDFLAGS:append:toolchain-clang:riscv32 = " -Wl,--no-relax"
