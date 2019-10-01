FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

GN_ARGS += "use_lld=true use_gold=false"

UNWINDLIB_toolchain-clang = "--unwindlib=libgcc"
