FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:toolchain-clang:riscv64 = " file://0001-gdb-Link-with-latomic-for-riscv-clang-alone.patch "
