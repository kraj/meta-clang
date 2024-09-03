SUMMARY = "bpftrace"
HOMEPAGE = "https://github.com/iovisor/bpftrace"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS += "bison-native \
            flex-native \
            gzip-native \
            elfutils \
            bcc \
            systemtap \
            libcereal \
            libbpf \
            "
DEPENDS += "${@bb.utils.contains('PTEST_ENABLED', '1', 'pahole-native llvm-native', '', d)}"

RDEPENDS:${PN} += "bash python3 xz"

PV .= "+git"

SRC_URI = "git://github.com/iovisor/bpftrace;branch=master;protocol=https \
           file://run-ptest \
           file://0001-cmake-Bump-max-LLVM-version-to-19.patch \
           file://0002-CMakeLists.txt-allow-to-set-BISON_FLAGS-like-l.patch \
"
SRCREV = "b2e255870ba010d4a7e4852bffcf1c567b016fd0"

S = "${WORKDIR}/git"

inherit cmake ptest

PACKAGECONFIG ?= "${@bb.utils.contains('PTEST_ENABLED', '1', 'tests', '', d)}"

PACKAGECONFIG[tests] = "-DBUILD_TESTING=ON,-DBUILD_TESTING=OFF,gtest xxd-native"

do_install_ptest() {
    if [ -e ${B}/tests/bpftrace_test ]; then
        install -Dm 755 ${B}/tests/bpftrace_test ${D}${PTEST_PATH}/tests/bpftrace_test
        cp -rf ${B}/tests/runtime ${D}${PTEST_PATH}/tests
        cp -rf ${B}/tests/test* ${D}${PTEST_PATH}/tests
    fi
}

EXTRA_OECMAKE = " \
    -DCMAKE_ENABLE_EXPORTS=1 \
    -DCMAKE_BUILD_TYPE=Release \
    -DUSE_SYSTEM_BPF_BCC=ON \
    -DENABLE_MAN=OFF \
    -DBISON_FLAGS='--file-prefix-map=${WORKDIR}=' \
"

COMPATIBLE_HOST = "(x86_64.*|aarch64.*|powerpc64.*|riscv64.*)-linux"
COMPATIBLE_HOST:libc-musl = "null"

INHIBIT_PACKAGE_STRIP_FILES += "\
    ${PKGD}${PTEST_PATH}/tests/testprogs/uprobe_test \
"

WARN_QA:append = "${@bb.utils.contains('PTEST_ENABLED', '1', ' buildpaths', '', d)}"
ERROR_QA:remove = "${@bb.utils.contains('PTEST_ENABLED', '1', 'buildpaths', '', d)}"
