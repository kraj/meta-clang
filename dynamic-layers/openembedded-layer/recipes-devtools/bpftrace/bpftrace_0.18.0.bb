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

PV .= "+git${SRCREV}"
RDEPENDS:${PN} += "bash python3 xz"

SRC_URI = "git://github.com/iovisor/bpftrace;branch=master;protocol=https \
           file://0001-cmake-Raise-max-llvm-major-version-to-16.patch \
           file://0001-Adjust-to-build-with-llvm-17.patch \
           file://run-ptest \
"
SRCREV = "e199c7e73da84bff9fe744d1e3402c2b505aa5a2"

S = "${WORKDIR}/git"

inherit cmake ptest

PACKAGECONFIG ?= "${@bb.utils.contains('PTEST_ENABLED', '1', 'tests', '', d)}"

# Clang-15.x crashes compiling some usdt tests
# see https://github.com/llvm/llvm-project/issues/58477
PACKAGECONFIG:remove:riscv64 = "tests"

PACKAGECONFIG[tests] = "-DBUILD_TESTING=ON,-DBUILD_TESTING=OFF,gtest xxd-native"

do_install_ptest() {
    if [ -e ${B}/tests/bpftrace_test ]; then
        install -Dm 755 ${B}/tests/bpftrace_test ${D}${PTEST_PATH}/tests/bpftrace_test
        cp -rf ${B}/tests/runtime ${D}${PTEST_PATH}/tests
        cp -rf ${B}/tests/test* ${D}${PTEST_PATH}/tests
    fi
}

def llvm_major_version(d):
    pvsplit = d.getVar('LLVMVERSION').split('.')
    return pvsplit[0]

LLVM_MAJOR_VERSION = "${@llvm_major_version(d)}"

EXTRA_OECMAKE = " \
    -DCMAKE_ENABLE_EXPORTS=1 \
    -DCMAKE_BUILD_TYPE=Release \
    -DLLVM_REQUESTED_VERSION=${LLVM_MAJOR_VERSION} \
    -DUSE_SYSTEM_BPF_BCC=ON \
    -DENABLE_MAN=OFF \
"

COMPATIBLE_HOST = "(x86_64.*|aarch64.*|powerpc64.*|riscv64.*)-linux"
COMPATIBLE_HOST:libc-musl = "null"
