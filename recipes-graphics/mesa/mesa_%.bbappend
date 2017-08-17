FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_toolchain-clang = "\
            file://0001-Clang-doesn-t-have-64bit-__atomic-builtins-on-i386.patch \
            file://0002-Add-prototypes-for-64bit-atomics-fallback.patch \
"
