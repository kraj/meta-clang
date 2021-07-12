FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_toolchain-clang = " file://intel-mediasdk-20.5.1-no-lgcc.patch"
