FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

inherit clang-native

# fix build with clang-18
SRC_URI:append:runtime-llvm = " file://0001-sancov-Switch-to-OptTable-from-llvm-cl.patch"
