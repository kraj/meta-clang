FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append_toolchain-clang = "\
    file://0001-Turn-ptr_to_globals-and-bb_errno-to-be-non-const.patch \
    "

TOOLCHAIN_x86 = "gcc"
TOOLCHAIN_riscv64 = "gcc"
