FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:toolchain-clang = " file://intel-mediasdk-20.5.1-no-lgcc.patch"
