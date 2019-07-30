SUMMARY = "Helper script for OE's llvm support"
LICENSE = "Apache-2.0-with-LLVM-exception"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0-with-LLVM-exception;md5=0bcd48c3bdfef0c9d9fd17726e4b7dab"

SRC_URI = "file://llvm-config"

S = "${WORKDIR}"

ALLOW_EMPTY_${PN} = "1"
SYSROOT_PREPROCESS_FUNCS_append_class-target = " llvm_common_sysroot_preprocess"

llvm_common_sysroot_preprocess() {
    install -d ${SYSROOT_DESTDIR}${bindir_crossscripts}/
    install -m 0755 ${WORKDIR}/llvm-config ${SYSROOT_DESTDIR}${bindir_crossscripts}/
}

do_install_class-native() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/llvm-config ${D}${bindir}
}

BBCLASSEXTEND = "native"
