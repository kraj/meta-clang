SUMMARY = "BPF Compiler Collection (BCC)"
HOMEPAGE = "https://github.com/iovisor/bcc"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit cmake python3native manpages

DEPENDS += "bison-native \
            flex-native \
            elfutils \
            ${LUAJIT} \
            clang \
            "

LUAJIT ?= "luajit"
LUAJIT_powerpc64le = ""
LUAJIT_powerpc64 = ""

RDEPENDS_${PN} += "bash python3 python3-core python3-setuptools xz"

# Don't fetch submodules otherwise we fetch an older, vendored libbpf version
# We use a dynamic libbpf library instead
SRC_URI = "git://github.com/iovisor/bcc \
           file://0001-python-CMakeLists.txt-Remove-check-for-host-etc-debi.patch \
           file://0001-tools-trace.py-Fix-failing-to-exit.patch \
           file://0001-CMakeLists.txt-override-the-PY_CMD_ESCAPED.patch \
           file://0001-fix-compilation-issues-with-latest-llvm12-trunk.patch \
           file://0002-fix-compilation-error-with-latest-llvm12-trunk.patch \
           file://0001-cmake-link-dynamically-to-libclang-cpp-if-found-and-.patch \
           file://0002-cmake-always-link-to-packaged-libbpf-if-CMAKE_USE_LI.patch \
           file://0003-Remove-libbcc-no-libbpf-shared-library-change-libbcc.patch \
           file://0004-cmake-look-for-either-static-or-dynamic-libraries.patch \
           "

DEPENDS += "libbpf"

SRCREV = "ad5b82a5196b222ed2cdc738d8444e8c9546a77f"

S = "${WORKDIR}/git"

PACKAGECONFIG ??= ""
PACKAGECONFIG[manpages] = "-DENABLE_MAN=ON,-DENABLE_MAN=OFF,"

EXTRA_OECMAKE = " \
    -DENABLE_LLVM_SHARED=ON \
    -DENABLE_CLANG_JIT=ON \
    -DLLVM_PACKAGE_VERSION=${LLVMVERSION} \
    -DPYTHON_CMD=${PYTHON} \
    -DPYTHON_FLAGS=--install-lib=${PYTHON_SITEPACKAGES_DIR} \
    -DCMAKE_USE_LIBBPF_PACKAGE=ON \
"

do_install_append() {
        sed -e 's@#!/usr/bin/python@#!/usr/bin/env python3@g' \
            -i $(find ${D}${datadir}/${PN} -type f)
}

FILES_${PN} += "${PYTHON_SITEPACKAGES_DIR}"
FILES_${PN}-doc += "${datadir}/${PN}/man"

COMPATIBLE_HOST = "(x86_64.*|aarch64.*|powerpc64.*)-linux"
