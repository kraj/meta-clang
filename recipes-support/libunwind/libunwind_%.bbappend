FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
# For clang libunwind.a comes from libcxx
EXTRA_OECONF_remove_toolchain-clang = "--enable-static"
EXTRA_OECONF_append_toolchain-clang = " --disable-static"
