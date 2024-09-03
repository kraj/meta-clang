SUMMARY = "BPF Compiler Collection (BCC)"
HOMEPAGE = "https://github.com/iovisor/bcc"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit cmake python3native manpages ptest

DEPENDS += "bison-native \
            flex-native \
            zip-native \
            flex \
            elfutils \
            clang \
            libbpf \
            python3-setuptools-native \
            "

RDEPENDS:${PN} += "bash python3 python3-core python3-setuptools xz"
RDEPENDS:${PN}-ptest = "cmake python3 python3-netaddr python3-pyroute2"

SRC_URI = "gitsm://github.com/iovisor/bcc;branch=master;protocol=https \
           file://0001-CMakeLists.txt-override-the-PY_CMD_ESCAPED.patch \
           file://0001-Vendor-just-enough-extra-headers-to-allow-libbpf-to-.patch \
           file://0001-tests-cc-Use-c-14-standard.patch \
           file://0001-CMakeLists.txt-don-t-modify-.gitconfig-on-build-host.patch \
           file://run-ptest \
           file://ptest_wrapper.sh \
           "

SRCREV = "1d8daaa395f066b328a56a36fbd40a0de3a7b3c1"

PV .= "+git"

S = "${WORKDIR}/git"

PACKAGECONFIG ??= "examples"
PACKAGECONFIG:remove:libc-musl = "examples"

PACKAGECONFIG[manpages] = "-DENABLE_MAN=ON,-DENABLE_MAN=OFF,"
PACKAGECONFIG[examples] = "-DENABLE_EXAMPLES=ON,-DENABLE_EXAMPLES=OFF,"

EXTRA_OECMAKE = " \
    -DCMAKE_USE_LIBBPF_PACKAGE=ON \
    -DENABLE_LLVM_SHARED=ON \
    -DENABLE_CLANG_JIT=ON \
    -DPY_SKIP_DEB_LAYOUT=ON \
    -DPYTHON_CMD=${PYTHON} \
    -DPYTHON_FLAGS=--install-lib=${PYTHON_SITEPACKAGES_DIR} \
"

do_install:append() {
        sed -e 's@#!/usr/bin/env python@#!/usr/bin/env python3@g' \
            -i $(find ${D}${datadir}/${PN} -type f)
        sed -e 's@#!/usr/bin/python.*@#!/usr/bin/env python3@g' \
            -i $(find ${D}${datadir}/${PN} -type f)
        rm -rf ${D}${datadir}/bcc/examples/lua
}

do_install_ptest() {
    install -d ${D}${PTEST_PATH}/tests/cc
    install ${B}/tests/cc/test_libbcc_no_libbpf ${B}/tests/cc/libusdt_test_lib.so ${D}${PTEST_PATH}/tests/cc
    cp -rf ${S}/tests/python ${D}${PTEST_PATH}/tests/python
    install ${UNPACKDIR}/ptest_wrapper.sh ${D}${PTEST_PATH}/tests
    install ${S}/examples/networking/simulation.py ${D}${PTEST_PATH}/tests/python
}

FILES:${PN} += "${PYTHON_SITEPACKAGES_DIR}"
FILES:${PN}-doc += "${datadir}/${PN}/man"

COMPATIBLE_HOST = "(x86_64.*|aarch64.*|powerpc64.*|riscv64.*)-linux"

# WARNING: bcc-0.30.0+git-r0 do_package_qa: QA Issue: File /usr/lib/bcc/ptest/tests/cc/test_libbcc_no_libbpf in package bcc-ptest contains reference to TMPDIR [buildpaths]
# this one is difficult to resolve, because the tests use CMAKE_CURRENT_BINARY_DIR directly in .cc e.g.:
# https://github.com/iovisor/bcc/commit/7271bfc946a19413761be2e3c60c48bf72c5eea1#diff-233a0bfa490f3d7466c49935b64c86dd93956bbc0461f5af703b344cf6601461
# we would probably need to use separate variable for "runtime" path for test assets from the standard CMAKE_CURRENT_BINARY_DIR variable or use relative
# path from the test binary
WARN_QA:append = "${@bb.utils.contains('PTEST_ENABLED', '1', ' buildpaths', '', d)}"
ERROR_QA:remove = "${@bb.utils.contains('PTEST_ENABLED', '1', 'buildpaths', '', d)}"
