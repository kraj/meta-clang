FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_toolchain-clang_riscv64 = " file://0001-gdb-Link-with-latomic-for-riscv-clang-alone.patch "
